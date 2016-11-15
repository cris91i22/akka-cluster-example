import sbt.Keys._

val akkaVersion = "2.4.10"

lazy val commonSettings = Seq(
  version := "1.0.0-SNAPSHOT",
  scalaVersion := "2.11.8",
  scalacOptions in Compile ++= Seq("-encoding", "UTF-8", "-target:jvm-1.8", "-deprecation", "-feature", "-unchecked", "-Xlog-reflective-calls", "-Xlint"),
  javacOptions in Compile ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint:unchecked", "-Xlint:deprecation"),
  javaOptions in run ++= Seq("-Xms128m", "-Xmx1024m", "-Djava.library.path=./target/native"),
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-cluster"                       % akkaVersion,
    "com.typesafe.akka" %% "akka-http-experimental"             % akkaVersion,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental"  % akkaVersion,
    "org.typelevel"     %% "cats"                               % "0.7.2"
    // "io.kamon" % "sigar-loader" % "1.6.6-rev002"
  )
)

lazy val persistence = project.in(file("./persistence"))
  .settings(commonSettings: _*)
  .settings(name := "persistence",
            libraryDependencies ++= Seq(
              "org.json4s" %% "json4s-ext" % "3.2.11",
              "org.json4s" %% "json4s-jackson" % "3.2.11",
              "joda-time"  % "joda-time" % "2.7",
              "org.slf4j"  % "slf4j-log4j12" % "1.7.21",
              "de.heikoseeberger" %% "akka-http-json4s" % "1.4.1",
              "org.reactivemongo" %% "reactivemongo" % "0.12.0"
            )
  )

lazy val http = project.in( file("./http"))
  .settings(commonSettings: _*)
  .settings(
    name := "http",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor"   % akkaVersion,
      "org.json4s" %% "json4s-ext" % "3.2.11",
      "org.json4s" %% "json4s-jackson" % "3.2.11",
      "joda-time"  % "joda-time" % "2.7",
      "org.slf4j"  % "slf4j-log4j12" % "1.7.21",
      "de.heikoseeberger" %% "akka-http-json4s" % "1.4.1"
    )
  ).dependsOn(persistence)

lazy val root =
  project.in( file("."))
    .aggregate(http)
    .settings(name := "akka-cluster-example")