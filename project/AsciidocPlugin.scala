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
import java.util

import com.typesafe.sbt.site.SitePlugin
import com.typesafe.sbt.site.SitePlugin.autoImport.siteSubdirName
import com.typesafe.sbt.site.util.SiteHelpers
import org.asciidoctor.Asciidoctor.Factory
import org.asciidoctor.{DirectoryWalker, Options, SafeMode}
import sbt.Keys._
import sbt._

import scala.collection.JavaConverters._

/** Asciidoctor generator. */
object AsciidocPlugin extends AutoPlugin {
  override lazy val requires = SitePlugin
  override lazy val trigger = allRequirements

  object autoImport {
    val Asciidoc = config("asciidoctor")
    val siteProperties = settingKey[Seq[(String, AnyRef)]]("Properties to filter through asciidoc")
  }

  import autoImport._

  override def projectSettings = asciidoctorSettings(Asciidoc)

  /** Creates settings necessary for running Asciidoctor in the given configuration. */
  def asciidoctorSettings(config: Configuration): Seq[Setting[_]] =
    inConfig(config)(
      Seq(
        includeFilter := AllPassFilter,
        unmanagedSources := (sourceDirectory.value ** "*.adoc").get,
        managedSources := Seq(),
        sources := unmanagedSources.value ++ managedSources.value,
        siteProperties := Seq("project-version" -> version.value),
        mappings := generate(sources.value, target.value, includeFilter.value, siteProperties.value),
        siteSubdirName := ""
      )
    ) ++
      SiteHelpers.directorySettings(config) ++
      SiteHelpers.watchSettings(config) ++
      SiteHelpers.addMappingsToSiteDir(mappings in config, siteSubdirName in config)

  /** Run asciidoctor in new ClassLoader. */
  private def generate(inputs: Seq[File], outputDirectory: File, includeFilter: FileFilter, props: Seq[(String, AnyRef)])
  : Seq[(File, String)] = {
    val oldContextClassLoader = Thread.currentThread().getContextClassLoader
    Thread.currentThread().setContextClassLoader(this.getClass.getClassLoader)
    try {
      val asciidoctor = Factory.create()
      if (!outputDirectory.exists()) outputDirectory.mkdirs()
      val options = new Options
      options.setToDir(outputDirectory.getAbsolutePath)
      options.setDestinationDir(outputDirectory.getAbsolutePath)
      options.setSafe(SafeMode.UNSAFE)
      // need to do this explicitly through HashMap because otherwise JRuby complains
      val attributes = new util.HashMap[String, AnyRef]
      for ((key, value) <- props) attributes.put(key, value)
      options.setAttributes(attributes)
      val walker = new DirectoryWalker {
        override def scan(): util.List[File] = inputs.asJava
      }
      asciidoctor.renderDirectory(walker, options)
    } finally {
      Thread.currentThread().setContextClassLoader(oldContextClassLoader)
    }
    outputDirectory ** includeFilter --- outputDirectory pair Path.relativeTo(outputDirectory)
  }
}