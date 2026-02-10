package dev.kmpx.collections.sets

/**
 * A [Set] that maintains its elements in a sorted order. The elements are sorted according to their natural ordering or
 * by a specified comparator.
 */
interface SortedSet<E : Any> : Set<E> {
    enum class ElementRankKind {
        /**
         * Rank of an element contained in the set.
         */
        Existing,

        /**
         * Rank of an element that is not contained in the set, but would be inserted at the returned rank.
         */
        Potential,
    }

    data class ElementRankResult(
        val elementRank: Int,
        val kind: ElementRankKind,
    )

    /**
     * Returns the greatest element in the sorted set that is less than or equal to the given element, or `null` if
     * there is no such element. The returned element may be equal to the given element if it is present in the set.
     */
    fun floor(element: E): E?

    /**
     * Returns the least element in the sorted set that is greater than or equal to the given element, or `null` if
     * there is no such element. The returned element may be equal to the given element if it is present in the set.
     */
    fun ceiling(element: E): E?

    /**
     * Returns the element at the given rank in the sorted set, or `null` if the rank is out of bounds. The smallest
     * element has rank 0.
     */
    fun select(
        rank: Int,
    ): E?

    /**
     * Finds the rank of the given element in the sorted set. If the element is present in the set, returns its rank
     * with [ElementRankKind.Existing]. If the element is not present, returns the rank where it would be inserted with
     * [ElementRankKind.Potential].
     */
    fun findRank(
        element: E,
    ): ElementRankResult

    /**
     * Returns the elements as a list, in sorted order. The smallest element is first, and the largest element is last.
     */
    val asList: List<E>
}
