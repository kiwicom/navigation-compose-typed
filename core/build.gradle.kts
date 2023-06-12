@file:Suppress("UnstableApiUsage")

plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
	id("org.jetbrains.kotlin.plugin.serialization")
	id("org.jetbrains.kotlinx.binary-compatibility-validator")
	id("com.vanniktech.maven.publish.base")
	id("org.jmailen.kotlinter")
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
		kotlinCompilerExtensionVersion = libs.compose.compiler.get().version
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
	implementation(libs.navigation.compose)

	testImplementation(libs.junit)
	testImplementation(libs.roboletric)
}
