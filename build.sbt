import Dependencies._

name := "Log4j Scala API"
organization := "org.apache.logging.log4j"
//organizationName := "Apache Software Foundation"
//organizationHomepage := Some(url("https://www.apache.org/"))
homepage := Some(url("https://logging.apache.org/log4j/scala/"))
//licenses := Seq("Apache License, Version 2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.txt"))
scmInfo := Some(ScmInfo(
  url("https://git-wip-us.apache.org/repos/asf?p=logging-log4j-scala.git;a=summary"),
  "scm:git:http://git-wip-us.apache.org/repos/asf/logging-log4j-scala.git",
  "scm:git:https://git-wip-us.apache.org/repos/asf/logging-log4j-scala.git"
))

lazy val commonSettings = Seq(
  scalacOptions := Seq("-feature", "-unchecked", "-deprecation")
)

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  publishArtifact in Test := false,
  publishTo := {
    if (version.value.endsWith("-SNAPSHOT")) {
      Some("Apache Snapshots" at "https://repository.apache.org/content/repositories/snapshots")
    } else {
      Some("Apache Releases" at "https://repository.apache.org/service/local/staging/deploy/maven2")
    }
  }
)

lazy val api = (project in file("api"))
  .settings(commonSettings: _*)
  .settings(
    name := "log4j-api-scala",
    scalaVersion := scala211,
    crossScalaVersions := Seq(scala210, scala211, scala212, scala213),
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "org.apache.logging.log4j" % "log4j-api" % log4j,
      "org.apache.logging.log4j" % "log4j-api" % log4j % Test classifier "tests",
      "junit" % "junit" % junit % Test,
      "org.scalatest" %% "scalatest" % scalatest % Test,
      "org.mockito" % "mockito-core" % mockito % Test
    ),
    excludeFilter in Compile := new FileFilter {
      // override LoggerMacro for Scala 2.10
      override def accept(pathname: File): Boolean = {
        val Some((_, minor)) = CrossVersion.partialVersion(scalaVersion.value)
        minor == 10 && pathname.getName == "LoggerMacro.scala" && pathname.getParent.endsWith("src/main/scala/org/apache/logging/log4j/scala")
      }
    }
  )

//lazy val sample = (project in file("sample"))
//  .settings(commonSettings)
//  .settings(
//    name := "log4j-api-scala-sample",
//    scalaVersion := scala212,
//    libraryDependencies := Seq(
//      "org.apache.logging.log4j" % "log4j-api" % log4j,
//      "org.apache.logging.log4j" % "log4j-core" % log4j % Runtime
//    )
//  )
//  .dependsOn(api)

pomExtra := {
  <parent>
    <groupId>org.apache.logging</groupId>
    <artifactId>logging-parent</artifactId>
    <version>2</version>
  </parent>
    <developers>
      <developer>
        <id>mikes</id>
        <name>Mikael Ståldal</name>
      </developer>
      <developer>
        <id>mattsicker</id>
        <name>Matt Sicker</name>
      </developer>
      <developer>
        <id>ggregory</id>
        <name>Gary Gregory</name>
      </developer>
    </developers>
}
