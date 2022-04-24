package com.flight.aggregator

import com.typesafe.scalalogging.LazyLogging
import akka.actor.{Actor, ActorSystem, Props, Status}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.event.Logging

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try
import akka.http.scaladsl.client.RequestBuilding._
import akka.http.scaladsl.model.headers.BasicHttpCredentials
import akka.stream.alpakka.slick.scaladsl.{Slick, SlickSession}
import akka.util.ByteString
import com.flight.aggregator.config.{ConfigUtils, FlightApi}
import net.liftweb.json._
import pureconfig.generic.auto._
import akka.stream.alpakka.slick.scaladsl._
import akka.stream.scaladsl._
import slick.jdbc.GetResult
import slick.lifted._

import scala.concurrent.duration.{FiniteDuration, SECONDS}

object MainApp extends LazyLogging{

  implicit val system: ActorSystem = ActorSystem("Sys")
  implicit val formats: DefaultFormats.type = DefaultFormats
  implicit val CONFIG_PATH = "com.flight.aggregator.flight-api"
  implicit val flightApi: FlightApi = ConfigUtils.loadAppConfig[FlightApi](CONFIG_PATH)
  implicit val session: SlickSession = SlickSession.forConfig("com.flight.aggregator.slick-postgres")

  def main(args: Array[String]): Unit = {
    import session.profile.api._
    val httpClient = Http().outgoingConnectionHttps(host = flightApi.host)
    val filterSuccess = Flow[HttpResponse].filter(_.status == StatusCodes.OK)
    val pollIntervalInSeconds = flightApi.pollInterval
    val flightRecords = TableQuery[RawFlightRecordsTable]
    val schema = flightRecords.schema

    system.registerOnTermination(() => session.close())
    session.db.run(DBIO.seq(schema.createIfNotExists))

    val dbSink = Slick.flow[RawFlightRecord](toStatement = record =>
            sqlu"""INSERT INTO flight_records VALUES(${record.icao24}, ${record.callsign}, ${record.originCountry}, ${record.timePosition},
            ${record.lastContact},${record.longitude}, ${record.latitude}, ${record.baroAltitude}, ${record.onGround}, ${record.velocity},${record.trueTrack},
            ${record.verticalRate}, ${record.sensors}, ${record.geoAltitude}, ${record.squawk}, ${record.spi}, ${record.positionSource}, ${record.unknownField})""")(session)

    val poll = Source.tick(FiniteDuration(1,SECONDS), FiniteDuration(pollIntervalInSeconds,SECONDS),Get(flightApi.flightStatePath + "?" + flightApi.searchBoundary))
              .via(httpClient)
              .via(filterSuccess)
              .flatMapConcat(resp => resp.entity.getDataBytes())
              .map(result => RawFlightQuery(result.utf8String))
              .mapConcat(query => query.states)
      .map(record => {println(record); record})
              .via(dbSink)
        .log("rows")
        .to(Sink.ignore)
    poll.run()

  }


}
