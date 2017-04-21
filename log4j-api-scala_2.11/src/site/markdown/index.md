<!--
    Licensed to the Apache Software Foundation (ASF) under one or more
    contributor license agreements.  See the NOTICE file distributed with
    this work for additional information regarding copyright ownership.
    The ASF licenses this file to You under the Apache License, Version 2.0
    (the "License"); you may not use this file except in compliance with
    the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
-->
# Log4j Scala 2.11 API

## Requirements

Log4j Scala API for Scala 2.11 requires Log4j API, Java 7, the Scala runtime
library, and the Scala reflection library. To use Log4j as a logging implementation,
then Log4j Core must also be included. Instructions on using these dependencies are
in the [dependency information page][dependencies], and additional information is
included in the [Log4j artifacts][artifacts] page.

## Example Usage

<pre class="prettyprint linenums">
import org.apache.logging.log4j.scala.Logging
import org.apache.logging.log4j.Level

class MyClass extends BaseClass with Logging {
  def doStuff(): Unit = {
    logger.info("Doing stuff")
  }
  def doStuffWithLevel(level: Level): Unit = {
    logger(level, "Doing stuff with arbitrary level")
  }
}
</pre>

## Configuration

Log4j Scala 2.11 API uses the standard [Log4j configuration][configuration]
plugins. This supports XML, properties files, and a configuration builder DSL by
default, and it optionally supports JSON and YAML formats with some additional
Jackson dependencies.

## Substituting Parameters

In the Java API, parameters to a log message are interpolated using various
strategies. In the Scala API, [string interpolation][interpolation] is used
instead. As all logger methods are implemented as macros, string construction and
method invocations will only occur when logging is enabled for the given log
level. For example:

<pre class="prettyprint linenums">
logger.debug(s"Logging in user ${user.getName} with birthday ${user.calcBirthday}")
</pre>

## Logger Names

Most logging implementations use a hierarchical scheme for matching logger names
with logging configuration. In this scheme the logger name hierarchy is
represented by '.' characters in the logger name, in a fashion very similar to
the hierarchy used for Java/Scala package names. The [`Logging` trait][logging]
will automatically name the Logger accordingly to the class it is being used in.

[dependencies]: dependency-info.html
[artifacts]: ../maven-artifacts.html
[configuration]: ../configuration.html
[interpolation]: http://docs.scala-lang.org/overviews/core/string-interpolation.html
[logging]: scaladocs/index.html#org.apache.logging.log4j.scala.Logging