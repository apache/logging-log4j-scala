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

  def infoMsg(underlying: Expr[ExtendedLogger], message: Expr[Message])(using Quotes): Expr[Unit] = {
    '{ if ($underlying.isEnabled(Level.INFO)) $underlying.info($message) }
  }

  def infoMarkerMsg(underlying: Expr[ExtendedLogger], marker: Expr[Marker], message: Expr[Message])(using Quotes): Expr[Unit] = {
    '{ if ($underlying.isEnabled(Level.INFO, $marker)) $underlying.info($marker, $message) }
  }

  def infoCseq(underlying: Expr[ExtendedLogger], message: Expr[CharSequence])(using Quotes): Expr[Unit] = {
    val (messageFormat, args) = deconstructInterpolatedMessage(message)
    infoMessageArgs(underlying, messageFormat, Expr.ofSeq(args))
  }

  def infoMarkerCseq(underlying: Expr[ExtendedLogger], marker: Expr[Marker], message: Expr[CharSequence])(using Quotes): Expr[Unit] = {
    val (messageFormat, args) = deconstructInterpolatedMessage(message)
    infoMarkerMessageArgs(underlying, marker, messageFormat, Expr.ofSeq(args))
  }

  def infoObject(underlying: Expr[ExtendedLogger], message: Expr[AnyRef])(using Quotes): Expr[Unit] = {
    '{ if ($underlying.isEnabled(Level.INFO)) $underlying.info($message) }
  }

  def infoMarkerObject(underlying: Expr[ExtendedLogger], marker: Expr[Marker], message: Expr[AnyRef])(using Quotes): Expr[Unit] = {
    '{ if ($underlying.isEnabled(Level.INFO, $marker)) $underlying.info($marker, $message) }
  }

  private def infoMessageArgs(underlying: Expr[ExtendedLogger], message: Expr[CharSequence], args: Expr[Seq[Any]]) (using Quotes) = {
    val anyRefArgs = formatArgs(args)
    if(anyRefArgs.isEmpty)
    '{ if ($underlying.isEnabled(Level.INFO)) $underlying.info(${charSequenceExprToStringExpr(message)}) }
    else if(anyRefArgs.length == 1)
    '{ if ($underlying.isEnabled(Level.INFO)) $underlying.info(${charSequenceExprToStringExpr(message)}, ${anyRefArgs.head}) }
    else
    '{ if ($underlying.isEnabled(Level.INFO)) $underlying.info(${charSequenceExprToStringExpr(message)}, ${Expr.ofSeq(anyRefArgs)}*) }
  }

  private def infoMarkerMessageArgs(underlying: Expr[ExtendedLogger], marker: Expr[Marker], message: Expr[CharSequence], args: Expr[Seq[Any]]) (using Quotes) = {
    val anyRefArgs = formatArgs(args)
    if(anyRefArgs.isEmpty)
    '{ if ($underlying.isEnabled(Level.INFO, $marker)) $underlying.info($marker, ${charSequenceExprToStringExpr(message)}) }
    else if(anyRefArgs.length == 1)
    '{ if ($underlying.isEnabled(Level.INFO, $marker)) $underlying.info($marker, ${charSequenceExprToStringExpr(message)}, ${anyRefArgs.head}) }
    else
    '{ if ($underlying.isEnabled(Level.INFO, $marker)) $underlying.info($marker, ${charSequenceExprToStringExpr(message)}, ${Expr.ofSeq(anyRefArgs)}*) }
  }

  /** Checks whether `message` is an interpolated string and transforms it into LOG4J string interpolation. */
  private def deconstructInterpolatedMessage(message: Expr[CharSequence])(using Quotes): (Expr[String], Seq[Expr[Any]]) = {
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
              // Escape literal log4j format anchors if the resulting call will require a format string
              .map(str => if (args.nonEmpty) str.replace("{}", "\\{}") else str)
              .mkString("{}")

            val formatArgs = args.map(_.asExpr)

            (Expr(format), formatArgs)
          case _ =>
            (charSequenceExprToStringExpr(message), Seq.empty)
        }
      case _ => (charSequenceExprToStringExpr(message), Seq.empty)
    }
  }
  
  private def formatArgs(args: Expr[Seq[Any]])(using q: Quotes): Seq[Expr[Object]] = {
    import quotes.reflect.*
    import util.*

    args.asTerm match {
      case p@Inlined(_, _, Typed(Repeated(v, _),_)) =>
        v.map{
          case t if t.tpe <:< TypeRepr.of[AnyRef] => t.asExprOf[Object]
          case t => '{${t.asExpr}.asInstanceOf[Object]}
        }
      case Repeated(v, _) =>
        v.map{
          case t if t.tpe <:< TypeRepr.of[Object] => t.asExprOf[Object]
          case t => '{${t.asExpr}.asInstanceOf[Object]}
        }
      case _ => Seq.empty
    }
  }

  private def charSequenceExprToStringExpr(expr: Expr[CharSequence])(using Quotes): Expr[String] = expr match {
    case '{ $cs } => Expr(cs.toString)
  }
}
