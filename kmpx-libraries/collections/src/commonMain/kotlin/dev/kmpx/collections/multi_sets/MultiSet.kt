package dev.kmpx.collections.multi_sets

/**
 * A read-only collection that counts the number of times an object appears in the collection.
 *
 * Functions in this interface support only read-only access to the multiset; read/write access is supported through the
 * [MutableMultiSet] interface.
 *
 * @param E the type of elements contained in the collection
 */
interface MultiSet<out E> : Collection<E> {
    interface Entry<out E> {
        val element: E
        val count: Int
    }

    companion object {
        fun <E> ofCounts(
            vararg pairs: Pair<E, Int>,
        ): MultiSet<E> = mutableMultiSetOf<E>().apply {
            pairs.forEach { (element, count) ->
                add(
                    element = element,
                    count = count,
                )
            }
        }
    }

    val entrySet: Set<Entry<E>>

    fun getCount(element: @UnsafeVariance E): Int
}

fun <E> multiSetOf(
    vararg elements: E,
): MultiSet<E> = HashMultiSet.of(elements)

fun <E> emptyMultiSet(): MutableMultiSet<E> = HashMultiSet.of(emptyList())

fun <E> Collection<E>.toMultiSet(): MultiSet<E> = HashMultiSet.of(this)

/**
 * Returns `true` if this multi-set contains at least as many occurrences of each element as the [other] multi-set.
 */
fun <E> MultiSet<E>.containsWhole(
    other: MultiSet<E>,
): Boolean = other.entrySet.all { otherEntry ->
    val element = otherEntry.element
    val otherCount = otherEntry.count

    val thisCount = getCount(element)

    thisCount >= otherCount
}

operator fun <E> MultiSet<E>.plus(
    other: MultiSet<E>,
): MultiSet<E> {
    val result = mutableMultiSetOf<E>()

    entrySet.forEach { entry ->
        result.add(
            element = entry.element,
            count = entry.count,
        )
    }

    other.entrySet.forEach { entry ->
        result.add(
            element = entry.element,
            count = entry.count,
        )
    }

    return result
}

operator fun <E> MultiSet<E>.minus(
    other: MultiSet<E>,
): MultiSet<E> {
    val result = mutableMultiSetOf<E>()

    entrySet.forEach { entry ->
        val element = entry.element
        val count = entry.count

        val otherCount = other.getCount(element)

        val newCount = count - otherCount

        if (newCount > 0) {
            result.add(
                element = element,
                count = newCount,
            )
        }
    }

    return result
}
