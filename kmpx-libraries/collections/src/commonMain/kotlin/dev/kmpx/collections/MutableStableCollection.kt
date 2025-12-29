package dev.kmpx.collections

import dev.kmpx.collections.StableCollection.Handle

/**
 * A generic mutable collection that provides stable handles to its elements.
 *
 * @param E the type of elements contained in the collection
 */
interface MutableStableCollection<E> : MutableCollection<E>, StableCollection<E> {
    /**
     * Adds the specified element to the collection (if possible) in exchange for a handle.
     *
     * Specific collection implementations might provide constraints (like uniqueness of elements or uniqueness of
     * keys), so the given element might not actually be added to the collection.
     *
     * Guarantees logarithmic time complexity or better.
     *
     * @return the handle to the added element or `null` if the element wasn't added because it broke a constraint
     */
    fun insert(
        element: E,
    ): Handle<E>?

    /**
     * Removes the element corresponding to the given [handle] from the collection.
     *
     * Guarantees logarithmic time complexity or better.
     *
     * @return the element that has been removed.
     */
    fun removeVia(
        handle: Handle<E>,
    ): E
}
