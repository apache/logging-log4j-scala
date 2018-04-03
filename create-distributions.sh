#!/bin/bash
#    Licensed to the Apache Software Foundation (ASF) under one or more
#    contributor license agreements.  See the NOTICE file distributed with
#    this work for additional information regarding copyright ownership.
#    The ASF licenses this file to You under the Apache License, Version 2.0
#    (the "License"); you may not use this file except in compliance with
#    the License.  You may obtain a copy of the License at
#
#         http://www.apache.org/licenses/LICENSE-2.0
#
#    Unless required by applicable law or agreed to in writing, software
#    distributed under the License is distributed on an "AS IS" BASIS,
#    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#    See the License for the specific language governing permissions and
#    limitations under the License.

function sign_file() {
  release_key=${GPG_KEYID:-748F15B2CF9BA8F024155E6ED7C92B70FA1C814D}
  gpg --detach-sign --armor --user ${release_key} "$1"
}

function create_sources_from_tag() {
  out_format=$1
  release_version=$2
  git archive --prefix=apache-log4j-api-scala-${release_version}-src/ -o target/apache-log4j-api-scala-${release_version}-src.${out_format} ${release_version}
  sign_file target/apache-log4j-api-scala-${release_version}-src.${out_format}
}

function create_all_sources_from_tag() {
  create_sources_from_tag zip $1
  create_sources_from_tag tar.gz $1
}

function create_sources() {
  out_format=$1
  git archive --prefix=apache-log4j-api-scala-SNAPSHOT-src/ -o target/apache-log4j-api-scala-SNAPSHOT-src.${out_format} HEAD
}

function create_all_sources() {
  create_sources zip
  create_sources tar.gz
}

function create_all_binaries() {
  ./sbt -batch "; + package; packageDistributions"
}

function create_all() {
  create_all_sources
  create_all_binaries
}

function main() {
  case $1 in
    SNAPSHOT) create_all;;
    *) create_all_sources_from_tag $1; create_all_binaries;;
  esac
}

tag=${1:-SNAPSHOT}

main ${tag}
