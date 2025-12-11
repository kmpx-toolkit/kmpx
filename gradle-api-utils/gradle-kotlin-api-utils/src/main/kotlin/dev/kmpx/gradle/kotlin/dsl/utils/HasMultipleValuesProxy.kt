package dev.kmpx.gradle.kotlin.dsl.utils

/**
 * A simple write-only proxy over [org.gradle.api.provider.HasMultipleValues].
 */
interface HasMultipleValuesProxy<T> {
    fun set(elements: Iterable<T>)

    fun add(element: T)

    fun addAll(vararg elements: T)

    fun addAll(elements: Iterable<T>)
}
