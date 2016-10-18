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
    dependsOn(api).
    settings(Common.settings: _*).
    settings(libraryDependencies ++= Dependencies.sparkDependencies)

lazy val search = project.
    dependsOn(api, apiJson).
    settings(Common.settings: _*).
    settings(libraryDependencies ++= Dependencies.searchDependencies)

lazy val graph = project.
    in(file("graph")).
  settings(Common.settings: _*).
  settings(libraryDependencies ++= Dependencies.graphDependencies)

lazy val mongo = project.
  dependsOn(api, apiJson).
    in(file("mongo")).
    settings(Common.settings: _*).
    settings(libraryDependencies ++= Dependencies.mongoDependencies)

lazy val rest = project.
    in(file("rest")).
    dependsOn(apiJson, mongo).
    settings(Common.settings: _*).
    settings(libraryDependencies ++= Dependencies.akkaHttpDependencies)

lazy val root = (project in file(".")).
    aggregate(api, mongo, graph, rest, apiJson, web, rest, search, spark)
