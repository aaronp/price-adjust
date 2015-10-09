import sbt._
import Keys._

object Dependencies {

  val slf4jVersion = "1.6.4"
  val slf4jNop = "org.slf4j" % "slf4j-nop" % slf4jVersion

  val commonDependencies: Seq[ModuleID] = Seq(
    "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    slf4jNop,
    "org.threeten" % "threetenbp" % "1.3",
    "junit" % "junit" % "4.12" % "test"
  )

  val sparkVersion = "1.4.1"
  
  val json : Seq[ModuleID] = Seq(
      "io.argonaut" %% "argonaut" % "6.0.4",
      "com.propensive" %% "rapture-json-argonaut" % "1.1.0",
      "com.typesafe.play" %% "play-json" % "2.4.2")

  val apiDependencies    : Seq[ModuleID] = commonDependencies
  val domainDependencies : Seq[ModuleID] = commonDependencies
  val clientDependencies : Seq[ModuleID] = commonDependencies
  val apiJsonDependencies: Seq[ModuleID] = commonDependencies ++ json
  val searchDependencies : Seq[ModuleID] = commonDependencies ++ Seq(
    "com.sksamuel.elastic4s" %% "elastic4s-core" %  "1.7.0",
    "com.sksamuel.elastic4s" %% "elastic4s-testkit" % "1.7.0" % "test"
  )
  val domainJsonDependencies : Seq[ModuleID] = commonDependencies ++ json
  val domainDaoDependencies : Seq[ModuleID] = commonDependencies ++ Seq(
    "org.mongodb" %% "casbah" % "2.8.2",
    "com.github.fakemongo" % "fongo" % "2.0.1" % "test",
    "org.anormcypher" %% "anormcypher" % "0.6.0"
    // "org.neo4j" % "neo4j-spatial-scala" % "0.1.0-SNAPSHOT",
    // "eu.fakod" %% "neo4j-scala" % "0.3.0"
  )
  val neoDependencies: Seq[ModuleID] = commonDependencies ++ Seq(
    "org.anormcypher" %% "anormcypher" % "0.6.0"
  )

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