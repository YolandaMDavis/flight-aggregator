package com.flight.aggregator

import slick.jdbc.PostgresProfile.api._
/*
* Flight Summary
* Class used to store aggregated values for retrieved data for faster querying. Data is aggregated within the retrieved set of active flights
*/
case class FlightSummary(time:Long, flight_count:Int,  average_velocity:Option[Double], average_altitude:Option[Double])

class FlightSummaryTable(tag:Tag) extends Table[FlightSummary](tag, "flight_summaries"){
  def time = column[Long]("time")
  def flight_count = column[Int]("flight_count")
  def average_velocity = column[Option[Double]]("average_velocity")
  def average_altitude = column[Option[Double]]("average_altitude")
  def * = (time, flight_count, average_velocity, average_altitude) <> (FlightSummary.tupled, FlightSummary.unapply)
}