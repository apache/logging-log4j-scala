import Dependencies._

lazy val metadataSettings = Seq(
  organization := "org.apache.logging.log4j",
  organizationName := "Apache Software Foundation",
  organizationHomepage := Some(url("https://www.apache.org/")),
  homepage := Some(url("https://logging.apache.org/log4j/scala/")),
  licenses := Seq("Apache License, Version 2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.txt")),
  scmInfo := Some(ScmInfo(
    url("https://git-wip-us.apache.org/repos/asf?p=logging-log4j-scala.git;a=summary"),
    "scm:git:http://git-wip-us.apache.org/repos/asf/logging-log4j-scala.git",
    "scm:git:https://git-wip-us.apache.org/repos/asf/logging-log4j-scala.git"
  )),
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
)

lazy val compileSettings = Seq(
  scalacOptions := Seq("-feature", "-unchecked", "-deprecation"),
  scalaVersion := scala211,
  crossScalaVersions := Seq(scala210, scala211, scala212, scala213)
)

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  publishArtifact in Test := false,
  publishTo := {
    if (isSnapshot.value) {
      Some("Apache Snapshots" at "https://repository.apache.org/content/repositories/snapshots")
    } else {
      Some("Apache Releases" at "https://repository.apache.org/service/local/staging/deploy/maven2")
    }
  },
  credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
//  managedResources
//  resourceGenerators in Compile += inlineTask(Seq(file("LICENSE.txt"), file("NOTICE.txt")))
)

lazy val releaseSettings = Seq(
  releaseCrossBuild := true
)

lazy val apiDependencies = Seq(
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    "org.apache.logging.log4j" % "log4j-api" % log4j,
    "org.apache.logging.log4j" % "log4j-api" % log4j % Test classifier "tests",
    "junit" % "junit" % junit % Test,
    "org.scalatest" %% "scalatest" % scalatest % Test,
    "org.mockito" % "mockito-core" % mockito % Test
  )
)

lazy val apiInputFiles = Seq(
  sources in Compile := {
    val filteredFiles = {
      for ((_, minor) <- CrossVersion.partialVersion(scalaVersion.value) if minor == 10)
        yield ((baseDirectory.value / "src" / "main" / "scala") ** "*Macro.scala").get
    }.getOrElse(Seq())
    (sources in Compile).value.filterNot(filteredFiles.contains)
  }
)

lazy val root = (project in file("."))
  .settings(name := "log4j-api-scala")
  .settings(metadataSettings: _*)
  .settings(compileSettings: _*)
  .settings(publishSettings: _*)
  .settings(releaseSettings: _*)
  .settings(apiDependencies: _*)
  .settings(apiInputFiles: _*)

//lazy val nopublish = Seq(
//  publish := {},
//  publishLocal := {},
//  publishM2 := {},
//  skip in publish := true
//)

//lazy val sample = (project in file("sample"))
//  .settings(metadataSettings: _*)
//  .settings(nopublish: _*)
//  .settings(
//    name := "log4j-api-scala-sample",
//    scalaVersion := scala212,
//    libraryDependencies := Seq(
//      "org.apache.logging.log4j" % "log4j-api" % log4j,
//      "org.apache.logging.log4j" % "log4j-core" % log4j % Runtime
//    )
//  )
//  .dependsOn(root)

