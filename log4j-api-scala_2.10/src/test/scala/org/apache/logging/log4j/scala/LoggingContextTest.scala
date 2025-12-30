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

import scala.collection.mutable

@RunWith(classOf[JUnitRunner])
class LoggingContextTest extends AnyFunSuite with Matchers {

  test("put single, contains, get") {
    LoggingContext += "key" -> "value"

    LoggingContext.contains("key") shouldBe true
    LoggingContext.get("key") shouldBe Some("value")
    LoggingContext.contains("bogus") shouldBe false
    LoggingContext.get("bogus") shouldBe None
  }

  test("put multiple 1") {
    LoggingContext ++= Seq("key1" -> "value1", "key2" -> "value2")

    LoggingContext.get("key1") shouldBe Some("value1")
    LoggingContext.get("key2") shouldBe Some("value2")
  }

  test("put multiple 2") {
    LoggingContext ++= Seq("key1" -> "value1", "key2" -> "value2")

    LoggingContext.get("key1") shouldBe Some("value1")
    LoggingContext.get("key2") shouldBe Some("value2")
  }

  test("remove single") {
    LoggingContext += ("key1" -> "value1")
    LoggingContext += ("key2" -> "value2")
    LoggingContext += ("key3" -> "value3")

    LoggingContext -= "key1"

    LoggingContext.get("key1") shouldBe None
    LoggingContext.get("key2") shouldBe Some("value2")
    LoggingContext.get("key3") shouldBe Some("value3")
  }

  test("remove multiple 1") {
    LoggingContext += ("key1" -> "value1")
    LoggingContext += ("key2" -> "value2")
    LoggingContext += ("key3" -> "value3")

    LoggingContext --= Set("key1", "key2")

    LoggingContext.get("key1") shouldBe None
    LoggingContext.get("key2") shouldBe None
    LoggingContext.get("key3") shouldBe Some("value3")
  }

  test("remove multiple 2") {
    LoggingContext += ("key1" -> "value1")
    LoggingContext += ("key2" -> "value2")
    LoggingContext += ("key3" -> "value3")

    LoggingContext --= Seq("key1", "key2")

    LoggingContext.get("key1") shouldBe None
    LoggingContext.get("key2") shouldBe None
    LoggingContext.get("key3") shouldBe Some("value3")
  }

  test("clear, size, isEmpty") {
    LoggingContext += ("key" -> "value")

    LoggingContext.clear()

    LoggingContext.contains("key") shouldBe false
    LoggingContext.size shouldBe 0
    LoggingContext.isEmpty shouldBe true
  }

  test("iterator") {
    LoggingContext += ("key1" -> "value1")
    LoggingContext += ("key2" -> "value2")

    LoggingContext.iterator.toSet shouldBe Set("key1" -> "value1", "key2" -> "value2")
  }

  test("foreach") {
    LoggingContext += ("key1" -> "value1")
    LoggingContext += ("key2" -> "value2")

    val result = mutable.Set.empty[(String, String)]

    LoggingContext.foreach { result += _ }

    result shouldBe Set("key1" -> "value1", "key2" -> "value2")
  }

  test("withContext should add and remove values") {
    LoggingContext.clear()
    LoggingContext.withContext(Map("key" -> "value")) {
      LoggingContext.get("key") shouldBe Some("value")
    }
    LoggingContext.contains("key") shouldBe false
  }

  test("withContext should save and restore existing values") {
    LoggingContext.clear()
    LoggingContext += "key" -> "oldValue"

    LoggingContext.withContext(Map("key" -> "newValue")) {
      LoggingContext.get("key") shouldBe Some("newValue")
    }

    // Harus balik ke oldValue, bukan dihapus
    LoggingContext.get("key") shouldBe Some("oldValue")
  }

  test("withContext should handle nested contexts") {
    LoggingContext.clear()

    LoggingContext.withContext(Map("outer" -> "outerValue", "common" -> "outerCommon")) {
      LoggingContext.get("outer") shouldBe Some("outerValue")
      LoggingContext.get("common") shouldBe Some("outerCommon")

      LoggingContext.withContext(Map("inner" -> "innerValue", "common" -> "innerCommon")) {
        LoggingContext.get("outer") shouldBe Some("outerValue")
        LoggingContext.get("inner") shouldBe Some("innerValue")
        // Inner harus override outer
        LoggingContext.get("common") shouldBe Some("innerCommon")
      }

      // Pas keluar inner, common harus balik ke outerCommon
      LoggingContext.get("common") shouldBe Some("outerCommon")
      LoggingContext.contains("inner") shouldBe false
    }

    LoggingContext.isEmpty shouldBe true
  }

  test("withContext should cleanup on exception") {
    LoggingContext.clear()
    intercept[RuntimeException] {
      LoggingContext.withContext(Map("key" -> "value")) {
        throw new RuntimeException("Test exception")
      }
    }

    // Pastikan tetap bersih meski ada error
    LoggingContext.contains("key") shouldBe false
  }

}
