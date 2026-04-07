package com.pambrose

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.withType

/**
 * Gradle plugin that configures all [Test] tasks to use JUnit Platform
 * with verbose logging.
 *
 * Configured defaults:
 * - Events: PASSED, SKIPPED, and FAILED are logged.
 * - Exception format: FULL stack traces on failure.
 * - Standard streams: suppressed to keep output focused on test results.
 */
class TestingPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    with(project) {
      tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
          events = setOf(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
          exceptionFormat = TestExceptionFormat.FULL
          showStandardStreams = false
        }
      }
    }
  }
}
