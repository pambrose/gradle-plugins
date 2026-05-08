.PHONY: default stop clean build tests tree depends refresh kdocs versioncheck \
        publish-local publish-local-snapshot publish-snapshot publish-maven-central upgrade-wrapper

VERSION=$(shell grep -E '^version' build.gradle.kts | head -1 | grep -oE '"[^"]+"$$' | tr -d '"')
GRADLE_VERSION=$(shell grep '^gradle =' gradle/libs.versions.toml | sed 's/.*"\(.*\)"/\1/')

default: versioncheck

stop:
	./gradlew --stop

clean:
	./gradlew clean

# Forces a full rebuild and skips tests; run `make tests` separately.
build: clean
	./gradlew build -xtest

tests:
	./gradlew test

tree:
	./gradlew -q dependencies

refresh:
	./gradlew --refresh-dependencies

kdocs:
	./gradlew dokkaGeneratePublicationHtml

versioncheck:
	./gradlew dependencyUpdates --no-configuration-cache

publish-local:
	./gradlew publishToMavenLocal

publish-local-snapshot:
	./gradlew -PoverrideVersion=$(VERSION)-SNAPSHOT publishToMavenLocal

GPG_ENV = \
	ORG_GRADLE_PROJECT_signingInMemoryKey="$$(gpg --armor --export-secret-keys $$GPG_SIGNING_KEY_ID)" \
	ORG_GRADLE_PROJECT_signingInMemoryKeyId="$$GPG_SIGNING_KEY_ID" \
	ORG_GRADLE_PROJECT_signingInMemoryKeyPassword=$$(security find-generic-password -a "gpg-signing" -s "gradle-signing-password" -w)

check-gpg-env:
	@if [ -z "$$GPG_SIGNING_KEY_ID" ]; then \
		echo "Error: GPG_SIGNING_KEY_ID is not set" >&2; exit 1; \
	fi
	@if ! gpg --list-secret-keys "$$GPG_SIGNING_KEY_ID" >/dev/null 2>&1; then \
		echo "Error: no GPG secret key found for GPG_SIGNING_KEY_ID=$$GPG_SIGNING_KEY_ID" >&2; exit 1; \
	fi
	@if ! security find-generic-password -a "gpg-signing" -s "gradle-signing-password" -w >/dev/null 2>&1; then \
		echo "Error: keychain entry 'gradle-signing-password' (account 'gpg-signing') not found" >&2; exit 1; \
	fi

publish-snapshot: check-gpg-env
	$(GPG_ENV) ./gradlew -PoverrideVersion=$(VERSION)-SNAPSHOT publishToMavenCentral

publish-maven-central: check-gpg-env
	$(GPG_ENV) ./gradlew publishAndReleaseToMavenCentral

upgrade-wrapper:
	./gradlew wrapper --gradle-version=$(GRADLE_VERSION) --distribution-type=bin
	./gradlew wrapper --gradle-version=$(GRADLE_VERSION) --distribution-type=bin
