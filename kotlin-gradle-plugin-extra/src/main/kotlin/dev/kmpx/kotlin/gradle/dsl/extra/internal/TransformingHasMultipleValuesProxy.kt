package dev.kmpx.kotlin.gradle.dsl.extra.internal

import dev.kmpx.kotlin.gradle.dsl.extra.HasMultipleValuesProxy
import org.gradle.api.provider.HasMultipleValues

internal abstract class TransformingHasMultipleValuesProxy<T, R : Any>(
    private val target: HasMultipleValues<R>,
) : HasMultipleValuesProxy<T> {
    final override fun set(elements: Iterable<T>) {
        target.set(
            elements.map { transform(it) },
        )
    }

    final override fun add(element: T) {
        target.add(
            transform(element),
        )
    }

    final override fun addAll(vararg elements: T) {
        target.addAll(
            elements.map { transform(it) },
        )
    }

    final override fun addAll(elements: Iterable<T>) {
        target.addAll(
            elements.map { transform(it) },
        )
    }

    abstract fun transform(element: T): R
}
