package dev.kmpx.collections.sets

import dev.kmpx.collections.StableCollection
import dev.kmpx.collections.StableCollection.Handle

/**
 * A read-only set providing stable handles to its elements.
 *
 * @param E the type of elements contained in the collection
 */
interface StableSet<out E> : Set<E>, StableCollection<E> {
    /**
     * Returns a handle to the given [element].
     * Guarantees logarithmic time complexity or better.
     *
     * @return the handle to the element or `null` if the set does not contain this element
     */
    fun lookup(
        element: @UnsafeVariance E,
    ): Handle<@UnsafeVariance E>?
}
