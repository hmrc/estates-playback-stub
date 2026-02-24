ThisBuild / scalaVersion := "3.3.7"
ThisBuild / majorVersion := 1

lazy val microservice = Project("estates-playback-stub", file("."))
  .enablePlugins(PlayScala, SbtDistributablesPlugin)
  .disablePlugins(JUnitXmlReportPlugin) // Required to prevent https://github.com/scalatest/scalatest/issues/1427
  .settings(
    PlayKeys.playDefaultPort := 8833,
    CodeCoverageSettings(),
    scalacOptions ++= Seq(
      "-feature",
      "-Wconf:src=routes/.*:s"
    ),
    libraryDependencies ++= AppDependencies()
  )

addCommandAlias("scalafmtAll", "all scalafmtSbt scalafmt Test/scalafmt")
