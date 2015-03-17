
version in LocalRootProject := "0.1-SNAPSHOT"

lazy val commonSettings = Seq(
  organization := "pl.falcone",
  scalaVersion := "2.11.6",
  resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)

val akka = "com.typesafe.akka" %% "akka-actor" % "2.3.9"
val scalameter = "com.storm-enroute" %% "scalameter" % "0.7-SNAPSHOT"

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

lazy val golombRuler = (project in file("golomb-ruler")).
  settings(commonSettings: _*).
  settings(
    name := "golomb-ruler",
    version := "0.1-SNAPSHOT",
    libraryDependencies ++= Seq(scalameter),
    testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework"),
    parallelExecution in Test := false
  )
