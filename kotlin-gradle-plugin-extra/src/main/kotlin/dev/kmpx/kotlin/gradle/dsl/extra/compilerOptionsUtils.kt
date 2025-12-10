package dev.kmpx.kotlin.gradle.dsl.extra

import dev.kmpx.kotlin.gradle.dsl.extra.internal.TransformingHasMultipleValuesProxy
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions

/**
 * A simple proxy over [KotlinCommonCompilerOptions.optIn].
 */
val KotlinCommonCompilerOptions.optInApis: HasMultipleValuesProxy<StdlibOptInApi>
    get() = object : TransformingHasMultipleValuesProxy<StdlibOptInApi, String>(
        target = optIn,
    ) {
        override fun transform(element: StdlibOptInApi): String = element.id
    }

/**
 * A simple proxy over [KotlinCommonCompilerOptions.freeCompilerArgs] for experimental language features (`-Xsome-feature` flags).
 */
val KotlinCommonCompilerOptions.experimentalLanguageFeatures: HasMultipleValuesProxy<ExperimentalLanguageFeature>
    get() = object : TransformingHasMultipleValuesProxy<ExperimentalLanguageFeature, String>(
        target = freeCompilerArgs,
    ) {
        override fun transform(element: ExperimentalLanguageFeature): String = element.toFlagString()
    }
