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
}

mavenPublishing {
    configureMavenPublishing(
        artifactId = "platform",
        version = version,
        name = "KMPX JS API Compat",
        description = "JS API compatibility library",
        inceptionYear = 2025,
    )
}
