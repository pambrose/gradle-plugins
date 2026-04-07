package com.pambrose

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get

/**
 * Gradle plugin that configures Maven publishing with a sources JAR.
 *
 * Applies the `maven-publish` and `java` plugins, enables the sources JAR
 * artifact, and creates a `maven` publication from the project's `java` component.
 *
 * The resulting publication can be installed locally via `publishToMavenLocal`
 * or pushed to a remote repository configured in the consuming project.
 */
class PublishingPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    with(project) {
      pluginManager.apply("maven-publish")
      pluginManager.apply("java")

      extensions.configure<JavaPluginExtension> {
        withSourcesJar()
      }

      extensions.configure<PublishingExtension> {
        publications {
          create<MavenPublication>("maven") {
            from(components["java"])
          }
        }
      }
    }
  }
}