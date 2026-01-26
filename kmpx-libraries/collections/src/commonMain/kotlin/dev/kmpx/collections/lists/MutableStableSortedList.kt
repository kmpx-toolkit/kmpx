package dev.kmpx.collections.lists

import dev.kmpx.collections.MutableStableCollection
import dev.kmpx.collections.StableCollection.Handle

/**
 * A mutable sorted list providing stable handles to its elements.
 *
 * @param E the type of elements contained in the collection
 */
interface MutableStableSortedList<E> : StableList<E>, MutableSortedList<E>, MutableStableCollection<E> {
    /**
     * Adds the specified element to the list (maintaining the sorted order) in exchange for a handle.
     *
     * Guarantees logarithmic time complexity or better.
     *
     * @return the handle to the added element
     */
    override fun insert(
        element: E,
    ): Handle<E>
}
