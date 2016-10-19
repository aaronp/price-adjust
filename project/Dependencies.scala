import sbt._
import Keys._

object Dependencies {

  val logging = Seq(
    "ch.qos.logback" %  "logback-classic" % "1.1.7",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0")

  val sparkVersion = "2.0.1"

  val circeVersion = "0.5.1"

  val typesafeConfig = "com.typesafe" % "config" % "1.3.1"

  val circeDependencies = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % circeVersion)

  val commonDependencies: Seq[ModuleID] = logging ++ Seq(
    "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    "junit" % "junit" % "4.12" % "test"
  )

  val json : Seq[ModuleID] = circeDependencies ++ Seq(
      "com.typesafe.play" %% "play-json" % "2.4.2")

  val apiDependencies    : Seq[ModuleID] = commonDependencies
  val apiJsonDependencies: Seq[ModuleID] = commonDependencies ++ json
  val searchDependencies : Seq[ModuleID] = commonDependencies ++ Seq(
    "com.sksamuel.elastic4s" %% "elastic4s-core" %  "2.3.1",
    "com.sksamuel.elastic4s" %% "elastic4s-testkit" % "2.3.1" % "test"
  )

  val mongoDependencies : Seq[ModuleID] = commonDependencies ++ circeDependencies ++ Seq(
    //"org.mongodb" %% "casbah" % "3.1.1",
    "org.mongodb.scala" %% "mongo-scala-driver" % "1.0.1",
    "com.github.fakemongo" % "fongo" % "2.0.6" % "test",
    typesafeConfig
    // "org.neo4j" % "neo4j-spatial-scala" % "0.1.0-SNAPSHOT",
    // "eu.fakod" %% "neo4j-scala" % "0.3.0"
  )
  val graphDependencies: Seq[ModuleID] = commonDependencies ++ Seq(
    "org.anormcypher" %% "anormcypher" % "0.6.0",
    typesafeConfig
  )

  val akkaHttpDependencies: Seq[ModuleID] = commonDependencies ++ Seq(
    "com.typesafe.akka" %% "akka-http-core" % "2.4.11",
    "com.typesafe.akka" %% "akka-http-testkit" % "2.4.11")

  val sparkDependencies  : Seq[ModuleID] = commonDependencies ++ Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion,
    "org.apache.spark" %% "spark-sql" % sparkVersion,
    "org.apache.spark" %% "spark-mllib" % sparkVersion,
    "org.apache.spark" %% "spark-streaming" % sparkVersion)
  val webDependencies    : Seq[ModuleID] = commonDependencies ++ {
    Seq(
      play.sbt.PlayImport.cache,
      play.sbt.PlayImport.ws
    )
  }
}