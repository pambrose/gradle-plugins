# Release Notes

Narrative notes for each released version. For a compact log of all changes,
see [CHANGELOG.md](CHANGELOG.md).

---

## v1.1.0 — 2026-07-02

Drops the stable-versions plugin and refreshes the toolchain.

**Highlights**

- Removed the `com.pambrose.stable-versions` plugin (`StableVersionsPlugin`)
  and its test. The plugin applied the ben-manes `versions` plugin and
  filtered RC/beta/alpha/milestone candidates out of `dependencyUpdates`;
  it is no longer published. The now-unused `gradle-versions-plugin`
  dependency was dropped from the build as well.
- Gradle wrapper bumped to 9.6.1, Kotest to 6.2.1, and the vanniktech
  `maven-publish` plugin to 0.37.0.

**Upgrade notes**

- This is a **breaking change**. If your build applies
  `com.pambrose.stable-versions`, remove it. To keep filtering non-stable
  dependency updates, apply `com.github.ben-manes.versions` directly and
  reject non-stable candidates in your own `dependencyUpdates`
  configuration.

---

## v1.0.15 — 2026-06-05

Toolchain refresh, build-config consolidation, and louder test failures.

**Highlights**

- `TestingPlugin` now includes `TestLogEvent.STANDARD_ERROR` in its default
  test logging events (#19). With `showStandardStreams = false` Gradle keeps
  per-test capture quiet, but stderr written during a failing test is now
  routed into the build log so failures are easier to diagnose. The root
  build's own test logging mirrors the same event.
- Kotlin bumped to 2.4.0 and the Gradle wrapper to 9.5.1. Kotest bumped to
  6.1.11 and Kotlinter to 5.5.0.
- Versions consolidated into `gradle/libs.versions.toml` and the build /
  Makefile configuration cleaned up (#18).
- The `gradle` key in `libs.versions.toml` was renamed to `gradle-wrapper`
  so its purpose (driving `make upgrade-wrapper`) is obvious; the Makefile
  was updated to read the new key.

**Makefile cleanup**

- `.PHONY` now declares the helper targets (`_check-gpg-env`,
  `_require-version`, `_require-gradle-version`) and drops the stale
  `depends` entry.
- All publish targets now depend on `_require-version`, so a missing
  `version=` in `gradle.properties` fails fast instead of silently
  publishing a bogus `…-SNAPSHOT` artifact.

---

## v1.0.14 — 2026-04-22

More batteries-included defaults for Kotlin projects using `TestingPlugin`.

**Highlights**

- When the `org.jetbrains.kotlin.jvm` plugin is applied, `TestingPlugin` now
  adds `io.kotest:kotest-runner-junit5` and `org.jetbrains.kotlin:kotlin-test`
  to `testImplementation` automatically. Consumers get a working Kotest +
  kotlin-test setup without having to wire either dependency themselves.
- `kotlin-test` is declared without a version so it resolves to the version
  supplied by the Kotlin plugin already applied to the project.
- The Kotest version is sourced from `gradle/libs.versions.toml` via the
  generated `com.pambrose.BuildConfig` object (new `DEFAULT_KOTEST_VERSION`
  constant), so `dependencyUpdates` continues to track it.
- Both defaults are opt-out via the `pambroseTesting` extension:
  `addKotest`, `kotestVersion`, and `addKotlinTest`.

**Upgrade notes**

- If your Kotlin project already declares `kotest-runner-junit5` or
  `kotlin-test` explicitly, either remove those declarations or disable
  the defaults:

  ```kotlin
  pambroseTesting {
    addKotest.set(false)
    addKotlinTest.set(false)
  }
  ```

---

## v1.0.13 — 2026-04-22

Convenience and tooling improvements for test setup and documentation.

**Highlights**

- `TestingPlugin` now adds `ch.qos.logback:logback-classic` to `testRuntimeOnly`
  automatically, so consumers get a working SLF4J backend for tests without
  extra configuration.
- The logback version is sourced from `gradle/libs.versions.toml` via a
  generated `com.pambrose.BuildConfig` object, keeping it visible to
  `dependencyUpdates` while still usable from plugin code at runtime.
- Dokka documentation and a GitHub Actions workflow have been added (#15).
- README polished with badges and an updated title (#14).
- Duplicate publication warning resolved and JVM memory raised for the build (#13).
- Signing is now functional for local publishing, and the Makefile extracts a
  shared `GPG_ENV` block (#12).

---

## v1.0.12 — 2026-04-03

Major distribution change: this release moves publishing from JitPack to
Maven Central.

**Highlights**

- Published through the vanniktech `maven-publish` plugin to Maven Central.
- Dokka-generated javadoc JAR and GPG signing are wired into the release flow.
- `ReposPlugin` and `SnapshotPlugin` have been removed along with their tests —
  they were tied to the old distribution model.

**Upgrade notes**

- Consumers should resolve the plugins from Maven Central. See the README for
  the updated coordinates and `pluginManagement` snippet.
- If you were relying on `ReposPlugin` or `SnapshotPlugin`, configure the
  equivalent repositories directly in your own build.

---

## v1.0.11 — 2026-03-30

Toolchain refresh.

- Gradle wrapper bumped to 9.4.1.
- Kotest bumped to 6.1.10.

---

## v1.0.10 — 2026-03-15

Toolchain refresh.

- Gradle wrapper bumped to 9.4.0.
- Kotest bumped to 6.1.7.

---

## v1.0.9 — 2026-03-04

Version and documentation housekeeping release; no behavioral changes.

---

## v1.0.8 — 2026-03-01

Quality pass across the plugins with small but meaningful fixes.

**Highlights**

- `TestingPlugin` now sets `showStandardStreams = false` explicitly, with a
  test to lock the behavior in.
- `EnvVarPlugin` trims keys and values after splitting on `=`, so stray
  whitespace in `.env` files no longer leaks into process environments.
- `StableVersionsPlugin.isNonStable` simplified (removed double negation).
- `PublishingPlugin` no longer uses an unnecessary `afterEvaluate`.
- `SnapshotPlugin` switched to `configurations.configureEach` for lazy,
  correct iteration.
- Makefile derives `VERSION` from `build.gradle.kts` rather than hardcoding.
- Kotest bumped to 6.1.4.
- Added `CODE_REVIEW.md` and `llms.txt`; filled in missing `ReposPlugin` and
  `KotlinterPlugin` entries in `CLAUDE.md`.

**Fixed**

- "testinging" typo in README plugin IDs and examples.

---

## v1.0.7 — 2026-02-24

Small test-logging tweak.

- Standard output streams are now disabled in the test logging configuration
  for a quieter default.

---

## v1.0.6 — 2026-02-21

Minor housekeeping release.

- Added build-trigger and view commands to the Makefile.
- Cleaned up test logging configuration in `TestingPlugin`.
- Assorted formatting fixes in `misc.xml`.

---

## v1.0.5 — 2026-02-21

Environment variable ergonomics and licensing.

**Highlights**

- New `vars` property on `EnvVarExtension` exposes the resolved environment
  map for programmatic access.
- Environment variables are applied to both `JavaExec` and `Test` tasks.
- Added the Apache 2.0 license file.
- Project renamed to `pambrose-gradle-plugins` for clarity.

---

## v1.0.4 — 2026-02-19

`EnvVarPlugin` becomes configurable.

- New `envvar` extension lets consumers customize the path of the env var file.
- Dependency-updates configuration enhanced.

---

## v1.0.3 — 2026-02-18

Configuration-cache hygiene.

- Dependency updates plugin marked as incompatible with the configuration
  cache to avoid misleading failures.

---

## v1.0.2 — 2026-02-18

New plugin plus catalog adoption.

**Highlights**

- New `TestingPlugin` for JUnit Platform configuration.
- Renamed `ExcludeBetasPlugin` → `StableVersionsPlugin` (more descriptive).
- Migrated dependency management to the Gradle version catalog.

---

## v1.0.1 — 2026-02-17

First iteration after the initial release; rounds out the plugin set.

**Highlights**

- Added `ReposPlugin` for repository management.
- Added `KotlinterPlugin` to apply kotlinter with sensible reporters.
- Integrated the Java Gradle Plugin.
- README updated with Maven Central usage instructions.
- Group ID updated to `com.pambrose.gradle-plugins`.

---

## v1.0.0 — 2026-02-17

Initial public release.

- First publication of the multi-plugin convention plugin project.
- Project renamed from `common-gradle` to `gradle-plugins`.
- Initial plugin set and project scaffolding in place.
