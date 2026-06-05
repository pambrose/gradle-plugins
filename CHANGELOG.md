# Changelog

All notable changes to this project are documented here. The format follows
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/) and this project
adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.15] - 2026-06-05

### Added
- `TestingPlugin` now includes `TestLogEvent.STANDARD_ERROR` in its default test logging events (#19), so test stderr is surfaced in the build log even with `showStandardStreams = false`.
- The root project's own test logging mirrors the same `STANDARD_ERROR` event.

### Changed
- Upgraded Kotlin to 2.4.0 and the Gradle wrapper to 9.5.1.
- Bumped Kotest to 6.1.11 and Kotlinter to 5.5.0.
- Centralized versions in `gradle/libs.versions.toml` and consolidated build / Makefile configuration (#18).
- Renamed the `gradle` entry in `libs.versions.toml` to `gradle-wrapper` to make its purpose explicit; the Makefile's `GRADLE_VERSION` extraction was updated accordingly.

### Fixed
- Makefile `.PHONY` now declares the helper targets (`_check-gpg-env`, `_require-version`, `_require-gradle-version`) and drops the stale `depends` entry.
- Publish targets now depend on `_require-version` so a missing `version=` in `gradle.properties` fails fast instead of silently publishing a bogus artifact.

## [1.0.14] - 2026-04-22

### Added
- `TestingPlugin` adds `io.kotest:kotest-runner-junit5` to `testImplementation` by default when the `org.jetbrains.kotlin.jvm` plugin is applied. Configurable via `pambroseTesting.addKotest` and `pambroseTesting.kotestVersion`.
- `TestingPlugin` adds `org.jetbrains.kotlin:kotlin-test` (unversioned, resolves to the Kotlin plugin's version) to `testImplementation` by default when the `org.jetbrains.kotlin.jvm` plugin is applied. Configurable via `pambroseTesting.addKotlinTest`.
- Kotest version exposed on the generated `BuildConfig` object as `DEFAULT_KOTEST_VERSION` so `dependencyUpdates` can track it.

## [1.0.13] - 2026-04-22

### Added
- `TestingPlugin` adds `ch.qos.logback:logback-classic` to `testRuntimeOnly` by default.
- Generated internal `com.pambrose.BuildConfig` object exposes versions from `gradle/libs.versions.toml` (e.g. logback) to plugin code so `dependencyUpdates` can track them.
- Dokka documentation support and GitHub Actions workflow (#15).
- README badges and updated title (#14).

### Changed
- Signing configuration fixed for local publishing; `GPG_ENV` extracted in the Makefile (#12).

### Fixed
- Duplicate publication warning; JVM memory bumped for the build (#13).

## [1.0.12] - 2026-04-03

### Added
- Maven Central distribution via the vanniktech `maven-publish` plugin.
- Dokka-generated javadoc JAR and GPG signing for releases.

### Removed
- `ReposPlugin` and `SnapshotPlugin` (and their tests).
- JitPack distribution; Maven Central is now the primary channel.

### Changed
- All documentation updated to reflect Maven Central publishing.

## [1.0.11] - 2026-03-30

### Changed
- Upgraded Gradle wrapper to 9.4.1.
- Bumped Kotest to 6.1.10.

## [1.0.10] - 2026-03-15

### Changed
- Upgraded Gradle wrapper to 9.4.0.
- Bumped Kotest to 6.1.7.

## [1.0.9] - 2026-03-04

### Changed
- Version and documentation housekeeping.

## [1.0.8] - 2026-03-01

### Added
- `CODE_REVIEW.md` and `llms.txt`.
- Missing `ReposPlugin` and `KotlinterPlugin` entries in `CLAUDE.md` plugins table.
- Explicit `showStandardStreams = false` in `TestingPlugin` with a matching test.

### Changed
- `EnvVarPlugin` trims env var keys and values after splitting on `=`.
- Simplified `StableVersionsPlugin.isNonStable` double negation.
- `SnapshotPlugin` uses `configurations.configureEach` instead of `configurations.all`.
- Makefile derives `VERSION` from `build.gradle.kts` instead of hardcoding it.
- Bumped Kotest to 6.1.4.

### Fixed
- "testinging" typo in the README plugin IDs and examples.
- Removed unnecessary `afterEvaluate` in `PublishingPlugin`.

## [1.0.7] - 2026-02-24

### Changed
- Disabled standard output streams in test logging configuration.

## [1.0.6] - 2026-02-21

### Added
- Build trigger and view commands in the Makefile.

### Changed
- Cleaned up test logging configuration in `TestingPlugin`.
- Formatting fixes in `misc.xml`.

## [1.0.5] - 2026-02-21

### Added
- `vars` property on `EnvVarExtension` exposing accessible environment variables.
- Environment variables wired into `JavaExec` and `Test` tasks.
- Apache 2.0 license file.

### Changed
- Project renamed to `pambrose-gradle-plugins`.
- Test logging events updated in `TestingPlugin`.

## [1.0.4] - 2026-02-19

### Added
- `envvar` extension allowing customization of the environment variable file path.

### Changed
- Enhanced dependency updates configuration.

## [1.0.3] - 2026-02-18

### Fixed
- Marked the dependency updates plugin as incompatible with the configuration cache.

## [1.0.2] - 2026-02-18

### Added
- `TestingPlugin` for JUnit Platform configuration.

### Changed
- Renamed `ExcludeBetasPlugin` to `StableVersionsPlugin`.
- Refactored dependency management to use the Gradle version catalog.

## [1.0.1] - 2026-02-17

### Added
- `ReposPlugin` for repository management.
- `KotlinterPlugin` for kotlinter integration.
- Java Gradle Plugin setup.
- Maven Central usage instructions in README.

### Changed
- Group ID updated to `com.pambrose.gradle-plugins`.
- Refactored package name variable for clarity.

## [1.0.0] - 2026-02-17

### Added
- Initial Gradle plugin project structure with multiple plugins and configuration files.
- Project renamed from `common-gradle` to `gradle-plugins`.

[1.0.15]: https://github.com/pambrose/pambrose-gradle-plugins/compare/1.0.14...1.0.15
[1.0.14]: https://github.com/pambrose/pambrose-gradle-plugins/compare/1.0.13...1.0.14
[1.0.13]: https://github.com/pambrose/pambrose-gradle-plugins/compare/1.0.12...1.0.13
[1.0.12]: https://github.com/pambrose/pambrose-gradle-plugins/compare/1.0.11...1.0.12
[1.0.11]: https://github.com/pambrose/pambrose-gradle-plugins/compare/1.0.10...1.0.11
[1.0.10]: https://github.com/pambrose/pambrose-gradle-plugins/compare/1.0.9...1.0.10
[1.0.9]: https://github.com/pambrose/pambrose-gradle-plugins/compare/1.0.8...1.0.9
[1.0.8]: https://github.com/pambrose/pambrose-gradle-plugins/compare/1.0.7...1.0.8
[1.0.7]: https://github.com/pambrose/pambrose-gradle-plugins/compare/1.0.6...1.0.7
[1.0.6]: https://github.com/pambrose/pambrose-gradle-plugins/compare/1.0.5...1.0.6
[1.0.5]: https://github.com/pambrose/pambrose-gradle-plugins/compare/1.0.4...1.0.5
[1.0.4]: https://github.com/pambrose/pambrose-gradle-plugins/compare/1.0.3...1.0.4
[1.0.3]: https://github.com/pambrose/pambrose-gradle-plugins/compare/1.0.2...1.0.3
[1.0.2]: https://github.com/pambrose/pambrose-gradle-plugins/compare/1.0.1...1.0.2
[1.0.1]: https://github.com/pambrose/pambrose-gradle-plugins/compare/1.0.0...1.0.1
[1.0.0]: https://github.com/pambrose/pambrose-gradle-plugins/releases/tag/1.0.0
