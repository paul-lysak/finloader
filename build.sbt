name := "finloader"

version := "1.0"

scalaVersion := "2.10.1"

libraryDependencies ++= List(
  // use the right Slick version here:
  "com.typesafe.slick" %% "slick" % "1.0.1",
  "com.typesafe" % "config" % "1.0.2",
  "com.github.tototoshi" %% "scala-csv" % "0.8.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
//  "com.h2database" % "h2" % "1.3.166",
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "org.specs2" %% "specs2" % "1.14" % "test,it"
)