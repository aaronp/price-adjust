name := MyBuild.NamePrefix + "root"

version := "0.0.1"

scalaVersion := "2.11.7"

lazy val common = project.
    settings(Common.settings: _*)

lazy val api = project.
    settings(Common.settings: _*).
    settings(libraryDependencies ++= Dependencies.apiDependencies)

lazy val apiJson = project.
    in(file("api-json")).
    settings(Common.settings: _*).
    settings(libraryDependencies ++= Dependencies.apiJsonDependencies)

lazy val client = project.
    dependsOn(api).
    settings(Common.settings: _*).
    settings(libraryDependencies ++= Dependencies.clientDependencies)

lazy val domain = project.
    dependsOn(api).
    settings(Common.settings: _*).
    settings(libraryDependencies ++= Dependencies.domainDependencies)

lazy val web = project.
    dependsOn(api, common, domain % "test->test;compile->compile").
    settings(Common.settings: _*).
    settings(libraryDependencies ++= Dependencies.webDependencies).
    enablePlugins(PlayScala)

lazy val spark = project.
    dependsOn(api, common).
    settings(Common.settings: _*).
    settings(libraryDependencies ++= Dependencies.sparkDependencies)

lazy val search = project.
    dependsOn(api, common).
    settings(Common.settings: _*).
    settings(libraryDependencies ++= Dependencies.searchDependencies)

lazy val neo = project.
    in(file("neo")).
  settings(Common.settings: _*).
  settings(libraryDependencies ++= Dependencies.neoDependencies)

lazy val domainDao = project.
    in(file("domain-dao")).
    dependsOn(domain, common, neo).
    settings(Common.settings: _*).
    settings(libraryDependencies ++= Dependencies.domainDaoDependencies)

//lazy val avro = project.
//    in(file("domain-avro")).
//    dependsOn(domain, common).
//    settings(Common.settings: _*).
//    settings(libraryDependencies ++= Dependencies.avroDependencies)

lazy val domainJson = project.
    in(file("domain-json")).
    dependsOn(domain, common).
    settings(Common.settings: _*).
    settings(libraryDependencies ++= Dependencies.domainJsonDependencies)

lazy val root = (project in file(".")).
    aggregate(api, common, neo, client, domain, domainJson, domainDao, web, search, spark)
