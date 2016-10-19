// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.9")

// Coursier (https://github.com/alexarchambault/coursier#sbt-plugin)
// as great as this is, it breaks intellij :-(
//addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.0-M14")


// docker (https://github.com/marcuslonnberg/sbt-docker)
addSbtPlugin("se.marcuslonnberg" % "sbt-docker" % "1.4.0")

// web plugins

addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.0.6")

addSbtPlugin("com.typesafe.sbt" % "sbt-jshint" % "1.0.3")

addSbtPlugin("com.typesafe.sbt" % "sbt-rjs" % "1.0.7")

addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-mocha" % "1.1.0")

//https://github.com/sbt/sbt-assembly
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.3")
