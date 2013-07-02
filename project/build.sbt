name := "finloader_build"

version := "1.0"

scalaVersion := "2.10.1"

libraryDependencies ++= List(
  // use the right Slick version here:
  "com.typesafe.slick" %% "slick" % "1.0.1",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.h2database" % "h2" % "1.3.166",
  "postgresql" % "postgresql" % "9.1-901.jdbc4"
)