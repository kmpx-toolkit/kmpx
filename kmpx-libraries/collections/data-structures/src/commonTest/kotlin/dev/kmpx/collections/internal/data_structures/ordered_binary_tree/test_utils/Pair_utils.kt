package dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils

fun <T : Comparable<T>> Pair<T, T>.sorted(): Pair<T, T> = when {
    first <= second -> this
    else -> Pair(second, first)
}
