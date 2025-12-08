plugins {
    alias(libs.plugins.kotlin.multiplatform)
    `maven-publish`
}

repositories {
    mavenCentral()
}

group = "dev.kmpx"
version = "0.1.0-SNAPSHOT"

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

if (System.getProperty("configurePublishing") == "true") {
    val ghRepository: String =
        System.getenv("GITHUB_REPOSITORY") ?: throw IllegalStateException("GITHUB_REPOSITORY is not set")
    val ghActor = System.getenv("GITHUB_ACTOR") ?: throw IllegalStateException("GITHUB_ACTOR is not set")
    val ghToken = System.getenv("GITHUB_TOKEN") ?: throw IllegalStateException("GITHUB_TOKEN is not set")

    publishing {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/${ghRepository}")

                credentials {
                    username = ghActor
                    password = ghToken
                }
            }
        }
    }
}
