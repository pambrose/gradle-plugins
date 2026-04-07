package com.pambrose

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jmailen.gradle.kotlinter.KotlinterExtension

/**
 * Gradle plugin that applies [kotlinter](https://github.com/jeremymailen/kotlinter-gradle)
 * for Kotlin code style checking and formatting.
 *
 * Configured defaults:
 * - Reporters: `checkstyle` (machine-readable) and `plain` (human-readable).
 *
 * Provides the `lintKotlin` and `formatKotlin` tasks to the consuming project.
 */
class KotlinterPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    with(project) {
      pluginManager.apply("org.jmailen.kotlinter")

      extensions.configure(KotlinterExtension::class.java) {
        reporters = arrayOf("checkstyle", "plain")
      }
    }
  }
}