package dev.kmpx.collections.lists

import dev.kmpx.collections.StableCollection
import dev.kmpx.collections.StableCollection.Handle

/**
 * A list providing stable handles to its elements.
 *
 * Functions in this interface support only read-only access to the list; read/write access is supported through the
 * [MutableStableList] interface.
 *
 * @param E the type of elements contained in the collection
 */
interface StableList<out E> : StableCollection<E>, List<E> {
    /**
     * Returns a handle to the first instance of the given [element] in the list.
     *
     * Guarantees linear time complexity or better.
     *
     * @return the handle to the element or `null` if the list does not contain such element
     */
    fun findEx(
        element: @UnsafeVariance E,
    ): Handle<@UnsafeVariance E>?

    /**
     * Returns the handle to the element at the specified [index] in the list.
     *
     * Guarantees linear time complexity or better.
     *
     * @return the handle to the element or `null` if the index is out of bounds
     */
    fun resolveAt(
        index: Int,
    ): Handle<@UnsafeVariance E>?

    /**
     * Returns the index of the element corresponding to the given handle in the list.
     *
     * Guarantees linear time complexity or better.
     *
     * @return the index of the element or null if the corresponding element has already been removed
     */
    fun indexOfVia(
        handle: Handle<@UnsafeVariance E>,
    ): Int?
}
