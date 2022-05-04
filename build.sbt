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
libraryDependencies += "org.postgresql" % "postgresql" % "42.3.4"

mainClass in Compile := Some("com.flight.aggregator.MainApp")

enablePlugins(DockerPlugin)

docker / dockerfile := {
  // The assembly task generates a fat JAR file
  val artifact: File = assembly.value
  val artifactTargetPath = s"/app/${artifact.name}"

  new Dockerfile {
    from("openjdk:11-jre")
    add(artifact, artifactTargetPath)
    entryPoint("java", "-jar", artifactTargetPath)
  }
}

// Custom task to zip files for homework submission
lazy val zipHomework = taskKey[Unit]("zip files for project submission")

zipHomework := {
  val bd = baseDirectory.value
  val targetFile = s"${bd.getAbsolutePath}/scalaHomework.zip"
  val ignoredPaths =
    ".*(\\.idea|target|\\.DS_Store|\\.bloop|\\.metals|\\.vsc)/*".r.pattern
  val fileFilter = new FileFilter {
    override def accept(f: File) =
      !ignoredPaths.matcher(f.getAbsolutePath).lookingAt
  }
  println("zipping homework files ...")
  IO.delete(new File(targetFile))
  IO.zip(
    Path.selectSubpaths(new File(bd.getAbsolutePath), fileFilter),
    new File(targetFile)
  )
}
