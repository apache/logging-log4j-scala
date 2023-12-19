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

import org.apache.logging.log4j.message.*
import org.apache.logging.log4j.spi.{AbstractLogger, ExtendedLogger}
import org.apache.logging.log4j.{Level, Marker, MarkerManager}
import org.apache.logging.log4j.message.MapMessage
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.{any, anyString, eq as eqv}
import org.mockito.Mockito.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.junit.JUnitRunner
import org.scalatestplus.mockito.MockitoSugar
import scala.jdk.CollectionConverters._

case class Custom(i: Int)

trait Manager {
  def fetchValue(): Int
}

case class TestFixture(mockLogger: ExtendedLogger, manager: Manager)

@RunWith(classOf[JUnitRunner])
class LoggerTest extends AnyFunSuite with MockitoSugar {

  val msg                    = new ParameterizedMessage("msg {}", 17)
  val entryMsg: EntryMessage = new DefaultFlowMessageFactory().newEntryMessage(msg)
  val cseqMsg : CharSequence = new StringBuilder().append("cseq msg")
  val objectMsg              = Custom(17)
  val mapMessage             = MapMessage(Map("foo" -> "bar").asJava)
  val cause                  = new RuntimeException("cause")
  val marker  : Marker       = MarkerManager.getMarker("marker")
  val result                 = "foo"

  def fixture: TestFixture = {
    val mockLogger = {
      val mockLogger = mock[ExtendedLogger]
      when(mockLogger.getMessageFactory).thenReturn(new ParameterizedMessageFactory)
      mockLogger
    }
    val manager = {
      val mockManager = mock[Manager]
      when(mockManager.fetchValue()).thenReturn(4711)
      mockManager
    }
    TestFixture(mockLogger, manager)
  }

  test("fatal enabled with String message") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.FATAL)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger.fatal(s"string msg with value: ${f.manager.fetchValue()}")
    verify(f.mockLogger).log(eqv(Level.FATAL), any[String], any[String])
    verify(f.manager).fetchValue()
  }

  test("fatal disabled with String message") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.FATAL)).thenReturn(false)
    val logger = Logger(f.mockLogger)
    logger.fatal(s"string msg with value: ${f.manager.fetchValue()}")
    verify(f.mockLogger, never).log(any[Level], any[String], any[String])
    verify(f.manager, never).fetchValue()
  }

  test("error enabled with String message") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.ERROR)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger.error(s"string msg with value: ${f.manager.fetchValue()}")
    verify(f.mockLogger).log(eqv(Level.ERROR), any[String], any[String])
    verify(f.manager).fetchValue()
  }

  test("error disabled with String message") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.ERROR)).thenReturn(false)
    val logger = Logger(f.mockLogger)
    logger.error(s"string msg with value: ${f.manager.fetchValue()}")
    verify(f.mockLogger, never).log(any[Level], any[String], any[String])
    verify(f.manager, never).fetchValue()
  }

  test("warn enabled with String message") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.WARN)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger.warn(s"string msg with value: ${f.manager.fetchValue()}")
    verify(f.mockLogger).log(eqv(Level.WARN), any[String], any[String])
    verify(f.manager).fetchValue()
  }

  test("warn disabled with String message") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.WARN)).thenReturn(false)
    val logger = Logger(f.mockLogger)
    logger.warn(s"string msg with value: ${f.manager.fetchValue()}")
    verify(f.mockLogger, never).log(any[Level], any[String], any[String])
    verify(f.manager, never).fetchValue()
  }

  test("info enabled with String message") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger.info(s"string msg with value: ${f.manager.fetchValue()}")
    verify(f.mockLogger).log(eqv(Level.INFO), any[String], any[String])
    verify(f.manager).fetchValue()
  }

  test("info disabled with String message") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO)).thenReturn(false)
    val logger = Logger(f.mockLogger)
    logger.info(s"string msg with value: ${f.manager.fetchValue()}")
    verify(f.mockLogger, never).log(any[Level], any[String], any[String])
    verify(f.manager, never).fetchValue()
  }

  test("debug enabled with String message") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.DEBUG)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger.debug(s"string msg with value: ${f.manager.fetchValue()}")
    verify(f.mockLogger).log(eqv(Level.DEBUG), any[String], any[String])
    verify(f.manager).fetchValue()
  }

  test("debug disabled with String message") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.DEBUG)).thenReturn(false)
    val logger = Logger(f.mockLogger)
    logger.debug(s"string msg with value: ${f.manager.fetchValue()}")
    verify(f.mockLogger, never).log(any[Level], any[String], any[String])
    verify(f.manager, never).fetchValue()
  }

  test("trace enabled with String message") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.TRACE)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger.trace(s"string msg with value: ${f.manager.fetchValue()}")
    verify(f.mockLogger).log(eqv(Level.TRACE), any[String], any[String])
    verify(f.manager).fetchValue()
  }

  test("trace disabled with String message") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.TRACE)).thenReturn(false)
    val logger = Logger(f.mockLogger)
    logger.trace(s"string msg with value: ${f.manager.fetchValue()}")
    verify(f.mockLogger, never).log(any[Level], any[String], any[String])
    verify(f.manager, never).fetchValue()
  }


  test("log enabled with Message message and Marker") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO, marker)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, marker, msg)
    verify(f.mockLogger).log(eqv(Level.INFO), eqv(marker), eqv(msg))
  }

  test("log disabled with Message message and Marker") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO, marker)).thenReturn(false)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, marker, msg)
    verify(f.mockLogger, never).log(any[Level], any[Marker], any[Message])
  }

  test("log enabled with String message and Marker") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO, marker)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, marker, s"string msg with value: ${f.manager.fetchValue()}")
    verify(f.mockLogger).log(eqv(Level.INFO), eqv(marker), any[String], eqv(4711))
    verify(f.manager).fetchValue()
  }

  test("log disabled with String message and Marker") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO, marker)).thenReturn(false)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, marker, s"string msg with value: ${f.manager.fetchValue()}")
    verify(f.mockLogger, never).log(any[Level], any[Marker], any[String], any[Object])
    verify(f.manager, never).fetchValue()
  }

  test("log enabled with CharSequence message and Marker") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO, marker)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, marker, cseqMsg)
    verify(f.mockLogger).logMessage(eqv(Logger.FQCN), eqv(Level.INFO), eqv(marker), any[Message], eqv(null))
  }

  test("log disabled with CharSequence message and Marker") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO, marker)).thenReturn(false)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, marker, cseqMsg)
    verify(f.mockLogger, never).logMessage(any[String], any[Level], any[Marker], any[Message], any[Throwable])
  }

  test("log enabled with Object message and Marker") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO, marker)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, marker, objectMsg)
    verify(f.mockLogger).log(eqv(Level.INFO), eqv(marker), eqv(objectMsg))
  }

  test("log disabled with Object message and Marker") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO, marker)).thenReturn(false)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, marker, objectMsg)
    verify(f.mockLogger, never).log(any[Level], any[Marker], any[Object], any[Throwable])
  }

  test("log enabled with Message message and cause and Marker") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO, marker)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, marker, msg, cause)
    verify(f.mockLogger).log(eqv(Level.INFO), eqv(marker), eqv(msg), eqv(cause))
  }

  test("log disabled with Message message and cause and Marker") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO, marker)).thenReturn(false)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, marker, msg, cause)
    verify(f.mockLogger, never).log(any[Level], any[Marker], any[Message], any[Throwable])
  }

  test("log enabled with String message and cause and Marker") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO, marker)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, marker, s"string msg with value: ${f.manager.fetchValue()}", cause)
    verify(f.mockLogger).log(eqv(Level.INFO), eqv(marker), any[String], eqv(4711), eqv(cause))
    verify(f.manager).fetchValue()
  }

  test("log disabled with String message and cause and Marker") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO, marker)).thenReturn(false)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, marker, s"string msg with value: ${f.manager.fetchValue()}", cause)
    verify(f.mockLogger, never).log(any[Level], any[Marker], any[String], any[Object], any[Throwable])
    verify(f.manager, never).fetchValue()
  }

  test("log enabled with CharSequence message and cause and Marker") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO, marker)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, marker, cseqMsg, cause)
    verify(f.mockLogger).logMessage(eqv(Logger.FQCN), eqv(Level.INFO), eqv(marker), any[Message], eqv(cause))
  }

  test("log disabled with CharSequence message and cause and Marker") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO, marker)).thenReturn(false)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, marker, cseqMsg, cause)
    verify(f.mockLogger, never).logMessage(eqv(Logger.FQCN), any[Level], any[Marker], any[Message], any[Throwable])
  }

  test("log enabled with Object message and cause and Marker") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO, marker)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, marker, objectMsg, cause)
    verify(f.mockLogger).log(eqv(Level.INFO), eqv(marker), eqv(objectMsg), eqv(cause))
  }

  test("log disabled with Object message and cause and Marker") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO, marker)).thenReturn(false)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, marker, objectMsg, cause)
    verify(f.mockLogger, never).log(any[Level], any[Marker], any[Object], any[Throwable])
  }

  test("log enabled with Message message") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, msg)
    verify(f.mockLogger).log(eqv(Level.INFO), eqv(msg))
  }

  test("log disabled with Message message") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO)).thenReturn(false)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, msg)
    verify(f.mockLogger, never).log(any[Level], any[Message])
  }

  test("log enabled with String message") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, s"string msg with value: ${f.manager.fetchValue()}")
    verify(f.mockLogger).log(eqv(Level.INFO), any[String], eqv(4711))
    verify(f.manager).fetchValue()
  }

  test("log disabled with String message") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO)).thenReturn(false)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, s"string msg with value: ${f.manager.fetchValue()}")
    verify(f.mockLogger, never).log(any[Level], any[String], any[Object])
    verify(f.manager, never).fetchValue()
  }

  test("log enabled with CharSequence message") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, cseqMsg)
    verify(f.mockLogger).logMessage(eqv(Logger.FQCN), eqv(Level.INFO), eqv(null), any[Message], eqv(null))
  }

  test("log disabled with CharSequence message") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO)).thenReturn(false)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, cseqMsg)
    verify(f.mockLogger, never).logMessage(eqv(Logger.FQCN), any[Level], any[Marker], any[Message], any[Throwable])
  }

  test("log enabled with Object message") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, objectMsg)
    verify(f.mockLogger).log(eqv(Level.INFO), any[Object])
  }

  test("log disabled with Object message") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO)).thenReturn(false)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, objectMsg)
    verify(f.mockLogger, never).log(any[Level], any[Object], any[Throwable])
  }

  test("log enabled with Message message and cause") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, msg, cause)
    verify(f.mockLogger).log(eqv(Level.INFO), eqv(msg), eqv(cause))
  }

  test("log disabled with Message message and cause") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO)).thenReturn(false)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, msg, cause)
    verify(f.mockLogger, never).log(any[Level], any[Marker], any[Message], any[Throwable])
  }

  test("log enabled with String message and cause") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, s"string msg with value: ${f.manager.fetchValue()}", cause)
    verify(f.mockLogger).log(eqv(Level.INFO), any[String], eqv(4711), eqv(cause))
    verify(f.manager).fetchValue()
  }

  test("log disabled with String message and cause") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO)).thenReturn(false)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, s"string msg with value: ${f.manager.fetchValue()}", cause)
    verify(f.mockLogger, never).log(any[Level], any[String], any[Object], any[Throwable])
    verify(f.manager, never).fetchValue()
  }

  test("log enabled with CharSequence message and cause") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, cseqMsg, cause)
    verify(f.mockLogger).logMessage(eqv(Logger.FQCN), eqv(Level.INFO), eqv(null), any[Message], eqv(cause))
  }

  test("log disabled with CharSequence message and cause") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO)).thenReturn(false)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, cseqMsg, cause)
    verify(f.mockLogger, never).logMessage(eqv(Logger.FQCN), any[Level], any[Marker], any[Message], any[Throwable])
  }

  test("log enabled with Object message and cause") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, objectMsg, cause)
    verify(f.mockLogger).log(eqv(Level.INFO), eqv(objectMsg), eqv(cause))
  }

  test("log disabled with Object message and cause") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO)).thenReturn(false)
    val logger = Logger(f.mockLogger)
    logger(Level.INFO, objectMsg, cause)
    verify(f.mockLogger, never).log(any[Level], any[Object], any[Throwable])
  }


  test("traceEntry") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.TRACE, AbstractLogger.ENTRY_MARKER, null.asInstanceOf[AnyRef], null)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger.traceEntry()
    verify(f.mockLogger).traceEntry()
  }

  test("traceEntry enabled with params") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.TRACE, AbstractLogger.ENTRY_MARKER, null.asInstanceOf[AnyRef], null)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger.traceEntry("foo", "bar")
    verify(f.mockLogger).traceEntry("foo", "bar")
  }

  test("traceEntry disabled with params") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.TRACE, AbstractLogger.ENTRY_MARKER, null.asInstanceOf[AnyRef], null)).thenReturn(false)
    val logger = Logger(f.mockLogger)
    logger.traceEntry("foo", "bar")
    //traceEntry is now passes through to the underlying logger without checking if logging is enabled (the delegate checks anyway)
    verify(f.mockLogger).traceEntry("foo", "bar")
  }

  test("traceEntry enabled with message") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.TRACE, AbstractLogger.ENTRY_MARKER, null.asInstanceOf[AnyRef], null)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger.traceEntry(msg)
    verify(f.mockLogger).traceEntry(eqv(msg))
  }

  test("traceEntry disabled with message") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.TRACE, AbstractLogger.ENTRY_MARKER, null.asInstanceOf[AnyRef], null)).thenReturn(false)
    val logger = Logger(f.mockLogger)
    logger.traceEntry(msg)
    //traceEntry is now passes through to the underlying logger without checking if logging is enabled (the delegate checks anyway)
    verify(f.mockLogger).traceEntry(eqv(msg))
  }

  test("traceExit") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.TRACE, AbstractLogger.EXIT_MARKER, msg, null)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger.traceExit()
    verify(f.mockLogger).traceExit()
  }

  test("traceExit with result") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.TRACE, AbstractLogger.EXIT_MARKER, msg, null)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger.traceExit(result)
    verify(f.mockLogger).traceExit(result)
  }

  test("traceExit with entrymessage") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.TRACE, AbstractLogger.EXIT_MARKER, msg, null)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger.traceExit(entryMsg)
    verify(f.mockLogger).traceExit(entryMsg)
  }

  test("traceExit with entrymessage and result") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.TRACE, AbstractLogger.EXIT_MARKER, msg, null)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger.traceExit(entryMsg, result)
    verify(f.mockLogger).traceExit(entryMsg, result)
  }

  test("traceExit enabled with message") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.TRACE, AbstractLogger.EXIT_MARKER, msg, null)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger.traceExit(msg, result)
    verify(f.mockLogger).traceExit(eqv(msg), eqv(result))
  }

  test("traceExit disabled with message") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.TRACE, AbstractLogger.EXIT_MARKER, msg, null)).thenReturn(false)
    val logger = Logger(f.mockLogger)
    logger.traceExit(msg, result)
    //traceExit is now passes through to the underlying logger without checking if logging is enabled (the delegate checks anyway)
    verify(f.mockLogger).traceExit(eqv(msg), eqv(result))
  }

  test("throwing") {
    val f = fixture
    val logger = Logger(f.mockLogger)
    logger.throwing(cause)
    verify(f.mockLogger).throwing(eqv(cause))
  }

  test("throwing with level") {
    val f = fixture
    val logger = Logger(f.mockLogger)
    logger.throwing(Level.INFO, cause)
    verify(f.mockLogger).throwing(eqv(Level.INFO), eqv(cause))
  }

  test("catching") {
    val f = fixture
    val logger = Logger(f.mockLogger)
    logger.catching(cause)
    verify(f.mockLogger).catching(eqv(cause))
  }

  test("catching with level") {
    val f = fixture
    val logger = Logger(f.mockLogger)
    logger.catching(Level.INFO, cause)
    verify(f.mockLogger).catching(eqv(Level.INFO), eqv(cause))
  }

  test("log enabled with Map message") {
    val f = fixture
    when(f.mockLogger.isEnabled(Level.INFO)).thenReturn(true)
    val logger = Logger(f.mockLogger)
    logger.info(mapMessage)
    verify(f.mockLogger).log(eqv(Level.INFO), eqv(mapMessage))
  }
}
