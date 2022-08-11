import play.core.PlayVersion.current
import sbt._

object AppDependencies {

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"    %% "bootstrap-backend-play-28" % "6.4.0",
    "com.github.fge" %  "json-schema-validator"     % "2.2.14"
  )

  val test: Seq[ModuleID] = Seq(
    "org.scalatest"           %% "scalatest"          % "3.2.13" % "test",
    "com.typesafe.play"       %% "play-test"          % current  % "test",
    "org.pegdown"             %  "pegdown"            % "1.6.0"  % "test",
    "org.scalatestplus.play"  %% "scalatestplus-play" % "5.1.0"  % "test",
    "com.vladsch.flexmark"    %  "flexmark-all"       % "0.62.2" % "test"
  )

  val akkaVersion = "2.6.7"
  val akkaHttpVersion = "10.1.12"

  val overrides = Seq(
    "com.typesafe.akka" %% "akka-stream_2.12"     % akkaVersion,
    "com.typesafe.akka" %% "akka-protobuf_2.12"   % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j_2.12"      % akkaVersion,
    "com.typesafe.akka" %% "akka-actor_2.12"      % akkaVersion,
    "com.typesafe.akka" %% "akka-http-core_2.12"  % akkaHttpVersion
  )

}
