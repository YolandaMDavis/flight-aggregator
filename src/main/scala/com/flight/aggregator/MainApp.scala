package com.flight.aggregator

import com.typesafe.scalalogging.LazyLogging
import akka.actor.ActorSystem

import akka.stream.alpakka.slick.scaladsl.SlickSession

import com.flight.aggregator.config.{ConfigUtils, FlightApi}
import net.liftweb.json._
import pureconfig.generic.auto._


object MainApp extends LazyLogging{

  implicit val system: ActorSystem = ActorSystem("Sys")
  implicit val formats: DefaultFormats.type = DefaultFormats
  implicit val CONFIG_PATH = "com.flight.aggregator.flight-api"
  implicit val flightApi: FlightApi = ConfigUtils.loadAppConfig[FlightApi](CONFIG_PATH)
  implicit val session: SlickSession = SlickSession.forConfig("com.flight.aggregator.slick-postgres")

  def main(args: Array[String]): Unit = {

    // create and run ingest flow for flights
    val poller = FlightStream.getStream()
    poller.run()

  }

}
