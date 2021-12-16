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

import org.apache.logging.log4j.message.{DefaultFlowMessageFactory, EntryMessage, Message, ParameterizedMessage, ParameterizedMessageFactory}
import org.apache.logging.log4j.spi.{AbstractLogger, ExtendedLogger}
import org.apache.logging.log4j.{Level, Marker, MarkerManager}
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.{any, anyString, eq => eqv}
import org.mockito.Mockito._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.junit.JUnitRunner
import org.scalatestplus.mockito.MockitoSugar

@RunWith(classOf[JUnitRunner])
class TempLoggerTest extends AnyFunSuite with MockitoSugar {

  val msg                    = new ParameterizedMessage("msg {}", 17)
  val entryMsg: EntryMessage = new DefaultFlowMessageFactory().newEntryMessage(msg)
  val cseqMsg : CharSequence = new StringBuilder().append("cseq msg")
  val objectMsg              = Custom(17)
  val cause                  = new RuntimeException("cause")
  val marker  : Marker       = MarkerManager.getMarker("marker")
  val result                 = "foo"

  test("info enabled with String message") {
    val logger = Logger(classOf[TempLoggerTest])
    logger.info(s"string msg with value: ${1 + 1}")
  }

  test("trace enabled with String message") {
    val logger = Logger(classOf[TempLoggerTest])
    logger.trace(s"string msg with value: ${1 + 1}")
  }

  def failedEval(): Int = {
    throw new RuntimeException("eval failed")
  }

}
