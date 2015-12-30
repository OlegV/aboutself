name := "activator-play-slick"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.5" // or "2.10.4"

libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.3.0-2",
  "com.typesafe.play" %% "play-slick" % "0.8.1"
)

fork in Test := false

lazy val root = (project in file(".")).enablePlugins(PlayScala)


libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.38"

libraryDependencies += "com.google.inject" % "guice" % "3.0"

