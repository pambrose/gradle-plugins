package com.pambrose

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.withType

/**
 * Extension for [TestingPlugin] configuration.
 */
abstract class TestingExtension {
  /**
   * When `true` (default), adds a logback-classic dependency to the `testRuntimeOnly` configuration.
   * Brings `slf4j-api` onto the test classpath so libraries that use SLF4J (directly or via
   * `kotlin-logging`) can log during tests without a `NoClassDefFoundError`.
   */
  abstract val addLogbackClassic: Property<Boolean>

  /**
   * Version of `ch.qos.logback:logback-classic` added when [addLogbackClassic] is `true`.
   */
  abstract val logbackVersion: Property<String>
}

/**
 * Gradle plugin that configures all [Test] tasks to use JUnit Platform
 * with verbose logging, and optionally adds a default SLF4J backend to
 * the `testRuntimeOnly` configuration.
 *
 * Configured defaults:
 * - Events: PASSED, SKIPPED, and FAILED are logged.
 * - Exception format: FULL stack traces on failure.
 * - Standard streams: suppressed to keep output focused on test results.
 * - `testRuntimeOnly("ch.qos.logback:logback-classic:<default>")` is added when the
 *   consuming project applies the `java` plugin. Configurable via the `testing` extension.
 */
class TestingPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    with(project) {
      val ext = extensions.create<TestingExtension>("pambroseTesting").apply {
        addLogbackClassic.convention(true)
        logbackVersion.convention(DEFAULT_LOGBACK_VERSION)
      }

      tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
          events = setOf(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
          exceptionFormat = TestExceptionFormat.FULL
          showStandardStreams = false
        }
      }

      pluginManager.withPlugin("java") {
        afterEvaluate {
          if (ext.addLogbackClassic.get()) {
            dependencies.add(
              "testRuntimeOnly",
              "ch.qos.logback:logback-classic:${ext.logbackVersion.get()}",
            )
          }
        }
      }
    }
  }

  private companion object {
    const val DEFAULT_LOGBACK_VERSION = "1.5.18"
  }
}
