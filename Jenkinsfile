#!groovy
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

pipeline {
    agent {
        label 'ubuntu'
    }
    tools {
        jdk 'JDK 1.8 (latest)'
    }
    stages {
        stage('Build') {
            steps {
                ansiColor('xterm') {
                    sh './sbt -batch auditCheck'
                    sh './sbt -batch "+ compile"'
                }
            }
        }
        stage('Test') {
            steps {
                ansiColor('xterm') {
                    sh './sbt -batch Test/auditCheck'
                    sh './sbt -batch "+ test"'
                }
            }
        }
        stage('Deploy') {
            when {
                anyOf {
                    branch 'master'
                    branch 'sbt'
                }
            }
            steps {
                ansiColor('xterm') {
                    withCredentials([
                            usernamePassword(
                                    credentialsId: 'logging-snapshots',
                                    passwordVariable: 'NEXUS_PASSWORD',
                                    usernameVariable: 'NEXUS_USERNAME')
                    ]) {
                        sh './sbt -batch "+ publish"'
                    }
                }
            }
            post {
                failure {
                    emailext body: "See <${env.BUILD_URL}>", replyTo: 'dev@logging.apache.org', subject: "[Scala] Jenkins build failure (#${env.BUILD_NUMBER})", to: 'notifications@logging.apache.org'
                }
            }
        }
    }
}
