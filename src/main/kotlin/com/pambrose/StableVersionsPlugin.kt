package com.pambrose

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

/**
 * Gradle plugin that applies the [Ben-Manes Versions plugin](https://github.com/ben-manes/gradle-versions-plugin)
 * and configures it to reject non-stable dependency update candidates.
 *
 * Versions containing qualifiers such as `-RC`, `-BETA`, `-ALPHA`, or `-M`
 * (case-insensitive) are treated as non-stable and excluded from the
 * `dependencyUpdates` report.
 */
class StableVersionsPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    with(project) {
      pluginManager.apply("com.github.ben-manes.versions")

      tasks.withType<DependencyUpdatesTask> {
        notCompatibleWithConfigurationCache("the dependency updates plugin is not compatible with the configuration cache")
        rejectVersionIf {
          isNonStable(candidate.version)
        }
      }
    }
  }

  companion object {
    private val UNSTABLE_QUALIFIERS = listOf("-RC", "-BETA", "-ALPHA", "-M")

    /**
     * Returns `true` if [version] contains a pre-release qualifier
     * (RC, BETA, ALPHA, or M).
     */
    internal fun isNonStable(version: String): Boolean =
      UNSTABLE_QUALIFIERS.any { version.uppercase().contains(it) }
  }
}
