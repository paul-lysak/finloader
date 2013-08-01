import sbt._
import Keys._
import sbtassembly.Plugin._
import AssemblyKeys._

object HelloBuild extends Build {


  lazy val root = Project(id = "finloader",
    base = file(".")).
    configs(IntegrationTest).
    settings(Defaults.itSettings : _*).
    settings(assemblySettings: _*).
    settings(testOptions in IntegrationTest += Tests.Setup(DbSetupUtils.create _)).
    settings(testOptions in IntegrationTest += Tests.Cleanup(DbSetupUtils.drop _))
}

