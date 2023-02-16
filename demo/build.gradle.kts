@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
	id("com.android.application")
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlinter)
	alias(libs.plugins.kotlin.serialization)
}

android {
	namespace = "com.kiwi.navigationcompose.typed.demo"

	compileSdk = libs.versions.compileSdk.get().toInt()

	defaultConfig {
		applicationId = "com.kiwi.navigationcompose.typed.demo"
		minSdk = 26
		targetSdk = 33
		versionName = "1.0.0"
		versionCode = 1
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildFeatures {
		compose = true
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}

	composeOptions {
		kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
	}

	buildFeatures {
		compose = true
		buildConfig = false
		aidl = false
		renderScript = false
		resValues = false
		shaders = false
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
}

dependencies {
	implementation(projects.core)

	implementation(libs.kotlin.stdlib)
	implementation(libs.kotlin.serialization)
	implementation(libs.compose.material3)
	implementation(libs.compose.navigation)
	implementation(libs.accompanist.systemController)
	implementation(libs.androidx.appcompat)
}
