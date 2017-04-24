<!---
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
# Building Log4j 2 Scala API
  
To build Log4j 2 Scala API, you need a JDK implementation version 1.8 or greater and Apache Maven.
Note that building the site requires Maven 3.0.5, while everything else works fine with any version of Maven 3.

To perform the license release audit, a.k.a. "RAT check", run.

    mvn apache-rat:check

To install the jars in your local Maven repository, from a command line, run:

    mvn clean install

Next, to build the site:

    mvn site

On Windows, use a local staging directory, for example:
