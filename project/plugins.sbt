import sbt._

import Defaults._

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.5.0-SNAPSHOT")

//addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.4.0")

//libraryDependencies += sbtPluginExtra(
//    m = "com.github.mpeltonen" % "sbt-idea" % "1.5.0-SNAPSHOT", // Plugin module name and version
//    sbtV = "0.13.0-M2",    // SBT version
//    scalaV = "2.10"    // Scala version compiled the plugin
//)