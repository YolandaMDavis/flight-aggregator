name := "flight-aggregator"

version := "0.1"

scalaVersion := "2.13.8"

val AkkaVersion = "2.6.19"

val AkkaHttpVersion = "10.2.9"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % AkkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % AkkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4"
libraryDependencies += "net.liftweb" %% "lift-json" % "3.5.0"
libraryDependencies += "com.github.pureconfig" %% "pureconfig" % "0.15.0"
libraryDependencies += "com.lightbend.akka" %% "akka-stream-alpakka-slick" % "3.0.4"
libraryDependencies += "com.h2database" % "h2" % "2.1.212"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"