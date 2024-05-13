import scoverage.ScoverageKeys

ThisBuild / scalaVersion := "2.13.13"
ThisBuild / majorVersion := 0

val appName = "estates-playback-stub"

val excludedPackages = Seq(
  "<empty>",
  ".*Routes.*"
)

lazy val microservice = Project(appName, file("."))
  .enablePlugins(PlayScala, SbtDistributablesPlugin)
  .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
  .settings(
    PlayKeys.playDefaultPort := 8833,
    ScoverageKeys.coverageExcludedFiles := excludedPackages.mkString(";"),
    ScoverageKeys.coverageMinimumStmtTotal := 98,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true,
    scalacOptions ++= Seq(
      "-feature",
      "-Wconf:src=routes/.*:s"
    ),
    libraryDependencies ++= AppDependencies()
  )

addCommandAlias("scalastyleAll", "all scalastyle Test/scalastyle")
