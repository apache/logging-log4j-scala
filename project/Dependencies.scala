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
  val junit = "junit" % "junit" % "4.12" % Test
  val scalatest = "org.scalatest" %% "scalatest" % "3.0.4" % Test
  val mockito = "org.mockito" % "mockito-core" % "1.10.19" % Test
}
