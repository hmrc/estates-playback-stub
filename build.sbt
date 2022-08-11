import scoverage.ScoverageKeys
import uk.gov.hmrc.DefaultBuildSettings.integrationTestSettings
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings

val appName = "estates-playback-stub"

val excludedPackages = Seq(
  "<empty>",
  ".*Reverse.*",
  ".*Routes.*",
  ".*standardError*.*",
  ".*main_template*.*",
  "uk.gov.hmrc.BuildInfo",
  "app.*",
  "prod.*",
  "config.*",
  "testOnlyDoNotUseInAppConf.*",
  "views.html.*",
  "testOnly.*",
  "com.kenshoo.play.metrics*.*",
  ".*LocalDateService.*",
  ".*LocalDateTimeService.*",
  ".*RichJsValue.*",
  ".*Repository.*"
)

lazy val microservice = Project(appName, file("."))
  .enablePlugins(PlayScala, SbtDistributablesPlugin)
  .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
  .settings(
    PlayKeys.playDefaultPort := 8833,
    ScoverageKeys.coverageExcludedFiles := excludedPackages.mkString(";"),
    ScoverageKeys.coverageMinimumStmtTotal := 80,        // all new JSON files must be unit tested
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true
  )
  .settings(
    scalaVersion := "2.12.16",
    SilencerSettings(),
    majorVersion                     := 0,
    libraryDependencies              ++= AppDependencies.compile ++ AppDependencies.test,
    dependencyOverrides              ++= AppDependencies.overrides
  )
  .settings(publishingSettings: _*)
  .configs(IntegrationTest)
  .settings(integrationTestSettings(): _*)
  .settings(resolvers += Resolver.jcenterRepo)
