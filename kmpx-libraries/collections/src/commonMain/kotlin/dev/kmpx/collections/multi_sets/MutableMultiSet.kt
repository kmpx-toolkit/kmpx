package dev.kmpx.collections.multi_sets

/**
 * A mutable collection that counts the number of times an object appears in the collection.
 *
 * @param E the type of elements contained in the collection
 */
interface MutableMultiSet<E> : MultiSet<E>, MutableCollection<E> {
    /**
     * Adds a number of occurrences of the specified element to the [MultiSet]. If the element is already in the
     * multi-set, then increments its count as reported by [getCount].
     *
     * @param element the element to add
     * @param count the number of occurrences to add, may be zero, in which case no change is made to the multi-set
     * @return the number of occurrences of the element before this operation (possibly zero)
     */
    fun add(
        element: E,
        count: Int,
    ): Int
}

fun <E> hashMultiSetOf(
    vararg elements: E,
): MutableMultiSet<E> = HashMultiSet.of(elements)

fun <E> mutableMultiSetOf(
    vararg elements: E,
): MutableMultiSet<E> = HashMultiSet.of(elements)
