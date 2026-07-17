#!/bin/sh
# Run this once after cloning if gradle/wrapper/gradle-wrapper.jar is missing.
# Requires any local Gradle install (brew install gradle / sdk install gradle / apt install gradle),
# or open the project in Android Studio and use its bundled Gradle instead — see README.md.
set -e
gradle wrapper --gradle-version 8.7
chmod +x gradlew
echo "Gradle wrapper regenerated. You can now use ./gradlew as normal."
