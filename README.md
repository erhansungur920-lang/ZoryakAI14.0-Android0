# Zoryak AI 14.0 Android starter project

This is a minimal Android Studio project that builds a debug APK using GitHub Actions.

## Build

Open in Android Studio, or run `gradle assembleDebug` with Gradle 8.9 and JDK 17.

GitHub Actions runs on pushes to `main`/`master` and manually from the Actions tab. Download the APK from the workflow run's Artifacts section.

## Scope

This starter project provides a basic chat UI only. It does not yet connect to Ollama or implement real email verification, image generation, or voice features. Those require integration work and service configuration.
