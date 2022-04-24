package com.flight.aggregator.config

import pureconfig.{ ConfigReader, ConfigSource }
import scala.reflect.ClassTag

case class SearchBoundary(latMin: Double, latMax: Double, lonMin:Double, lonMax: Double) {
  override def toString: String = s"lamin=${latMin}&lomin=${lonMin}&lamax=${latMax}&lomax=${lonMax}"
}

case class FlightApi(host: String, flightStatePath: String, pollInterval: Long, searchBoundary: SearchBoundary)

object ConfigUtils {

  /** loads a configuration case class
   */
  def loadAppConfig[A: ConfigReader: ClassTag](path: String): A =
    ConfigSource.default.at(path).loadOrThrow[A]
}