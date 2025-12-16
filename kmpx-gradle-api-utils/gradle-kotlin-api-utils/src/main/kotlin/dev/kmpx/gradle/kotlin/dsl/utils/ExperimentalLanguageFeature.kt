package dev.kmpx.gradle.kotlin.dsl.utils

enum class ExperimentalLanguageFeature(
    val id: String,
) {
    /**
     * Consistent `copy` visibility
     *
     * https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-consistent-copy-visibility/
     */
    ConsistentDataClassCopyVisibility("consistent-data-class-copy-visibility"),

    /**
     * Expected and actual declarations
     *
     * https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-expect-actual.html
     */
    ExpectActualClasses("expect-actual-classes"),

    /**
     * Context parameters
     *
     * https://kotlinlang.org/docs/context-parameters.html
     */
    ContextParameters("context-parameters"),

    /**
     * Nested type aliases
     *
     * https://kotlinlang.org/docs/type-aliases.html#nested-type-aliases
     */
    NestedTypeAliases("nested-type-aliases"),
}

internal fun ExperimentalLanguageFeature.toFlagString(): String = "-X$id"
