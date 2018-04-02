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
import sbt._

object Dependencies {
  val scala210 = "2.10.7"
  val scala211 = "2.11.12"
  val scala212 = "2.12.4"
  val scala213 = "2.13.0-M2"

  def scalaReflect(version: String): ModuleID =
    "org.scala-lang" % "scala-reflect" % version

  val osgiCoreApi = "org.osgi" % "org.osgi.core" % "4.3.0" % Provided
  private val log4jV = "2.11.0"
  val log4jApi = "org.apache.logging.log4j" % "log4j-api" % log4jV
  val log4jApiTests = "org.apache.logging.log4j" % "log4j-api" % log4jV % Test classifier "tests"
  val log4jCore = "org.apache.logging.log4j" % "log4j-core" % log4jV % Runtime
  val junit = "junit" % "junit" % "4.12" % Test
  val scalatest = "org.scalatest" %% "scalatest" % "3.0.4" % Test
  val mockito = "org.mockito" % "mockito-core" % "1.10.19" % Test
}
