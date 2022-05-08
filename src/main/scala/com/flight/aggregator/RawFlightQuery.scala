package com.flight.aggregator

import com.typesafe.scalalogging.LazyLogging
import net.liftweb.json.DefaultFormats
import net.liftweb.json._

/**
 *  RawFlightQuery Case Class
 *  This class represents the payload returned from the OpenSkyNetwork
 */
case class RawFlightQuery(time:Int, states:List[RawFlightRecord]) {

  // Generate FlightSummary objects using flight records in the RawFlightQuery object
  def generateSummary(): Option[FlightSummary] = {

    val reducedFlights = states.map(flight => FlightSummary(time, 1, flight.velocity, flight.geoAltitude))
      .reduce((fs1, fs2) => FlightSummary(fs1.time,
        fs1.flight_count + fs2.flight_count,
        (fs1.average_velocity ++ fs2.average_velocity).reduceLeftOption(_ + _),
        (fs1.average_altitude ++ fs2.average_altitude).reduceLeftOption(_ + _)))

    for {
        velocity <- reducedFlights.average_velocity
        altitude <- reducedFlights.average_altitude
        average_velocity = Option(velocity / states.length)
        average_altitude = Option(altitude / states.length)
    } yield FlightSummary(reducedFlights.time, reducedFlights.flight_count, average_velocity, average_altitude)

  }
}

object RawFlightQuery extends LazyLogging {

  implicit val formats: DefaultFormats.type = DefaultFormats
  val FULL_RECORD_LENGTH = 18

  // flight records periodically send an undocumented field that is still tracked however sometimes it is missing
  // this will ensure unknown field is defaulted if missing
  def valid(item:JValue): Boolean = {
    if (item.children.length < FULL_RECORD_LENGTH) {
      logger.warn("Record received with invalid length: {}. Replacing with empty value", item.children.length)
      false
    } else true
  }

  // Create a RawFlightQuery object given json string
  def apply(json:String): RawFlightQuery = {
    val tree = parse(json)
    val time = (System.currentTimeMillis() /1000 ).intValue
    val elements = (tree \\ "states").children
    val states = elements.flatMap(_.children)
                       .map(item  => {

                         val missingUnknownField = if (!valid(item)) 0 else item(FULL_RECORD_LENGTH - 1).extract[Int]
                         RawFlightRecord(item(0).extract[String],
                         Option(item(1).extract[String].trim), item(2).extract[String], Option(item(3).extract[Int]),
                         item(4).extract[Int], Option(item(5).extract[Double]), Option(item(6).extract[Double]),
                         Option(item(7).extract[Double]), item(8).extract[Boolean], Option(item(9).extract[Double]), Option(item(10).extract[Double]),
                         Option(item(11).extract[Double]), Option(item(12).extract[List[Int]].mkString("|")), Option(item(13).extract[Double]),
                         Option(item(14).extract[String]), item(15).extract[Boolean], item(16).extract[Int],missingUnknownField)

                       })

    return RawFlightQuery(time,states)
  }

}