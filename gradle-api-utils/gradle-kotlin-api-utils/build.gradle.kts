plugins {
    `kotlin-dsl`
    alias(libs.plugins.vanniktech.mavenPublish)
}

val version = "0.1.0"

this@Project.group = groupId
this@Project.version = version

repositories {
    repositoriesDefaults()
}

kotlin {
    kotlinDefaults()
}

dependencies {
    api(libs.kotlin.gradlePlugin.api)
}

mavenPublishing {
    mavenPublishingDefaults(
        artifactId = "gradle-kotlin-api-utils",
        version = version,
        name = "Kotlin Gradle API utils",
        description = "Utilities for Kotlin-related Gradle API",
        inceptionYear = 2025,
    )
}
