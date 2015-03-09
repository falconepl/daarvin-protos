
version in LocalRootProject := "0.1-SNAPSHOT"

lazy val commonSettings = Seq(
  organization := "pl.falcone",
  scalaVersion := "2.11.6"
)

val akka = "com.typesafe.akka" %% "akka-actor" % "2.3.9"

lazy val daarvinProto = (project in file("daarvin-proto")).
  settings(commonSettings: _*).
  settings(
    name := "daarvin-proto",
    version := "0.1-SNAPSHOT",
    libraryDependencies ++= Seq(akka)
  )

lazy val simpleAgents = (project in file("simple-agents")).
  settings(commonSettings: _*).
  settings(
    name := "simple-agents",
    version := "0.1-SNAPSHOT",
    libraryDependencies ++= Seq(akka)
  ).dependsOn(
    daarvinProto
  )

lazy val golombRule = (project in file("golomb-rule")).
  settings(commonSettings: _*).
  settings(
    name := "golomb-rule",
    version := "0.1-SNAPSHOT"
  )
