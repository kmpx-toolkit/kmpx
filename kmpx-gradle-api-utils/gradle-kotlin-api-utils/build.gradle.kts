plugins {
    `kotlin-dsl`
    alias(libs.plugins.vanniktech.mavenPublish)
}

val version = "0.1.1-SNAPSHOT"

this@Project.group = groupId
this@Project.version = version

repositories {
    configureRepositories()
}

kotlin {
    configureKotlin()
}

dependencies {
    api(libs.kotlin.gradlePlugin.api)
}

mavenPublishing {
    configureMavenPublishing(
        artifactId = "gradle-kotlin-api-utils",
        version = version,
        name = "Kotlin Gradle API utils",
        description = "Utilities for Kotlin-related Gradle API",
        inceptionYear = 2025,
    )
}
