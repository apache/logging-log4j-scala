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

import org.apache.logging.log4j.message.{EntryMessage, Message}
import org.apache.logging.log4j.spi.{AbstractLogger, ExtendedLogger}
import org.apache.logging.log4j.{Level, Marker}

import scala.quoted.*

/**
  * Inspired from [[https://github.com/typesafehub/scalalogging ScalaLogging]].
  */
private object LoggerMacro {
  // Info

  def infoMessage(underlying: Expr[ExtendedLogger], message: Expr[String])(using Quotes): Expr[Unit] = {
    val (messageFormat, args) = deconstructInterpolatedMessage(message)
    infoMessageArgs(underlying, messageFormat, Expr.ofSeq(args))
  }

  def infoMessageArgs(underlying: Expr[ExtendedLogger], message: Expr[String], args: Expr[Seq[Any]]) (using Quotes) = {
    val anyRefArgs = formatArgs(args)
    if(anyRefArgs.isEmpty)
    '{ if ($underlying.isEnabled(Level.INFO)) $underlying.info($message) }
    else if(anyRefArgs.length == 1)
    '{ if ($underlying.isEnabled(Level.INFO)) $underlying.info($message, ${anyRefArgs.head}) }
    else
    '{ if ($underlying.isEnabled(Level.INFO)) $underlying.info($message, ${Expr.ofSeq(anyRefArgs)}*) }
  }

  /** Checks whether `message` is an interpolated string and transforms it into LOG4J string interpolation. */
  private def deconstructInterpolatedMessage(message: Expr[String])(using Quotes): (Expr[String], Seq[Expr[Any]]) = {
    import quotes.reflect.*
    import util.*

    message.asTerm match{
      case Inlined(_, _, Apply(Select(Apply(Select(Select(_, "StringContext"), _), messageNode), _), argumentsNode)) =>
        val messageTextPartsOpt: Option[List[String]] =
          messageNode.collectFirst{
            case Typed(Repeated(ls, _), _) =>
              ls.collect{ case Literal(StringConstant(s)) => s}
          }
        val argsOpt: Option[List[Term]] =
          argumentsNode.collectFirst {
            case Typed(Repeated(ls, _), _) => ls
          }

        (messageTextPartsOpt, argsOpt) match{
          case (Some(messageTextParts), Some(args)) =>
            val format = messageTextParts.iterator
              // Emulate standard interpolator escaping
              .map(StringContext.processEscapes)
              // Escape literal slf4j format anchors if the resulting call will require a format string
              .map(str => if (args.nonEmpty) str.replace("{}", "\\{}") else str)
              .mkString("{}")

            val formatArgs = args.map(_.asExpr)

            (Expr(format), formatArgs)
          case _ =>
            (message, Seq.empty)
        }
      case _ => (message, Seq.empty)
    }
  }
  
  def formatArgs(args: Expr[Seq[Any]])(using q: Quotes): Seq[Expr[AnyRef]] = {
    import quotes.reflect.*
    import util.*

    args.asTerm match {
      case p@Inlined(_, _, Typed(Repeated(v, _),_)) =>
        v.map{
          case t if t.tpe <:< TypeRepr.of[AnyRef] => t.asExprOf[AnyRef]
          case t => '{${t.asExpr}.asInstanceOf[AnyRef]}
        }
      case Repeated(v, _) =>
        v.map{
          case t if t.tpe <:< TypeRepr.of[AnyRef] => t.asExprOf[AnyRef]
          case t => '{${t.asExpr}.asInstanceOf[AnyRef]}
        }
      case _ => Seq.empty
    }
  }
}
