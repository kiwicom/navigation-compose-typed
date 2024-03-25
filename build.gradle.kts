import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SonatypeHost
import java.util.Properties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
	id("org.jetbrains.kotlin.android") version "1.9.22" apply false
	id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22" apply false
	id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.14.0" apply false
	id("org.jmailen.kotlinter") version "4.1.1" apply false
	id("com.android.application") version "8.3.1" apply false
	id("com.vanniktech.maven.publish.base") version "0.27.0" apply false
}

subprojects {
	tasks.withType<KotlinJvmCompile>().configureEach {
		compilerOptions {
			jvmTarget.set(JvmTarget.JVM_1_8)
			allWarningsAsErrors.set(true)
		}
	}

	val signingPropsFile = rootProject.file("release/signing.properties")
	if (signingPropsFile.exists()) {
		val localProperties = Properties()
		with(signingPropsFile.inputStream()) {
			localProperties.load(this)
		}
		localProperties.forEach { key, value ->
			if (key == "signing.secretKeyRingFile") {
				project.ext.set(key as String, rootProject.file(value).absolutePath)
			} else {
				project.ext.set(key as String, value)
			}
		}
	}

	plugins.withId("com.vanniktech.maven.publish.base") {
		@Suppress("UnstableApiUsage")
		configure<MavenPublishBaseExtension> {
			group = requireNotNull(project.findProperty("GROUP"))
			version = requireNotNull(project.findProperty("VERSION_NAME"))
			pomFromGradleProperties()
			publishToMavenCentral(SonatypeHost.S01, true)
			signAllPublications()
			configure(AndroidSingleVariantLibrary())
		}
	}
}
