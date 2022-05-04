package com.flight.aggregator.config

import pureconfig.{ ConfigReader, ConfigSource }
import scala.reflect.ClassTag

/**
 *  Helper class to store search boundary for flight retrieval
 * @param latMin
 * @param latMax
 * @param lonMin
 * @param lonMax
 */
case class SearchBoundary(latMin: Double, latMax: Double, lonMin:Double, lonMax: Double) {
  override def toString: String = s"lamin=${latMin}&lomin=${lonMin}&lamax=${latMax}&lomax=${lonMax}"
}

/***
 * Configuration data for Flight API settings
 * @param host
 * @param flightStatePath
 * @param pollInterval
 * @param searchBoundary
 */
case class FlightApi(host: String, flightStatePath: String, pollInterval: Long, searchBoundary: SearchBoundary)

object ConfigUtils {

  /** loads a configuration case class
   */
  def loadAppConfig[A: ConfigReader: ClassTag](path: String): A =
    ConfigSource.default.at(path).loadOrThrow[A]
}