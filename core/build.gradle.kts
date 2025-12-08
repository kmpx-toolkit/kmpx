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

val configurePublishing = providers.gradleProperty("configurePublishing").orNull

if (configurePublishing == "true") {
    val ghRepository: String =
        System.getenv("GITHUB_REPOSITORY") ?: throw IllegalStateException("GITHUB_REPOSITORY is not set")
    val ghActor = System.getenv("GITHUB_ACTOR") ?: throw IllegalStateException("GITHUB_ACTOR is not set")
    val ghToken = System.getenv("GITHUB_TOKEN") ?: throw IllegalStateException("GITHUB_TOKEN is not set")

    publishing {
        publications {
            withType<MavenPublication> {
                pom {
                    name.set("KMPX Core")
                    description.set("Utility libraries for Kotlin Multiplatform")
                    url.set("https://github.com/$ghRepository")

                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }

                    developers {
                        developer {
                            id.set(ghActor)
                            name.set("KMPX Authors")
                            email.set("contact@kmpx.dev")
                        }
                    }

                    scm {
                        connection.set("scm:git:git://github.com/$ghRepository.git")
                        developerConnection.set("scm:git:ssh://github.com:$ghRepository.git")
                        url.set("https://github.com/$ghRepository")
                    }
                }
            }
        }

        repositories {
            maven {
                name = "GithubPackages"
                url = uri("https://maven.pkg.github.com/${ghRepository}")

                credentials {
                    username = ghActor
                    password = ghToken
                }
            }
        }
    }
}
