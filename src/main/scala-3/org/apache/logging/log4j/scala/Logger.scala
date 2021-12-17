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

import org.apache.logging.log4j.message.{EntryMessage, Message, MessageFactory2}
import org.apache.logging.log4j.spi.ExtendedLogger
import org.apache.logging.log4j.{Level, LogManager, Marker}

import scala.quoted.*

/**
  * Factory for [[Logger]]s.
  *
  * The [[Logging]] trait provides a simple way to get a properly named logger into a class.
  */
object Logger {

  final val FQCN = getClass.getName

    /**
    * Create a properly named [[Logger]] for a given class.
    *
    * @param clazz the class
    */
  def apply(clazz: Class[_]): Logger = Logger(LogManager.getContext(clazz.getClassLoader, false).getLogger(clazz.getName))

  /**
    * Create a [[Logger]] wrapping the given Log4j logger.
    *
    * @param delegate the Log4j logger to wrap
    */
  def apply(delegate: ExtendedLogger): Logger = new Logger(delegate)

}

/**
  * Scala wrapper for the Log4j `Logger` interface.
  *
  * Frequently the purpose of logging is to provide information about what is happening in the system,
  * which requires including information about the objects being manipulated. In Scala, you can use
  * [[http://docs.scala-lang.org/overviews/core/string-interpolation.html string interpolation]]
  * to achieve this:
  *
  * {{{
  * logger.debug(s"Logging in user ${user.getName} with birthday ${user.calcBirthday}")
  * }}}
  *
  * Since this wrapper is implemented with macros, the String construction and method invocations
  * will only occur when debug logging is enabled.
  */
class Logger private(val delegate: ExtendedLogger) extends AnyVal {

  inline def fatal(inline marker: Marker, inline message: Message): Unit =
    ${LoggerMacro.fatalMarkerMsg('delegate, 'marker, 'message)}

  inline def fatal(inline marker: Marker, inline message: CharSequence): Unit =
    ${LoggerMacro.fatalMarkerCseq('delegate, 'marker, 'message)}

  inline def fatal(inline marker: Marker, inline message: AnyRef): Unit =
    ${LoggerMacro.fatalMarkerObject('delegate, 'marker, 'message)}

  inline def fatal(inline marker: Marker, inline message: Message, inline cause: Throwable): Unit =
    ${LoggerMacro.fatalMarkerMsgThrowable('delegate, 'marker, 'message, 'cause)}

  inline def fatal(inline marker: Marker, inline message: CharSequence, inline cause: Throwable): Unit =
    ${LoggerMacro.fatalMarkerCseqThrowable('delegate, 'marker, 'message, 'cause)}

  inline def fatal(inline marker: Marker, inline message: AnyRef, inline cause: Throwable): Unit =
    ${LoggerMacro.fatalMarkerObjectThrowable('delegate, 'marker, 'message, 'cause)}

  inline def fatal(inline message: Message): Unit =
    ${LoggerMacro.fatalMsg('delegate, 'message)}

  inline def fatal(inline message: CharSequence): Unit =
    ${LoggerMacro.fatalCseq('delegate, 'message)}

  inline def fatal(inline message: AnyRef): Unit =
    ${LoggerMacro.fatalObject('delegate, 'message)}

  inline def fatal(inline message: Message, inline cause: Throwable): Unit =
    ${LoggerMacro.fatalMsgThrowable('delegate, 'message, 'cause)}

  inline def fatal(inline message: CharSequence, inline cause: Throwable): Unit =
    ${LoggerMacro.fatalCseqThrowable('delegate, 'message, 'cause)}

  inline def fatal(inline message: AnyRef, inline cause: Throwable): Unit =
    ${LoggerMacro.fatalObjectThrowable('delegate, 'message, 'cause)}

  inline def error(inline marker: Marker, inline message: Message): Unit =
    ${LoggerMacro.errorMarkerMsg('delegate, 'marker, 'message)}

  inline def error(inline marker: Marker, inline message: CharSequence): Unit =
    ${LoggerMacro.errorMarkerCseq('delegate, 'marker, 'message)}

  inline def error(inline marker: Marker, inline message: AnyRef): Unit =
    ${LoggerMacro.errorMarkerObject('delegate, 'marker, 'message)}


  inline def error(inline marker: Marker, inline message: Message, inline cause: Throwable): Unit =
    ${LoggerMacro.errorMarkerMsgThrowable('delegate, 'marker, 'message, 'cause)}

  inline def error(inline marker: Marker, inline message: CharSequence, inline cause: Throwable): Unit =
    ${LoggerMacro.errorMarkerCseqThrowable('delegate, 'marker, 'message, 'cause)}

  inline def error(inline marker: Marker, inline message: AnyRef, inline cause: Throwable): Unit =
    ${LoggerMacro.errorMarkerObjectThrowable('delegate, 'marker, 'message, 'cause)}

  inline def error(inline message: Message): Unit =
    ${LoggerMacro.errorMsg('delegate, 'message)}

  inline def error(inline message: CharSequence): Unit =
    ${LoggerMacro.errorCseq('delegate, 'message)}

  inline def error(inline message: AnyRef): Unit =
    ${LoggerMacro.errorObject('delegate, 'message)}

  inline def error(inline message: Message, inline cause: Throwable): Unit =
    ${LoggerMacro.errorMsgThrowable('delegate, 'message, 'cause)}

  inline def error(inline message: CharSequence, inline cause: Throwable): Unit =
    ${LoggerMacro.errorCseqThrowable('delegate, 'message, 'cause)}

  inline def error(inline message: AnyRef, inline cause: Throwable): Unit =
    ${LoggerMacro.errorObjectThrowable('delegate, 'message, 'cause)}


  inline def warn(inline marker: Marker, inline message: Message): Unit =
    ${LoggerMacro.warnMarkerMsg('delegate, 'marker, 'message)}

  inline def warn(inline marker: Marker, inline message: CharSequence): Unit =
    ${LoggerMacro.warnMarkerCseq('delegate, 'marker, 'message)}

  inline def warn(inline marker: Marker, inline message: AnyRef): Unit =
    ${LoggerMacro.warnMarkerObject('delegate, 'marker, 'message)}

  inline def warn(inline marker: Marker, inline message: Message, inline cause: Throwable): Unit =
    ${LoggerMacro.warnMarkerMsgThrowable('delegate, 'marker, 'message, 'cause)}

  inline def warn(inline marker: Marker, inline message: CharSequence, inline cause: Throwable): Unit =
    ${LoggerMacro.warnMarkerCseqThrowable('delegate, 'marker, 'message, 'cause)}

  inline def warn(inline marker: Marker, inline message: AnyRef, inline cause: Throwable): Unit =
    ${LoggerMacro.warnMarkerObjectThrowable('delegate, 'marker, 'message, 'cause)}


  inline def warn(message: Message): Unit =
    ${LoggerMacro.warnMsg('delegate, 'message)}

  inline def warn(inline message: CharSequence): Unit =
    ${LoggerMacro.warnCseq('delegate, 'message)}

  inline def warn(inline message: AnyRef): Unit =
    ${LoggerMacro.warnObject('delegate, 'message)}

  inline def warn(inline message: Message, inline cause: Throwable): Unit =
    ${LoggerMacro.warnMsgThrowable('delegate, 'message, 'cause)}

  inline def warn(inline message: CharSequence, inline cause: Throwable): Unit =
    ${LoggerMacro.warnCseqThrowable('delegate, 'message, 'cause)}

  inline def warn(inline message: AnyRef, inline cause: Throwable): Unit =
    ${LoggerMacro.warnObjectThrowable('delegate, 'message, 'cause)}


  inline def info(inline marker: Marker, inline message: Message): Unit =
    ${LoggerMacro.infoMarkerMsg('delegate, 'marker, 'message)}

  inline def info(inline marker: Marker, inline message: CharSequence): Unit =
    ${LoggerMacro.infoMarkerCseq('delegate, 'marker, 'message)}

  inline def info(inline marker: Marker, inline message: AnyRef): Unit =
    ${LoggerMacro.infoMarkerObject('delegate, 'marker, 'message)}

  inline def info(inline marker: Marker, inline message: Message, inline cause: Throwable): Unit =
    ${LoggerMacro.infoMarkerMsgThrowable('delegate, 'marker, 'message, 'cause)}

  inline def info(inline marker: Marker, inline message: CharSequence, inline cause: Throwable): Unit =
    ${LoggerMacro.infoMarkerCseqThrowable('delegate, 'marker, 'message, 'cause)}

  inline def info(inline marker: Marker, inline message: AnyRef, inline cause: Throwable): Unit =
    ${LoggerMacro.infoMarkerObjectThrowable('delegate, 'marker, 'message, 'cause)}

  inline def info(inline message: Message): Unit =
    ${LoggerMacro.infoMsg('delegate, 'message)}

  inline def info(inline message: CharSequence): Unit =
    ${LoggerMacro.infoCseq('delegate, 'message)}

  inline def info(inline message: AnyRef): Unit =
    ${LoggerMacro.infoObject('delegate, 'message)}

  inline def info(inline message: Message, inline cause: Throwable): Unit =
    ${LoggerMacro.infoMsgThrowable('delegate, 'message, 'cause)}

  inline def info(inline message: CharSequence, inline cause: Throwable): Unit =
    ${LoggerMacro.infoCseqThrowable('delegate, 'message, 'cause)}

  inline def info(inline message: AnyRef, inline cause: Throwable): Unit =
    ${LoggerMacro.infoObjectThrowable('delegate, 'message, 'cause)}


  inline def debug(inline marker: Marker, inline message: Message): Unit =
    ${LoggerMacro.debugMarkerMsg('delegate, 'marker, 'message)}

  inline def debug(inline marker: Marker, inline message: CharSequence): Unit =
    ${LoggerMacro.debugMarkerCseq('delegate, 'marker, 'message)}

  inline def debug(inline marker: Marker, inline message: AnyRef): Unit =
    ${LoggerMacro.debugMarkerObject('delegate, 'marker, 'message)}

  inline def debug(inline marker: Marker, inline message: Message, inline cause: Throwable): Unit =
    ${LoggerMacro.debugMarkerMsgThrowable('delegate, 'marker, 'message, 'cause)}

  inline def debug(inline marker: Marker, inline message: CharSequence, inline cause: Throwable): Unit =
    ${LoggerMacro.fatalMarkerCseqThrowable('delegate, 'marker, 'message, 'cause)}

  inline def debug(inline marker: Marker, inline message: AnyRef, inline cause: Throwable): Unit =
    ${LoggerMacro.debugMarkerObjectThrowable('delegate, 'marker, 'message, 'cause)}

  inline def debug(inline message: Message): Unit =
    ${LoggerMacro.debugMsg('delegate, 'message)}

  inline def debug(inline message: CharSequence): Unit =
    ${LoggerMacro.debugCseq('delegate, 'message)}

  inline def debug(inline message: AnyRef): Unit =
    ${LoggerMacro.debugObject('delegate, 'message)}

  inline def debug(inline message: Message, inline cause: Throwable): Unit =
    ${LoggerMacro.debugMsgThrowable('delegate, 'message, 'cause)}

  inline def debug(inline message: CharSequence, inline cause: Throwable): Unit =
    ${LoggerMacro.debugCseqThrowable('delegate, 'message, 'cause)}

  inline def debug(inline message: AnyRef, inline cause: Throwable): Unit =
    ${LoggerMacro.debugObjectThrowable('delegate, 'message, 'cause)}


  inline def trace(inline marker: Marker, inline message: Message): Unit =
    ${LoggerMacro.traceMarkerMsg('delegate, 'marker, 'message)}

  inline def trace(inline marker: Marker, inline message: CharSequence): Unit =
    ${LoggerMacro.traceMarkerCseq('delegate, 'marker, 'message)}

  inline def trace(inline marker: Marker, inline message: AnyRef): Unit =
    ${LoggerMacro.traceMarkerObject('delegate, 'marker, 'message)}

  inline def trace(inline marker: Marker, inline message: Message, inline cause: Throwable): Unit =
    ${LoggerMacro.traceMarkerMsgThrowable('delegate, 'marker, 'message, 'cause)}

  inline def trace(inline marker: Marker, inline message: CharSequence, inline cause: Throwable): Unit =
    ${LoggerMacro.fatalMarkerCseqThrowable('delegate, 'marker, 'message, 'cause)}

  inline def trace(inline marker: Marker, inline message: AnyRef, inline cause: Throwable): Unit =
    ${LoggerMacro.traceMarkerObjectThrowable('delegate, 'marker, 'message, 'cause)}

  inline def trace(inline message: Message): Unit =
    ${LoggerMacro.traceMsg('delegate, 'message)}

  inline def trace(inline message: CharSequence): Unit =
    ${LoggerMacro.traceCseq('delegate, 'message)}

  inline def trace(inline message: AnyRef): Unit =
    ${LoggerMacro.traceObject('delegate, 'message)}

  inline def trace(inline message: Message, inline cause: Throwable): Unit =
    ${LoggerMacro.traceMsgThrowable('delegate, 'message, 'cause)}

  inline def trace(inline message: CharSequence, inline cause: Throwable): Unit =
    ${LoggerMacro.traceCseqThrowable('delegate, 'message, 'cause)}

  inline def trace(inline message: AnyRef, inline cause: Throwable): Unit =
    ${LoggerMacro.traceObjectThrowable('delegate, 'message, 'cause)}


  /**
    * Logs a `Message` with the specific `Marker` at the given `Level`.
    *
    * @param level   the logging level
    * @param marker  the marker data specific to this log statement
    * @param message the message to be logged
    */
  inline def apply(level: Level, marker: Marker, message: Message): Unit = {
    if (delegate.isEnabled(level, marker)) {
      delegate.log(level, marker, message)
    }
  }

  /**
    * Logs a string with the specific `Marker` at the given `Level`.
    *
    * @param level   the logging level
    * @param marker  the marker data specific to this log statement
    * @param message the message to be logged
    */
  inline def apply(inline level: Level, inline marker: Marker, inline message: CharSequence): Unit =
    ${LoggerMacro.logMarkerCseq('delegate, 'level, 'marker, 'message)}

  /**
    * Logs an object with the specific `Marker` at the given `Level`.
    *
    * @param level   the logging level
    * @param marker  the marker data specific to this log statement
    * @param message the message to be logged
    */
  inline def apply(level: Level, marker: Marker, message: AnyRef): Unit = {
    if (delegate.isEnabled(level, marker)) {
      delegate.log(level, marker, message)
    }
  }

  /**
    * Logs a `Message` with the specific `Marker` at the given `Level` including the stack trace
    * of the given `Throwable`.
    *
    * @param level   the logging level
    * @param marker  the marker data specific to this log statement
    * @param message the message to be logged
    * @param cause   the cause
    */
  inline def apply(level: Level, marker: Marker, message: Message, cause: Throwable): Unit = {
    if (delegate.isEnabled(level, marker)) {
      delegate.log(level, marker, message, cause)
    }
  }

  /**
    * Logs a string with the specific `Marker` at the given `Level` including the stack trace
    * of the given `Throwable`.
    *
    * @param level   the logging level
    * @param marker  the marker data specific to this log statement
    * @param message the message to be logged
    * @param cause   the cause
    */
  inline def apply(level: Level, marker: Marker, message: CharSequence, cause: Throwable): Unit =
    ${LoggerMacro.logMarkerCseqThrowable('delegate, 'level, 'marker, 'message, 'cause)}

  /**
    * Logs an object with the specific `Marker` at the given `Level` including the stack trace
    * of the given `Throwable`.
    *
    * @param level   the logging level
    * @param marker  the marker data specific to this log statement
    * @param message the message to be logged
    * @param cause   the cause
    */
  inline def apply(level: Level, marker: Marker, message: AnyRef, cause: Throwable): Unit = {
    if (delegate.isEnabled(level, marker)) {
      delegate.log(level, marker, message, cause)
    }
  }

  /**
    * Logs a `Message` at the given `Level`.
    *
    * @param level   the logging level
    * @param message the message to be logged
    */
  inline def apply(level: Level, message: Message): Unit = {
    if (delegate.isEnabled(level)) {
      delegate.log(level, message)
    }
  }

  /**
    * Logs a string at the given `Level`.
    *
    * @param level   the logging level
    * @param message the message to be logged
    */
  inline def apply(inline level: Level, inline message: CharSequence): Unit =
    ${LoggerMacro.logCseq('delegate, 'level, 'message)}

  /**
    * Logs an object at the given `Level`.
    *
    * @param level   the logging level
    * @param message the message to be logged
    */
  inline def apply(level: Level, message: AnyRef): Unit = {
    if (delegate.isEnabled(level)) {
      delegate.log(level, message)
    }
  }

  /**
    * Logs a `Message` at the given `Level` including the stack trace of the given `Throwable`.
    *
    * @param level   the logging level
    * @param message the message to be logged
    * @param cause   a `Throwable`
    */
  inline def apply(level: Level, message: Message, cause: Throwable): Unit = {
    if (delegate.isEnabled(level)) {
      delegate.log(level, message, cause)
    }
  }

  /**
    * Logs a string at the given `Level` including the stack trace of the given `Throwable`.
    *
    * @param level   the logging level
    * @param message the message to be logged
    * @param cause   a `Throwable`
    */
  inline def apply(inline level: Level, inline message: CharSequence, inline cause: Throwable): Unit =
    ${LoggerMacro.logCseqThrowable('delegate, 'level, 'message, 'cause)}

  /**
    * Logs an object at the given `Level` including the stack trace of the given `Throwable`.
    *
    * @param level   the logging level
    * @param message the message to be logged
    * @param cause   a `Throwable`
    */
  inline def apply(level: Level, message: AnyRef, cause: Throwable): Unit = {
    if (delegate.isEnabled(level)) {
      delegate.log(level, message, cause)
    }
  }


  /**
    * Logs entry to a method. Used when the method in question has no parameters or when the parameters should not be
    * logged.
    *
    * @return The built `EntryMessage`
    */
  inline def traceEntry(): EntryMessage =
    delegate.traceEntry()

  /**
    * Logs entry to a method along with its parameters.
    *
    * {{{
    * def doSomething(foo: String, bar: Int): Unit = {
    *   logger.traceEntry(foo, bar)
    *   // do something
    * }
    * }}}
    *
    * @param params the parameters to the method.
    * @return The built `EntryMessage`
    */
  inline def traceEntry(params: AnyRef*): EntryMessage = {
    params match {
      case Seq() => delegate.traceEntry()
      case seq if seq.head.isInstanceOf[CharSequence] => {
        delegate.traceEntry(seq.head.toString, seq.tail:_*)
      }
      case _ => ???
    }
  }

  /**
    * Logs entry to a method using a `Message` to describe the parameters.
    *
    * {{{
    * def doSomething(foo: Request): Unit = {
    *   logger.traceEntry(JsonMessage(foo))
    *   // do something
    * }
    * }}}
    *
    * @param message the message
    * @return The built `EntryMessage`
    */
  inline def traceEntry(message: Message): EntryMessage =
    delegate.traceEntry(message)

  /**
    * Logs exit from a method with no result.
    */
  inline def traceExit(): Unit =
    delegate.traceExit()

  /**
    * Logs exiting from a method with result.
    *
    * @param result The result being returned from the method call
    * @return `result`
    */
  inline def traceExit[R](result: R): R =
    delegate.traceExit(result)

  /**
    * Logs exiting from a method with no result.
    *
    * @param entryMessage the `EntryMessage` returned from one of the `traceEntry` methods
    */
  inline def traceExit(entryMessage: EntryMessage): Unit =
    delegate.traceExit(entryMessage)

  /**
    * Logs exiting from a method with result.
    *
    * {{{
    * def doSomething(foo: String, bar: Int): Int = {
    *   val entryMessage = logger.traceEntry(foo, bar)
    *   // do something
    *   traceExit(entryMessage, value)
    * }
    * }}}
    *
    * @param entryMessage the `EntryMessage` returned from one of the `traceEntry` methods
    * @param result       The result being returned from the method call
    * @return `result`
    */
  inline def traceExit[R](entryMessage: EntryMessage, result: R): R =
    delegate.traceExit(entryMessage, result)

  /**
    * Logs exiting from a method with result. Allows custom formatting of the result.
    *
    * @param message the Message containing the formatted result
    * @param result  The result being returned from the method call.
    * @return `result`
    */
  inline def traceExit[R](message: Message, result: R): R =
    delegate.traceExit(message, result)

  /**
    * Logs an exception or error to be thrown.
    *
    * {{{
    *   throw logger.throwing(myException)
    * }}}
    *
    * @param t the Throwable
    * @return `t`
    */
  inline def throwing[T <: Throwable](t: T): T =
    delegate.throwing(t)

  /**
    * Logs an exception or error to be thrown to a specific logging level.
    *
    * {{{
    *   throw logger.throwing(Level.DEBUG, myException)
    * }}}
    *
    * @param level the logging Level.
    * @param t     the Throwable
    * @return `t`
    */
  inline def throwing[T <: Throwable](level: Level, t: T): T =
    delegate.throwing(level, t)

  /**
    * Logs an exception or error that has been caught.
    *
    * @param t the Throwable.
    */
  inline def catching(t: Throwable): Unit =
    delegate.catching(t)

  /**
    * Logs an exception or error that has been caught to a specific logging level.
    *
    * @param level The logging Level.
    * @param t     The Throwable.
    */
  inline def catching(level: Level, t: Throwable): Unit =
    delegate.catching(level, t)


  /** Always logs a message at the specified level. It is the responsibility of the caller to ensure the specified
    * level is enabled.
    *
    * Should normally not be used directly from application code, but needs to be public for access by macros.
    *
    * @param level   log level
    * @param marker  marker or `null`
    * @param message message
    * @param cause   cause or `null`
    */
  def logMessage(level: Level, marker: Marker, message: Message, cause: Throwable): Unit = {
    delegate.logMessage(Logger.FQCN, level, marker, message, cause)
  }

  /** Always logs a message at the specified level. It is the responsibility of the caller to ensure the specified
    * level is enabled.
    *
    * Should normally not be used directly from application code, but needs to be public for access by macros.
    *
    * @param level   log level
    * @param marker  marker or `null`
    * @param message message
    * @param cause   cause or `null`
    */
  def logMessage(level: Level, marker: Marker, message: CharSequence, cause: Throwable): Unit = {
    delegate.logMessage(Logger.FQCN, level, marker, delegate.getMessageFactory.asInstanceOf[MessageFactory2].newMessage(message), cause)
  }

  /** Always logs a message at the specified level. It is the responsibility of the caller to ensure the specified
    * level is enabled.
    *
    * Should normally not be used directly from application code, but needs to be public for access by macros.
    *
    * @param level   log level
    * @param marker  marker or `null`
    * @param message message
    * @param cause   cause or `null`
    */
  def logMessage(level: Level, marker: Marker, message: AnyRef, cause: Throwable): Unit = {
    delegate.logMessage(Logger.FQCN, level, marker, delegate.getMessageFactory.asInstanceOf[MessageFactory2].newMessage(message), cause)
  }

}
