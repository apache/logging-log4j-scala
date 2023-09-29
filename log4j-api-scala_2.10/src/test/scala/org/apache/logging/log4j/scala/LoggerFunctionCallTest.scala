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
package org.apache.logging.log4j.scala

import org.junit.runner.RunWith
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class LoggerFunctionCallTest extends AnyFunSuite with Matchers {

  val logger = Logger(classOf[LoggerFunctionCallTest])

  test("interpolated call is not made if log level is not enabled") {
    var logCount: Int = 0

    def incrementAndGetLogCount(): Int = {
      logCount += 1
      logCount
    }

    logger.debug(s"logCount = ${incrementAndGetLogCount()}")
    logCount shouldEqual 0
  }

  test("interpolated call is made if log level is enabled") {
    var logCount: Int = 0

    def incrementAndGetLogCount(): Int = {
      logCount += 1
      logCount
    }

    logger.error(s"logCount = ${incrementAndGetLogCount()}")
    logCount shouldEqual 1
  }

}
