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
import java.io.FileOutputStream
import java.util.zip.GZIPOutputStream

import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import sbt.{Def, _}
import sbt.plugins.JvmPlugin

object Distributions extends AutoPlugin {
  override lazy val trigger = allRequirements

  override lazy val requires = JvmPlugin

  object autoImport {
    // run this task after "+ package" to get all versions included
    val packageDistributions = taskKey[Seq[File]]("Packages zip and tar.gz binary distributions")
    val packageZip = packageDistributionTask("zip")
    val packageTarGz = packageDistributionTask("tar.gz")
  }

  import autoImport._

  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    packageDistributions := Seq(
      packageZip.value,
      packageTarGz.value
    )
  )

  private val classified = "-([a-z]+)\\.jar".r

  def packageDistributionTask(ext: String): Def.Initialize[Task[File]] = Def.task {
    val target = Keys.target.value
    val binaries = target * "scala-*" ** "log4j-api-scala*.jar" pair { file =>
      file.name match {
        case classified(_) => None
        case _ => Some(file.name)
      }
    }
    val base = Keys.baseDirectory.value
    val inputs = binaries ++ Seq(
      (base / "LICENSE.txt", "LICENSE"),
      (base / "NOTICE.txt", "NOTICE")
    )
    val version = Keys.version.value
    val output = target / s"apache-log4j-api-scala-$version-bin.$ext"
    ext match {
      case "zip" => makeZip(inputs, output)
      case "tar.gz" => makeTarGz(inputs, output)
      case _ => sys.error(s"Unknown distribution extension: $ext")
    }
    output
  }

  def tarGz(sources: Traversable[(File, String)], output: File): Unit = {
    val out = new TarArchiveOutputStream(new GZIPOutputStream(new FileOutputStream(output)))
    for ((f, name) <- sources) {
      out.putArchiveEntry(out.createArchiveEntry(f, name))
      IO.transfer(f, out)
      out.closeArchiveEntry()
    }
    out.finish()
    out.close()
  }

  def makeTarGz(inputs: Traversable[(File, String)], output: File): Unit = {
    val out = output.getAbsoluteFile
    if (out.exists()) {
      if (out.isFile) out.delete() else sys.error(s"Cannot write to file $out as it is not a file")
    }
    tarGz(inputs, out)
  }

  def makeZip(inputs: Traversable[(File, String)], output: File): Unit = {
    val out = output.getAbsoluteFile
    if (out.exists()) {
      if (out.isFile) out.delete() else sys.error(s"Cannot write to file $out as it is not a file")
    }
    IO.zip(inputs, out)
  }
}
