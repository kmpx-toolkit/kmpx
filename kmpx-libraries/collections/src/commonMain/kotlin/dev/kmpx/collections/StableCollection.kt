package dev.kmpx.collections

/**
 * A generic collection providing stable handles to its elements.
 *
 * Functions in this interface support only read-only access to the collection; read/write access is supported through
 * the [MutableStableCollection] interface.
 *
 * All methods in this interface and sub-interfaces that accept handles assume that the handles were obtained from the
 * same collection instance. If a handle from a different collection instance is passed, the behavior is undefined.
 *
 * @param E the type of elements contained in the collection
 */
interface StableCollection<out E> : Collection<E> {
    interface Handle<E>

    /**
     * A sequence of handles to the elements of this collection, in the order defined by the collection (potentially not
     * a meaningful order).
     */
    val handles: Sequence<Handle<@UnsafeVariance E>>

    /**
     * Returns the element corresponding to the given handle.
     *
     * Guarantees constant time complexity.
     *
     * @return the element corresponding to the handle.
     */
    fun getVia(
        handle: Handle<@UnsafeVariance E>,
    ): E
}
