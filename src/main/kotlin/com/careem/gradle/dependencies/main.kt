/*
* Copyright Careem, an Uber Technologies Inc. company
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

@file:JvmName("DependencyTreeTldr")

package com.careem.gradle.dependencies

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.system.exitProcess

fun main(args: Array<String>) {
  val help = "-h" in args || "--help" in args
  if (help || args.size < 2) {
    printHelp()
    if (!help) {
      exitProcess(1)
    }
    return
  }

  val files = arrayOf("", "")
  val collapse = mutableListOf<String>()

  var filesIndex = 0
  var isNextCollapse = false
  args.forEachIndexed { index, _ ->
    when {
      isNextCollapse -> {
        collapse.add(args[index])
        isNextCollapse = false
      }
      "--collapse" == args[index] -> {
        isNextCollapse = true
      }
      else -> {
        files[filesIndex++] = args[index]
      }
    }
  }

  val old = Paths.get(files[0]).readText()
  val new = Paths.get(files[1]).readText()

  print(tldr(old, new, collapse))
}

private fun printHelp() {
  System.err.println(
    "Usage: dependency-tree-tldr old.txt new.txt\n" +
            "  You can also pass in one or more optional --collapse arguments.\n" +
            "  Note that these will only collapse if all version numbers match.\n" +
            "  (ex --collapse com.careem.ridehail --collapse com.careem.now)"
  )
}

private fun Path.readText(charset: Charset = StandardCharsets.UTF_8): String {
  return Files.readAllBytes(this).toString(charset)
}