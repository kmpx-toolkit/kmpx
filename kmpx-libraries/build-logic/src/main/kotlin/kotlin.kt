import dev.kmpx.gradle.kotlin.dsl.utils.ExperimentalLanguageFeature
import dev.kmpx.gradle.kotlin.dsl.utils.experimentalLanguageFeatures
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsSubTargetDsl
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

private const val usedJvmToolchainVersion = 21

val testTimeoutDuration: Duration = 10.seconds

fun KotlinMultiplatformExtension.configureKotlin() {
    jvm()

    jvmToolchain(usedJvmToolchainVersion)

    js(IR) {
        browser {
            testTask(
                timeoutDuration = testTimeoutDuration,
            )
        }

        nodejs {
            testTask(
                timeoutDuration = testTimeoutDuration,
            )
        }
    }

    compilerOptions {
        experimentalLanguageFeatures.addAll(
            ExperimentalLanguageFeature.ConsistentDataClassCopyVisibility,
            ExperimentalLanguageFeature.ExpectActualClasses,
        )
    }
}

private fun KotlinJsSubTargetDsl.testTask(
    timeoutDuration: Duration,
) {
    testTask {
        useMocha {
            timeout = "${timeoutDuration.inWholeSeconds}s"
        }
    }
}
