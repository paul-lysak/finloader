import sbtassembly.Plugin._
import AssemblyKeys._

name := "finloader"

version := "1.0"

scalaVersion := "2.10.1"

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= List(
  "com.typesafe.slick" % "slick_2.10" % "2.0.0-M2",
  "com.typesafe" % "config" % "1.0.2",
  "com.github.tototoshi" %% "scala-csv" % "0.8.0",
  "joda-time" % "joda-time" % "2.3",
  "com.github.tototoshi" %% "slick-joda-mapper" % "1.0.0-SNAPSHOT",
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "org.rogach" %% "scallop" % "0.9.3",
  "com.typesafe" %% "scalalogging-slf4j" % "1.0.1",
  "org.slf4j" % "slf4j-log4j12" % "1.6.4",
  "org.specs2" %% "specs2" % "1.14" % "test,it"
)

assemblySettings

//mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
//  {
//    case PathList("scala", xs @ _*) => MergeStrategy.first
//    case x => old(x)
////    case _ => MergeStrategy.first
//  }
//}

