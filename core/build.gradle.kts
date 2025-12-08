val groupId = "dev.kmpx"
val artifactId = "core"
val version = "0.1.0-SNAPSHOT"

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.vanniktech.mavenPublish)
}

repositories {
    mavenCentral()
}

this@Project.group = groupId
this@Project.version = version

kotlin {
    jvm()

    // Java 21 is the most recent LTS version
    jvmToolchain(21)

    js {
        browser()
        nodejs()
    }

    sourceSets {
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(
        groupId = groupId,
        artifactId = artifactId,
        version = version,
    )

    pom {
        name = "KMPX Core"
        description = "Utility libraries for Kotlin Multiplatform"
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
