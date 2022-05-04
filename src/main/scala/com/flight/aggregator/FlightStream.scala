package com.flight.aggregator

import akka.{Done, NotUsed}
import akka.actor.{ActorSystem, Cancellable}
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding.Get
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.stream.{ClosedShape, IOResult}
import akka.stream.alpakka.slick.scaladsl.{Slick, SlickSession}
import akka.stream.scaladsl.{Broadcast, FileIO, Flow, Framing, GraphDSL, Keep, RunnableGraph, Sink, Source}
import akka.util.ByteString
import com.flight.aggregator.MainApp.{flightApi, session, system}
import com.flight.aggregator.config.FlightApi

import scala.concurrent.Future
import scala.concurrent.duration.{FiniteDuration, SECONDS}

object FlightStream {

  import session.profile.api._

  private def initDb()(implicit system:ActorSystem, session:SlickSession):Unit = {
    val flightRecords = TableQuery[RawFlightRecordsTable]
    val flightSummaries = TableQuery[FlightSummaryTable]
    val flightRecordSchema = flightRecords.schema
    val flightSummarySchema = flightSummaries.schema

    system.registerOnTermination(() => session.close())
    session.db.run(DBIO.seq(flightRecordSchema.createIfNotExists))
    session.db.run(DBIO.seq(flightSummarySchema.createIfNotExists))
  }

  private def getPollingSource()(implicit flightApi: FlightApi): Source[HttpRequest, Cancellable] = {
    val pollIntervalInSeconds = flightApi.pollInterval
    Source.tick(FiniteDuration(1,SECONDS), FiniteDuration(pollIntervalInSeconds,SECONDS),Get(flightApi.flightStatePath + "?" + flightApi.searchBoundary))
  }

  private def getFlightRecordSink()(implicit session:SlickSession):Sink[RawFlightRecord, Future[Done]] = {
    Slick.sink[RawFlightRecord](toStatement = record =>
      sqlu"""INSERT INTO flight_records VALUES(${record.icao24}, ${record.callsign}, ${record.originCountry}, ${record.timePosition},
            ${record.lastContact},${record.longitude}, ${record.latitude}, ${record.baroAltitude}, ${record.onGround}, ${record.velocity},${record.trueTrack},
            ${record.verticalRate}, ${record.sensors}, ${record.geoAltitude}, ${record.squawk}, ${record.spi}, ${record.positionSource}, ${record.unknownField})""")(session)
  }

  private def getFlightSummarySink()(implicit  session:SlickSession):Sink[FlightSummary,Future[Done]] ={
    Slick.sink[FlightSummary](toStatement = record =>
      sqlu"""INSERT INTO flight_summaries VALUES(${record.time}, ${record.flight_count}, ${record.average_velocity}, ${record.average_altitude})""")(session)

  }

  def getStream()(implicit system:ActorSystem, session:SlickSession, flightApi:FlightApi): RunnableGraph[NotUsed] ={

    // initialize the database

    // obtain polling source
    val openSkySource = getPollingSource()

    // define flight data sinks
    val rawFlightSink = getFlightRecordSink()
    val flightSummarySink = getFlightSummarySink()

    // define ingest/transformation flows
    val httpClient = Http().outgoingConnectionHttps(host = flightApi.host)
    val filterSuccess = Flow[HttpResponse].filter(_.status == StatusCodes.OK)
    val getBytes = Flow[HttpResponse].flatMapConcat(resp => resp.entity.getDataBytes())
    val createRawQuery = Flow[ByteString].map(result => RawFlightQuery(result.utf8String))

    // define flight summary flows
    val summaryFlow: Flow[RawFlightQuery, FlightSummary, NotUsed] =  Flow[RawFlightQuery].map(query => query.generateSummary().get)
    val printSummary = Flow[FlightSummary].map(summary => {println(summary); summary})

    // define flight record flows
    val extractFlightState = Flow[RawFlightQuery].mapConcat(query => query.states)
    val printRecord = Flow[RawFlightRecord].map(record => {println(record); record})

    //assembly and return stream graph
    RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
      import GraphDSL.Implicits._
      val bcast = builder.add(Broadcast[RawFlightQuery](2))

      // Get endpoint and transform data to raw query
      openSkySource ~> httpClient ~> filterSuccess ~> getBytes ~> createRawQuery ~> bcast

      // save summaries from query
      bcast ~> summaryFlow ~> printSummary ~> flightSummarySink
      // save flight records from query
      bcast ~> extractFlightState ~> printRecord ~> rawFlightSink

      ClosedShape
    })
  }

}
