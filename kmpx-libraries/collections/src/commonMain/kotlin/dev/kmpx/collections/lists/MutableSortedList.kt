package dev.kmpx.collections.lists

/**
 * A mutable sorted list.
 *
 * This interface doesn't implement [MutableList], as the order of elements is determined by a specific comparator. It's
 * not possible to insert an element at an arbitrary index.
 *
 * @param E the type of elements contained in the collection
 */
interface MutableSortedList<E> : SortedList<E>, MutableCollection<E> {
    /**
     * Adds the specified element to the list (maintaining the sorted order). If there are already elements that are
     * considered equal to [element] in accordance with the list's comparator, the new element is added after them.
     *
     * If the list wasn't modified since the last call to [predictIndexOf], [element] will be inserted at the index
     * returned by that call.
     *
     * @return `true` because the list is always modified as the result of this operation.
     */
    override fun add(element: E): Boolean

    /**
     * Removes the first instance of the specified element from this list, if the list contains it.
     *
     * @return `true` if the element has been successfully removed; `false` if it was not contained in the list.
     */
    override fun remove(element: E): Boolean
}
