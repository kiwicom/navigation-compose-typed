@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
	alias(libs.plugins.mavenPublish)
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.kiwi.navigationcompose.typed"

    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
        buildConfig = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs.toMutableList().apply {
            add("-Xexplicit-api=strict")
        }.toList()
    }

    lint {
        abortOnError = true
        warningsAsErrors = true
    }
}

kotlinter {
    reporters = arrayOf("json")
    experimentalRules = true
	disabledRules = arrayOf("filename")
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.serialization)
    implementation(libs.compose.navigation)

    testImplementation(libs.junit)
    testImplementation(libs.roboletric)
}
