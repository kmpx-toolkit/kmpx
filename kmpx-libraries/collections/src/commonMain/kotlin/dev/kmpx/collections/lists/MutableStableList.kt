package dev.kmpx.collections.lists

import dev.kmpx.collections.MutableStableCollection
import dev.kmpx.collections.StableCollection.Handle

/**
 * A mutable list providing stable handles to its elements.
 *
 * @param E the type of elements contained in the collection
 */
interface MutableStableList<E> : MutableList<E>, MutableStableCollection<E>, StableList<E> {
    /**
     * Inserts an element into the list at the specified [index] in exchange for a handle.
     *
     * @return the handle to the added element.
     */
    fun insertAt(
        index: Int,
        element: E,
    ): Handle<E>

    /**
     * Replaces the element corresponding to the given handle with the specified element. Doesn't invalidate the handle.
     *
     * Guarantees constant time complexity.
     *
     * @return the element previously at the specified position.
     */
    fun setVia(
        handle: Handle<E>,
        element: E,
    ): E?
}

@Suppress("NOTHING_TO_INLINE")
inline fun <E> mutableStableListOf(
    vararg elements: E,
): MutableStableList<E> = treeListOf(*elements)
