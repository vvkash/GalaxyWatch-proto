plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.uncc.galaxyheart.core"
    compileSdk = 35

    defaultConfig {
        minSdk = 30
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Exposed (api) so every app module inherits the same Compose/Wear stack.
    val composeBom = platform("androidx.compose:compose-bom:2024.12.01")
    api(composeBom)
    api("androidx.core:core-ktx:1.13.1")
    api("androidx.activity:activity-compose:1.9.3")
    api("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    api("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    api("androidx.compose.ui:ui")
    api("androidx.compose.ui:ui-tooling-preview")
    api("androidx.wear.compose:compose-material:1.4.1")
    api("androidx.wear.compose:compose-foundation:1.4.1")
}
