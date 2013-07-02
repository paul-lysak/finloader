import sbt._
import Keys._

object HelloBuild extends Build {


  lazy val root = Project(id = "finloader",
    base = file(".")).
    configs(IntegrationTest).
    settings(Defaults.itSettings : _*)
}