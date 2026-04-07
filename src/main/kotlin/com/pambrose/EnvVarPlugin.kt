package com.pambrose

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.withType

/**
 * Configuration extension for [EnvVarPlugin].
 *
 * @property filename Path to the environment file, relative to the project directory.
 *   Defaults to `.env`.
 * @property vars The parsed environment variables. Populated after project evaluation
 *   and available for other plugins or tasks to read.
 */
open class EnvVarExtension {
  var filename: String = ".env"
  val vars: MutableMap<String, String> = mutableMapOf()
}

/**
 * Gradle plugin that loads environment variables from a file and injects them
 * into [JavaExec] and [Test] tasks.
 *
 * The file is parsed after project evaluation. Lines starting with `#` and blank
 * lines are ignored. Each remaining line is split on the first `=` character into
 * a key-value pair.
 *
 * Register an `envvar` extension block to customise the source file path:
 * ```kotlin
 * envvar {
 *   filename = "config/my.env"
 * }
 * ```
 *
 * @see EnvVarExtension
 */
class EnvVarPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    val extension = project.extensions.create<EnvVarExtension>("envvar")

    project.afterEvaluate {
      val secretsFile = file(extension.filename)
      if (secretsFile.exists()) {
        val envVars =
          secretsFile.readLines()
            .map { it.trim() }
            .filter { it.isNotEmpty() && !it.startsWith("#") }
            .mapNotNull { line ->
              val idx = line.indexOf('=')
              if (idx > 0) line.substring(0, idx).trim() to line.substring(idx + 1).trim() else null
            }
            .toMap()

        // Make the envVars available to other plugins
        extension.vars.putAll(envVars)

        /*
        Finds all tasks of type JavaExec and Test (both existing and any added later) and
        adds envVars to each task's process environment. When those tasks run, the child
        JVM process will have those environment variables set — alongside the system's
        existing environment variables.
         */
        tasks.withType<JavaExec>().configureEach { environment(envVars) }
        tasks.withType<Test>().configureEach { environment(envVars) }
      }
    }
  }
}
