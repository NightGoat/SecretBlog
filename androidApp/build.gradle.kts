plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 33
    defaultConfig {
        applicationId = "ru.nightgoat.secretblog.android"
        minSdk = 27
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(kotlin("stdlib-jdk8", config.Libs.kotlin_version))
    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(config.Libs.Compose.ui)
    implementation(config.Libs.Compose.ui_tooling)
    implementation(config.Libs.Compose.activity)
    implementation(config.Libs.Compose.viewmodel)
    implementation(config.Libs.Compose.foundation)
    implementation(config.Libs.Compose.material)
    implementation(config.Libs.Compose.navigation)
    implementation(config.Libs.Koin.koin_android)
    implementation(config.Libs.Core.coroutines_android)
}