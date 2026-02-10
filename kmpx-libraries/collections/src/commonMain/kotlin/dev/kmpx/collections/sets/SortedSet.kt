package dev.kmpx.collections.sets

import dev.kmpx.collections.SortedCollections.RankKind
import dev.kmpx.collections.SortedCollections.RankResult

/**
 * A [Set] that maintains its elements in a sorted order. The elements are sorted according to their natural ordering or
 * by a specified comparator.
 */
interface SortedSet<E : Any> : Set<E> {
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
     * with [RankKind.Existing]. If the element is not present, returns the rank where it would be inserted with
     * [RankKind.Potential].
     */
    fun findRank(
        element: E,
    ): RankResult

    /**
     * Returns the elements as a list, in sorted order. The smallest element is first, and the largest element is last.
     */
    val asList: List<E>
}
