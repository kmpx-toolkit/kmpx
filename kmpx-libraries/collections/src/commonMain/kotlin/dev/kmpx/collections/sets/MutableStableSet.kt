package dev.kmpx.collections.sets

import dev.kmpx.collections.MutableStableCollection

/**
 * A mutable set providing stable handles to its elements.
 *
 * @param E the type of elements contained in the collection
 */
interface MutableStableSet<E> : MutableStableCollection<E>, MutableSet<E>, StableSet<E>
