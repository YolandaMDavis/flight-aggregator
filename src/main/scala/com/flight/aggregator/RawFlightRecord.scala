package com.flight.aggregator

import slick.jdbc.PostgresProfile.api._

case class RawFlightRecord(icao24:String, callsign:Option[String], originCountry:String, timePosition:Option[Int], lastContact:Int,
                           longitude:Option[Double], latitude:Option[Double], baroAltitude:Option[Double], onGround:Boolean, velocity:Option[Double],
                           trueTrack:Option[Double], verticalRate:Option[Double], sensors:Option[String], geoAltitude:Option[Double],
                           squawk:Option[String], spi:Boolean, positionSource:Int, unknownField:Int)

class RawFlightRecordsTable(tag:Tag) extends Table[RawFlightRecord](tag, "flight_records"){
  def icao24 = column[String]("icao24")
  def callsign = column[Option[String]]("callsign")
  def originCountry = column[String]("origin_country")
  def timePosition = column[Option[Int]]("time_position")
  def lastContact = column[Int]("last_contact")
  def longitude = column[Option[Double]]("longitude")
  def latitude = column[Option[Double]]("latitude")
  def baroAltitude = column[Option[Double]]("baroAltitude")
  def onGround = column[Boolean]("onGround")
  def velocity = column[Option[Double]]("velocity")
  def trueTrack = column[Option[Double]]("trueTrack")
  def verticalRate = column[Option[Double]]("verticalRate")
  def sensors = column[Option[String]]("sensors")
  def geoAltitude = column[Option[Double]]("geo_altitue")
  def squawk = column[Option[String]]("squawk")
  def spi = column[Boolean]("spi")
  def positionSource = column[Int]("positionSource")
  def unknownField = column[Int]("unknownField")
  def * = (icao24, callsign, originCountry, timePosition, lastContact,
    longitude, latitude, baroAltitude, onGround, velocity,
    trueTrack, verticalRate, sensors, geoAltitude, squawk, spi,
    positionSource, unknownField) <> (RawFlightRecord.tupled, RawFlightRecord.unapply)
}