import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

// Java 21 is the most recent LTS version
private const val usedJvmToolchainVersion = 21

fun KotlinJvmProjectExtension.kotlinDefaults() {
    jvmToolchain(usedJvmToolchainVersion)
}
