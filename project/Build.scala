import sbt._
import Keys._

object HelloBuild extends Build {


  lazy val root = Project(id = "finloader",
    base = file(".")).
    configs(IntegrationTest).
    settings(Defaults.itSettings : _*).
    settings(testOptions in IntegrationTest += Tests.Setup(DB.create _)).
    settings(testOptions in IntegrationTest += Tests.Cleanup(DB.drop _))
}

