package com.careem.gradle.dependencies

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import java.io.File

@RunWith(Parameterized::class)
class FixturesTest(private val fixtureDir: File) {

  @Test
  fun run() {
    val before = fixtureDir.resolve("before.txt").readText()
    val after = fixtureDir.resolve("after.txt").readText()
    val expected = fixtureDir.resolve("expected.txt").readText()
    val actual = tldr(before, after, emptyList())
    assertThat(actual).isEqualTo(expected)
  }

  companion object {
    @JvmStatic
    @Parameters(name = "{0}")
    fun params() = File("src/test/fixtures")
      .listFiles { file -> file.isDirectory }
  }
}