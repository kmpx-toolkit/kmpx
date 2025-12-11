plugins {
    `kotlin-dsl`
    alias(libs.plugins.vanniktech.mavenPublish)
}

val version = "0.1.1-SNAPSHOT"

this@Project.group = groupId
this@Project.version = version

repositories {
    repositoriesDefaults()
}

kotlin {
    kotlinDefaults()
}

dependencies {
    api(gradleApi())
}

mavenPublishing {
    mavenPublishingDefaults(
        artifactId = "gradle-core-api-utils",
        version = version,
        name = "Core Gradle API utils",
        description = "Utilities for core Gradle API",
        inceptionYear = 2025,
    )
}
