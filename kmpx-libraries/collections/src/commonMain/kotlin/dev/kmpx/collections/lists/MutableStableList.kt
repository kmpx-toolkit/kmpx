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
     * Adds the specified element to the list in exchange for a handle by appending it at the end of the list. When
     * working with [MutableStableList] instances, prefer using the [append] method explicitly.
     *
     * Guarantees logarithmic time complexity or better.
     *
     * @return the handle to the added element.
     */
    override fun insert(
        element: E,
    ): Handle<E> = append(element = element)

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
     * Appends an element to the end of the list in exchange for a handle.
     *
     * Guarantees logarithmic time complexity or better.
     *
     * @return the handle to the appended element.
     */
    fun append(
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
