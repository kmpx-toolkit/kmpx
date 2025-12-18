package dev.kmpx.collections.internal.utils

/**
 * Asserts that a condition is true. If it is not, an [AssertionError] is thrown with the given [message].
 *
 * This utility is meant for verifying internal invariants.
 */
inline fun assert(
    condition: Boolean,
    message: () -> String,
) {
    if (!condition) {
        throw AssertionError(message())
    }
}

/**
 * Throws an [AssertionError] with the given [message].
 *
 * This utility is meant to be called when an internal invariant is known to be broken.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun fail(
    message: String,
): Nothing {
    throw AssertionError(message)
}
