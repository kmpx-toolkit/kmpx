import dev.kmpx.gradle.kotlin.dsl.utils.ExperimentalLanguageFeature
import dev.kmpx.gradle.kotlin.dsl.utils.experimentalLanguageFeatures
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

private const val usedJvmToolchainVersion = 21

fun KotlinMultiplatformExtension.configureKotlin() {
    jvm()

    jvmToolchain(usedJvmToolchainVersion)

    js(IR) {
        browser()
        nodejs()
    }

    compilerOptions {
        experimentalLanguageFeatures.addAll(
            ExperimentalLanguageFeature.ConsistentDataClassCopyVisibility,
        )
    }
}
