package com.flight.aggregator

import com.typesafe.scalalogging.LazyLogging
import net.liftweb.json.DefaultFormats
import net.liftweb.json._

case class RawFlightQuery(time:Long, states:List[RawFlightRecord])

object RawFlightQuery extends LazyLogging{

  implicit val formats: DefaultFormats.type = DefaultFormats
  val FULL_RECORD_LENGTH = 18

  def validate(item:JValue): Boolean = {
    if (item.children.length < FULL_RECORD_LENGTH) {
      logger.warn("Record received with invalid length: {}", item)
      false
    } else true
  }

  def apply(json:String): RawFlightQuery = {
    val tree = parse(json)
    val time = System.currentTimeMillis()
    val elements = (tree \\ "states").children
    val states = elements.flatMap(_.children)
      .filter(validate)
                         .map(item  => RawFlightRecord(item(0).extract[String],
                           Option(item(1).extract[String].trim), item(2).extract[String], Option(item(3).extract[Int]),
                           item(4).extract[Int], Option(item(5).extract[Double]), Option(item(6).extract[Double]),
                           Option(item(7).extract[Double]), item(8).extract[Boolean], Option(item(9).extract[Double]), Option(item(10).extract[Double]),
                           Option(item(11).extract[Double]), Option(item(12).extract[List[Int]].mkString("|")), Option(item(13).extract[Double]),
                           Option(item(14).extract[String]), item(15).extract[Boolean], item(16).extract[Int], item(17).extract[Int]))

    return RawFlightQuery(time,states)
  }

}