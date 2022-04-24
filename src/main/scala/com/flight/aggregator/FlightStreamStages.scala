package com.flight.aggregator

import java.nio.file.Paths

import akka.NotUsed
import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Flow, Framing, Keep, Sink, Source}
import akka.util.ByteString

import scala.concurrent.Future

object FlightStreamStages {

  /**
   * This is a simple example of a flow that reads a file and returns the lines as a stream of strings.
   *
   * @param path path to the file to read
   * @return
   */
  def fileInputSource(path: String): Source[String, Future[IOResult]] = {
    val source: Source[ByteString, Future[IOResult]] = FileIO.fromPath(Paths.get(path))
    val lineSource: Source[ByteString,Future[IOResult]]  = source.via(Framing.delimiter(ByteString("\n"), maximumFrameLength = 256, allowTruncation = true))
    lineSource.map(_.utf8String)
  }

  /**
   * This is a simple flow that transforms a type to a ByteString to write to a file
   * */
  def byteStringFlow[A](): Flow[A, ByteString, NotUsed] = {
    Flow[A].map(a => ByteString(s"$a\n"))
  }
  /**
   * This is a simple Sink that writes to a file
   *
   * @param path
   * @return
   */
  def fileOutputSink[A](path: String): Sink[A, Future[IOResult]] = {
    val sink: Sink[ByteString, Future[IOResult]] = FileIO.toPath(Paths.get(path))
    byteStringFlow().toMat(sink)(Keep.right)
  }

}
