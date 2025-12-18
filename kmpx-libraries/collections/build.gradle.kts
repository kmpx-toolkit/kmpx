plugins {
    alias(libs.plugins.kotlin.multiplatform)
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

    sourceSets {
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

mavenPublishing {
    configureMavenPublishing(
        artifactId = "collections",
        version = version,
        name = "KMPX Collections",
        description = "Collection implementations supplementing the Kotlin Standard Library",
        inceptionYear = 2025,
    )
}
