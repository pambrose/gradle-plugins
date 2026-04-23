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

  /**
   * When `true` (default), adds `io.kotest:kotest-runner-junit5` to `testImplementation`.
   * Only takes effect when the `org.jetbrains.kotlin.jvm` plugin is applied.
   */
  abstract val addKotest: Property<Boolean>

  /**
   * Version of `io.kotest:kotest-runner-junit5` added when [addKotest] is `true`.
   */
  abstract val kotestVersion: Property<String>

  /**
   * When `true` (default), adds `org.jetbrains.kotlin:kotlin-test` to `testImplementation`.
   * Only takes effect when the `org.jetbrains.kotlin.jvm` plugin is applied.
   * The dependency is declared without a version so it resolves to the Kotlin plugin's version.
   */
  abstract val addKotlinTest: Property<Boolean>
}

/**
 * Gradle plugin that configures all [Test] tasks to use JUnit Platform
 * with verbose logging, and optionally adds default test dependencies.
 *
 * Configured defaults:
 * - Events: PASSED, SKIPPED, and FAILED are logged.
 * - Exception format: FULL stack traces on failure.
 * - Standard streams: suppressed to keep output focused on test results.
 * - `testRuntimeOnly("ch.qos.logback:logback-classic:<default>")` when the `java` plugin is applied.
 * - `testImplementation("io.kotest:kotest-runner-junit5:<default>")` when the Kotlin JVM plugin is applied.
 * - `testImplementation("org.jetbrains.kotlin:kotlin-test")` when the Kotlin JVM plugin is applied.
 *
 * All dependencies are configurable via the `pambroseTesting` extension.
 */
class TestingPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    with(project) {
      val ext = extensions.create<TestingExtension>("pambroseTesting").apply {
        addLogbackClassic.convention(true)
        logbackVersion.convention(BuildConfig.DEFAULT_LOGBACK_VERSION)
        addKotest.convention(true)
        kotestVersion.convention(BuildConfig.DEFAULT_KOTEST_VERSION)
        addKotlinTest.convention(true)
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

      pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
        afterEvaluate {
          if (ext.addKotest.get()) {
            dependencies.add(
              "testImplementation",
              "io.kotest:kotest-runner-junit5:${ext.kotestVersion.get()}",
            )
          }
          if (ext.addKotlinTest.get()) {
            dependencies.add("testImplementation", "org.jetbrains.kotlin:kotlin-test")
          }
        }
      }
    }
  }
}
