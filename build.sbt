name := MyBuild.NamePrefix + "root"

version := "0.0.1"

scalaVersion := "2.11.8"

lazy val api = project.
    settings(Common.settings: _*).
    settings(libraryDependencies ++= Dependencies.apiDependencies)

lazy val apiJson = project.
    in(file("api-json")).
  dependsOn(api, api % "test->test;compile->compile").
    settings(Common.settings: _*).
    settings(libraryDependencies ++= Dependencies.apiJsonDependencies)

lazy val web = project.
    dependsOn(apiJson, apiJson % "test->test;compile->compile").
    settings(Common.settings: _*).
    settings(libraryDependencies ++= Dependencies.webDependencies).
    enablePlugins(PlayScala)

lazy val spark = project.
    dependsOn(api, api % "test->test;compile->compile").
    settings(Common.settings: _*).
    settings(libraryDependencies ++= Dependencies.sparkDependencies)

lazy val root = (project in file(".")).
    aggregate(api, apiJson, web, spark)
