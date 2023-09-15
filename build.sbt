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
    homepage = Some(url("https://github.com/apache/logging-log4j-scala")),
    startYear = Some(2016),
    licenses = Vector("Apache License, Version 2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.txt")),
    organizationName = "Apache Software Foundation",
    organizationHomepage = Some(url("https://www.apache.org/")),
    scmInfo = Some(ScmInfo(
      url("https://github.com/apache/logging-log4j-scala.git"),
      "scm:git:git@github.com:apache/logging-log4j-scala.git",
      "scm:git:git@github.com:apache/logging-log4j-scala.git"
    )),
    developers = Vector(
      Developer("mikes", "Mikael Ståldal", "mikes@apache.org", null),
      Developer("mattsicker", "Matt Sicker", "mattsicker@apache.org", null),
      Developer("ggregory", "Gary Gregory", "ggregory@apache.org", null),
      Developer("vy", "Volkan Yazıcı", "vy@apache.org", null)
    )
  ),
  pomExtra := {
    <parent>
      <groupId>org.apache.logging</groupId>
      <artifactId>logging-parent</artifactId>
      <version>10.0.0</version>
    </parent>
  }
)

lazy val compileSettings = Seq(
  scalacOptions := Seq("-feature", "-unchecked", "-deprecation"),
  scalaVersion := scala213,
  crossScalaVersions := Seq(scala210, scala211, scala212, scala213, scala3)
)

lazy val licensePackagingSettings =
  for (task <- Seq(packageBin, packageSrc, packageDoc)) yield
    Compile / task / mappings ++= Seq(
      (baseDirectory.value / "LICENSE.txt", "META-INF/LICENSE"),
      (baseDirectory.value / "NOTICE.txt", "META-INF/NOTICE")
    )

lazy val sourceSettings = Seq(
    Compile / unmanagedSourceDirectories ++= {
        (Compile / unmanagedSourceDirectories).value.flatMap { dir =>
          CrossVersion.partialVersion(scalaVersion.value) match {
            case Some((3, _)) => Seq(file(dir.getPath ++ "-3"))
            case Some((2, n11)) if n11 >= 11 => Seq(file(dir.getPath ++ "-2"), file(dir.getPath ++ "-2.11+"))
            case Some((2, n10)) if n10 <= 10 => Seq(file(dir.getPath ++ "-2"), file(dir.getPath ++ "-2.10"))
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
        case _ => sys.error(s"unexpected `scalaVersion.value`: ${scalaVersion.value}")
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
  .enablePlugins(SbtOsgi)
  .settings(metadataSettings: _*)
  .settings(compileSettings: _*)
  .settings(licensePackagingSettings: _*)
  .settings(sourceSettings: _*)
  .settings(testSourceSettings: _*)
  .settings(apiDependencies: _*)
  .settings(bundleSettings: _*)

lazy val sample = (project in file("sample"))
  .settings(metadataSettings: _*)
  .settings(compileSettings: _*)
  .settings(
    name := "log4j-api-scala-sample",
    libraryDependencies := Seq(log4jApi, log4jCore)
  )
  .dependsOn(root)
