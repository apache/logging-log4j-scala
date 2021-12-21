# [Apache Log4j 2 Scala API](http://logging.apache.org/log4j/2.x/)

Log4j Scala API is a Scala logging facade based on Log4j 2. This supports
Scala 2.10, and 2.11 on Java 7 at minimum, and Scala 2.12 on Java 8 at minimum. 
Log4j Scala API provides Log4j 2 as its default logging implementation, but this is not strictly
required (e.g., this API can also be used with Logback or other Log4j 2 API
provider implementations). Idiomatic Scala features are provided as an
alternative to using the Log4j 2 Java API.

Note that this Scala API was versioned and released in sync with Log4j up until version 2.8.2, but from now on it will 
be versioned and released independently, that's why the version was bumped to 11.0. 

[![Jenkins Status](https://builds.apache.org/job/Log4jScala/job/master/badge/icon)](https://builds.apache.org/job/Log4jScala)
[![Travis Status](https://travis-ci.org/apache/logging-log4j-scala.svg?branch=master)](https://travis-ci.org/apache/logging-log4j-scala)

## Usage

SBT users can add the following dependencies to their `build.sbt` file:

```scala
libraryDependencies ++= Seq(
  "org.apache.logging.log4j" %% "log4j-api-scala" % "11.0",
  "org.apache.logging.log4j" % "log4j-api" % "2.17.0",
  "org.apache.logging.log4j" % "log4j-core" % "2.17.0" % Runtime
)
```

Basic usage of the API:

```scala
import org.apache.logging.log4j.scala.Logging
import org.apache.logging.log4j.Level
 
class MyClass extends BaseClass with Logging {
  def doStuff(): Unit = {
    logger.info("Doing stuff")
  }
  def doStuffWithLevel(level: Level): Unit = {
    logger(level, "Doing stuff with arbitrary level")
  }
  def doStuffWithUser(user: User): Unit = {
    logger.info(s"Doing stuff with ${user.getName}.")
  }
}
```

## Documentation

The Log4j Scala API is documented in more detail [in the Log4j 2 manual](https://logging.apache.org/log4j/2.x/manual/scala-api.html)
and in the [ScalaDocs](https://logging.apache.org/log4j/2.x/log4j-api-scala_2.11/scaladocs/index.html#org.apache.logging.log4j.scala.package).

## Requirements

Log4j Scala API requires at least Java 7 for Scala 2.10/2.11, or Java 8 for
Scala 2.12. This also requires Log4j 2 API, but it is specified as transitive dependencies automatically 
if you are using SBT, Maven, Gradle, or some other similar build system. This also requires Log4j 2 Core 
(or possibly an other implementation of Log4j 2 API) as a runtime dependency. Some Log4j 2 Core features require optional
dependencies which are documented in the [Log4j 2 manual](https://logging.apache.org/log4j/2.x/manual/index.html).

## License

Apache Log4j 2 is distributed under the [Apache License, version 2.0](https://www.apache.org/licenses/LICENSE-2.0.html).

## Download

[How to download Log4j](https://logging.apache.org/log4j/2.x/download.html),
and [how to use it from SBT, Maven, Ivy and Gradle](https://logging.apache.org/log4j/2.x/maven-artifacts.html#Scala_API).
You can access the latest development snapshot by using the Maven repository `https://repository.apache.org/snapshots`, 
see [Snapshot builds](https://logging.apache.org/log4j/2.x/maven-artifacts.html#Snapshot_builds).

## Issue Tracking

Issues, bugs, and feature requests should be submitted to the 
[JIRA issue tracking system for this project](https://issues.apache.org/jira/browse/LOG4J2).

Pull request on GitHub are welcome, but please open a ticket in the JIRA issue tracker first, and mention the 
JIRA issue in the Pull Request.

## Building From Source

Log4j Scala API requires Java 8 to build. To install to your local
Maven repository, execute the following:

```sh
./sbt "+ publishM2"
```

## Contributing

We love contributions! Take a look at [our contributing page](https://github.com/apache/logging-log4j-scala/blob/master/src/asciidoctor/contributing.adoc).
