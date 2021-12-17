/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */

import Dependencies._
import com.typesafe.sbt.osgi.SbtOsgi.autoImport.OsgiKeys._

lazy val metadataSettings = Seq(
  organization := "org.apache.logging.log4j",
  projectInfo := ModuleInfo(
    nameFormal = "Apache Log4j Scala API",
    description = "Scala logging API facade for Log4j",
    homepage = Some(url("https://logging.apache.org/log4j/scala/")),
    startYear = Some(2016),
    licenses = Vector("Apache License, Version 2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.txt")),
    organizationName = "Apache Software Foundation",
    organizationHomepage = Some(url("https://www.apache.org/")),
    scmInfo = Some(ScmInfo(
      url("https://gitbox.apache.org/repos/asf?p=logging-log4j-scala.git;a=summary"),
      "scm:git:https://gitbox.apache.org/repos/asf/logging-log4j-scala.git",
      "scm:git:https://gitbox.apache.org/repos/asf/logging-log4j-scala.git"
    )),
    developers = Vector(
      Developer("mikes", "Mikael Ståldal", "mikes@apache.org", null),
      Developer("mattsicker", "Matt Sicker", "mattsicker@apache.org", null),
      Developer("ggregory", "Gary Gregory", "ggregory@apache.org", null)
    )
  ),
  pomExtra := {
    <parent>
      <groupId>org.apache.logging</groupId>
      <artifactId>logging-parent</artifactId>
      <version>2</version>
    </parent>
  }
)

lazy val compileSettings = Seq(
  scalacOptions := Seq("-feature", "-unchecked", "-deprecation"),
  scalaVersion := scala3
  //crossScalaVersions := Seq(scala210, scala211, scala212, scala213, scala3)
)

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  Test / publishArtifact := false,
  publishTo := {
    if (isSnapshot.value) {
      Some("Apache Snapshots" at "https://repository.apache.org/content/repositories/snapshots")
    } else {
      Some("Apache Releases" at "https://repository.apache.org/service/local/staging/deploy/maven2")
    }
  },
  credentials ++= {
    for {
      username <- sys.env.get("NEXUS_USR")
      password <- sys.env.get("NEXUS_PSW")
    } yield Credentials("Sonatype Nexus Repository Manager", "repository.apache.org", username, password)
  }.toList,
  // FIXME: https://github.com/sbt/sbt/issues/3519
  updateOptions := updateOptions.value.withGigahorse(false)
)

lazy val licensePackagingSettings =
  for (task <- Seq(packageBin, packageSrc, packageDoc)) yield
    mappings in (Compile, task) ++= Seq(
      (baseDirectory.value / "LICENSE.txt", "META-INF/LICENSE"),
      (baseDirectory.value / "NOTICE.txt", "META-INF/NOTICE")
    )

lazy val sourceSettings = Seq(
    Compile / unmanagedSourceDirectories ++= {
        (Compile / unmanagedSourceDirectories).value.map { dir =>
          CrossVersion.partialVersion(scalaVersion.value) match {
            case Some((3, _)) => file(dir.getPath ++ "-3")
            case Some((2, n11)) if n11 >= 11 => file(dir.getPath ++ "-2.11+")
            case Some((2, n10)) if n10 <= 10 => file(dir.getPath ++ "-2.10")
          }
        }
    },
    Compile / unmanagedSourceDirectories ++= {
      (Compile / unmanagedSourceDirectories).value.map { dir =>
        CrossVersion.partialVersion(scalaVersion.value) match {
          case Some((3, _)) => file(dir.getPath ++ "-2.13+")
          case Some((2, n13)) if n13 >= 13 => file(dir.getPath ++ "-2.13+")
          case Some((2, n12)) if n12 <= 12 => file(dir.getPath ++ "-2.12-")
        }
      }
    }
  )

lazy val testSourceSettings = Seq(
  Test / unmanagedSourceDirectories ++= {
    (Test / unmanagedSourceDirectories).value.map { dir =>
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((3, _)) => file(dir.getPath ++ "-3")
        case Some((2, _)) => file(dir.getPath ++ "-2")
      }
    }
  },
  Compile / unmanagedSourceDirectories ++= {
    (Compile / unmanagedSourceDirectories).value.map { dir =>
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((3, _)) => file(dir.getPath ++ "-2.13+")
        case Some((2, n13)) if n13 >= 13 => file(dir.getPath ++ "-2.13+")
        case Some((2, n12)) if n12 <= 12 => file(dir.getPath ++ "-2.12-")
      }
    }
  }
)

lazy val releaseSettings = Seq(
  releaseCrossBuild := true,
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  releaseProcess := {
    import ReleaseTransformations._
    Seq(
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      releaseStepTask(Compile / auditCheck),
      runTest,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      releaseStepTask(packageSite),
      publishArtifacts,
      setNextVersion,
      commitNextVersion,
      pushChanges
    )
  }
)

lazy val siteSettings = Seq(
  apiURL := Some(url(s"https://logging.apache.org/log4j/log4j-scala-${version.value}/")),
  SiteScaladoc / siteSubdirName := s"api/${scalaBinaryVersion.value}",
  Asciidoc / managedSources += {
    (Compile / auditReport).value
    (Compile / target).value / "rat.adoc"
  },
  makeSite / mappings ++= Seq(
    (baseDirectory.value / "LICENSE.txt", "LICENSE"),
    (baseDirectory.value / "NOTICE.txt", "NOTICE")
  )
)

lazy val apiDependencies = Seq(
  libraryDependencies ++= scalaReflect(scalaVersion.value).toSeq ++ Seq(
    osgiCoreApi,
    log4jApi,
    log4jApiTests,
    junit,
    scalatest,
    scalatestFunsuit,
    scalatestMatcher,
    scalatestJunit,
    scalatestMockito,
    mockito
  )
)

lazy val bundleSettings = osgiSettings ++ Seq(
  bundleSymbolicName := "org.apache.logging.log4j.scala",
  exportPackage := Seq("org.apache.logging.log4j.scala")
)

lazy val root = (project in file("."))
  .settings(name := "log4j-api-scala")
  .enablePlugins(AsciidocPlugin, SiteScaladocPlugin, SbtOsgi, Distributions)
  .settings(metadataSettings: _*)
  .settings(compileSettings: _*)
  .settings(publishSettings: _*)
  .settings(licensePackagingSettings: _*)
  .settings(sourceSettings: _*)
  .settings(testSourceSettings: _*)
  .settings(releaseSettings: _*)
  .settings(siteSettings: _*)
  .settings(apiDependencies: _*)
  .settings(bundleSettings: _*)

lazy val nopublish = Seq(
  publish := {},
  publishLocal := {},
  publishM2 := {},
  publish / skip := true
)

lazy val sample = (project in file("sample"))
  .settings(metadataSettings: _*)
  .settings(compileSettings: _*)
  .settings(nopublish: _*)
  .settings(
    name := "log4j-api-scala-sample",
    libraryDependencies := Seq(log4jApi, log4jCore)
  )
  .dependsOn(root)

