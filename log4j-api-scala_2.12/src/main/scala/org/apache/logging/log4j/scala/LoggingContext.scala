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

import org.apache.logging.log4j.ThreadContext

import scala.collection.{immutable, mutable}
import scala.collection.JavaConverters._

/** Manages the context data (context map, MDC) that is added to log events.
  *
  * A wrapper around `org.apache.logging.log4j.ThreadContext`.
  */
object LoggingContext extends mutable.Map[String, String] {

  override def +=(kv: (String, String)): LoggingContext.this.type = {
    ThreadContext.put(kv._1, kv._2)
    this
  }

  override def +=(elem1: (String, String), elem2: (String, String), elems: (String, String)*): LoggingContext.this.type = {
    val builder = immutable.Map.newBuilder[String,String]
    builder += elem1
    builder += elem2
    builder ++= elems
    ThreadContext.putAll(builder.result.asJava)
    this
  }

  override def ++=(xs: TraversableOnce[(String, String)]): LoggingContext.this.type = {
    ThreadContext.putAll(xs.toMap.asJava)
    this
  }

  override def -=(key: String): LoggingContext.this.type = {
    ThreadContext.remove(key)
    this
  }

  override def -=(elem1: String, elem2: String, elems: String*): LoggingContext.this.type = {
    val builder = immutable.Seq.newBuilder[String]
    builder += elem1
    builder += elem2
    builder ++= elems
    ThreadContext.removeAll(builder.result.asJava)
    this
  }

  override def --=(xs: TraversableOnce[String]): LoggingContext.this.type = {
    ThreadContext.removeAll(xs.toSeq.asJava)
    this
  }

  override def clear(): Unit = {
    ThreadContext.clearMap()
  }

  override def contains(key: String): Boolean = ThreadContext.containsKey(key)

  override def get(key: String): Option[String] = Option(ThreadContext.get(key))

  override def iterator: Iterator[(String, String)] = ThreadContext.getImmutableContext.asScala.iterator

  override def foreach[U](f: ((String, String)) => U): Unit = ThreadContext.getImmutableContext.asScala.foreach(f)

  override def size: Int = ThreadContext.getImmutableContext.size()

  override def isEmpty: Boolean = ThreadContext.isEmpty

}
