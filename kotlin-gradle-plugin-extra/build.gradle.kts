plugins {
    `kotlin-dsl`
    alias(libs.plugins.vanniktech.mavenPublish)
}

val pluginGroupId = "dev.kmpx"
val pluginVersion = "0.1.0-SNAPSHOT"

group = pluginGroupId
version = pluginVersion

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(libs.kotlinGradlePlugin)
    implementation(libs.kotlinGradlePlugin.api)
}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(
        groupId = pluginGroupId,
        artifactId = "kotlin-gradle-plugin-extra",
        version = pluginVersion,
    )

    pom {
        name = "Kotlin Gradle Plugin Extra"
        description = "Extra functionality for the Kotlin Gradle Plugin"
        inceptionYear = "2025"
        url = "https://kmpx.dev/"

        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }

        developers {
            developer {
                id = "kmpx"
                name = "KMPX Authors"
                url = "https://kmpx.dev/"
            }
        }

        scm {
            url = "https://github.com/kmpx-toolkit/kmpx/"
            connection = "scm:git:git://github.com/kmpx-toolkit/kmpx.git"
            developerConnection = "scm:git:ssh://git@github.com/kmpx-toolkit/kmpx.git"
        }
    }
}
