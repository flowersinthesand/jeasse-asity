lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies += "io.cettia.asity" % "asity-bridge-play2" % "3.0.0"
libraryDependencies += "kim.donghwan" % "jeasse-asity-example" % "0.1.0"

resolvers += Resolver.mavenLocal

PlayKeys.devSettings := Seq("play.server.http.port" -> "8080")