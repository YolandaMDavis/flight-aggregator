package com.flight.aggregator

import com.typesafe.scalalogging.LazyLogging
import net.liftweb.json.DefaultFormats
import net.liftweb.json._

case class RawFlightQuery(time:Long, states:List[RawFlightRecord]) {

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

  // flight records periodically send an undocumented field that we still track but sometimes it is missing
  // this will validate wheter or not field exists based on length
  def valid(item:JValue): Boolean = {
    if (item.children.length < FULL_RECORD_LENGTH) {
      logger.warn("Record received with invalid length: {}. Replacing with empty value", item.children.length)
      false
    } else true
  }

  def apply(json:String): RawFlightQuery = {
    val tree = parse(json)
    val time = System.currentTimeMillis()
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