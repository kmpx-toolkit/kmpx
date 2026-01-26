package dev.kmpx.collections.lists

/**
 * A read-only sorted list, i.e. a list that maintains its elements in a sorted order (in accordance with a specified
 * comparator).
 *
 * Functions in this interface support only read-only access to the list; read/write access is supported through the
 * [MutableSortedList] interface.
 *
 * @param E the type of elements contained in the collection
 */
interface SortedList<E> : List<E> {
    /**
     * Returns the index at which the specified [newElement] would be added to maintain the sorted order of the list.
     *
     * Note that this method doesn't directly correspond to [indexOf] (inherited from [List]), which always returns the
     * index of _the first instance_ of a given element within the list.
     */
    fun predictIndexOf(newElement: E): Int
}
