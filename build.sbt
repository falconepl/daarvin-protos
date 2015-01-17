
version in LocalRootProject := "0.1-SNAPSHOT"

lazy val commonSettings = Seq(
  organization := "pl.falcone",
  scalaVersion := "2.11.5"
)

lazy val golombRule = (project in file("golomb-rule")).
  settings(commonSettings: _*).
  settings(
    name := "golomb-rule",
    version := "0.1-SNAPSHOT"
  )
