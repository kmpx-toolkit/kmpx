package dev.kmpx.collections.lists

/**
 * A read-only sorted list providing stable handles to its elements.
 *
 * Functions in this interface support only read-only access to the list; read/write access is supported through the
 * [MutableStableSortedList] interface.
 *
 * @param E the type of elements contained in the collection
 */
interface StableSortedList<E> : StableList<E>, SortedList<E>
