# [Apache Log4j 2 Scala API](http://logging.apache.org/log4j/2.x/)

[![Build Status](https://builds.apache.org/buildStatus/icon?job=Log4jScala)](https://builds.apache.org/job/Log4jScala)

## Usage

SBT users can add the following dependencies to their `build.sbt` file:

```scala
libraryDependencies += "org.apache.logging.log4j" %% "log4j-api-scala" % "2.8"
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
Scala 2.12. This also requires Log4j 2 API and Log4j 2 Core, but these are
specified as transitive dependencies automatically if you are using SBT, Maven,
Gradle, or some other similar build system. Some Log4j 2 features require optional
dependencies which are documented in the [Log4j 2 manual](https://logging.apache.org/log4j/2.x/manual/index.html).

## License

Apache Log4j 2 is distributed under the [Apache License, version 2.0](https://www.apache.org/licenses/LICENSE-2.0.html).

## Download

[How to download Log4j](https://logging.apache.org/log4j/2.x/download.html),
and [how to use it from SBT, Maven, Ivy and Gradle](https://logging.apache.org/log4j/2.x/maven-artifacts.html).

## Issue Tracking

Issues, bugs, and feature requests should be submitted to the 
[JIRA issue tracking system for this project](https://issues.apache.org/jira/browse/LOG4J2).

Pull request on GitHub are welcome, but please open a ticket in the JIRA issue tracker first, and mention the 
JIRA issue in the Pull Request.

## Building From Source

Log4j Scala API requires Maven 3 and Java 8 to build. To install to your local
Maven repository, execute the following:

```sh
mvn install
```

## Contributing

We love contributions! Take a look at [our contributing page](https://github.com/apache/logging-log4j-scala/blob/master/CONTRIBUTING.md).
