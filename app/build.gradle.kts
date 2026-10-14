plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.zoryak.ai"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.zoryak.ai"
        minSdk = 23
        targetSdk = 35
        versionCode = 14
        versionName = "14.0"
    }
}
