plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.vanniktech.mavenPublish)
}

val version = "0.1.0-SNAPSHOT"

this@Project.group = groupId
this@Project.version = version

repositories {
    configureRepositories()
}

kotlin {
    configureKotlin()

    sourceSets {
        jsMain.dependencies {
            implementation(project(":js-api-compat"))
        }
    }
}

mavenPublishing {
    configureMavenPublishing(
        artifactId = "platform",
        version = version,
        name = "KMPX Platform Abstractions",
        description = "Platform abstractions supplementing the Kotlin Standard Library",
        inceptionYear = 2025,
    )
}
