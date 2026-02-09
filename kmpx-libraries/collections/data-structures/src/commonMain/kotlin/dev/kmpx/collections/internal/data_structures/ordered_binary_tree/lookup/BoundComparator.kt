package dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup

/**
 * Provides a comparison function for imposing a total ordering between an element of the type [T] and another implicit
 * element of this type, bound to the comparator. If the defined order is not a strict order (the comparison depends
 * only on part of the element's data), the implicit bound element may be internally represented just by the data used
 * for comparison.
 */
interface BoundComparator<T> {
    companion object {
        fun <T : Comparable<T>> compareTo(boundElement: T): BoundComparator<T> = object : BoundComparator<T> {
            override fun compare(a: T): Int = a.compareTo(boundElement)
        }

        fun <T, K : Comparable<K>> compareBy(
            boundKey: K,
            keySelector: (T) -> K,
        ): BoundComparator<T> = object : BoundComparator<T> {
            override fun compare(a: T): Int = keySelector(a).compareTo(boundKey)
        }
    }

    /**
     * Compares the given element for order with the bound one. Returns zero if the elements are equal, a negative
     * number if the given argument is less than the bound one, or a positive number if the bound element is greater
     * than the given element.
     */
    fun compare(a: T): Int
}

fun <T> Comparator<T>.bind(b: T): BoundComparator<T> {
    return object : BoundComparator<T> {
        override fun compare(a: T): Int = this@bind.compare(a, b)
    }
}
