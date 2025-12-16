package dev.kmpx.gradle.kotlin.dsl.utils

import dev.kmpx.gradle.kotlin.dsl.utils.internal.TransformingHasMultipleValuesProxy
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions

/**
 * A simple proxy over [KotlinCommonCompilerOptions.optIn] property.
 */
val KotlinCommonCompilerOptions.optInApis: HasMultipleValuesProxy<StdlibOptInApi>
    get() = object : TransformingHasMultipleValuesProxy<StdlibOptInApi, String>(
        target = optIn,
    ) {
        override fun transform(element: StdlibOptInApi): String = element.qualifiedMarkerName
    }

/**
 * A simple proxy over [KotlinCommonCompilerOptions.freeCompilerArgs] property for experimental language features
 * (`-Xsome-feature` flags).
 */
val KotlinCommonCompilerOptions.experimentalLanguageFeatures: HasMultipleValuesProxy<ExperimentalLanguageFeature>
    get() = object : TransformingHasMultipleValuesProxy<ExperimentalLanguageFeature, String>(
        target = freeCompilerArgs,
    ) {
        override fun transform(element: ExperimentalLanguageFeature): String = element.toFlagString()
    }
