package dev.kmpx.collections.maps

import dev.kmpx.collections.maps.StableMap.EntryHandle

/**
 * A mutable map providing stable handles to its elements.
 *
 * @param K the type of map keys
 * @param V the type of map values
 */
interface MutableStableMap<K, V> : MutableMap<K, V>, StableMap<K, V> {
    /**
     * Adds the specified ([key], [value]) entry to the map (if possible) in exchange for a handle.
     *
     * If an entry with the given key is already present in the map, the entry is not added.
     *
     * Guarantees logarithmic time complexity or better.
     *
     * @return the handle to the added entry or `null` if an entry with the given key is already present in the map
     */
    fun addEntryEx(
        key: K,
        value: V,
    ): EntryHandle<K, V>?

    /**
     * Sets a [newValue] for the entry corresponding to the given [entryHandle].
     *
     * @return the previous value associated with the entry, or null if the corresponding entry has been removed earlier
     */
    fun setValueVia(
        entryHandle: EntryHandle<K, V>,
        newValue: V,
    ): V?

    /**
     * Removes the entry corresponding to the given handle from the map.
     *
     * Guarantees logarithmic time complexity or better.
     *
     * @return the element that has been removed, or null if the corresponding entry has been removed earlier
     */
    fun removeEntryVia(
        entryHandle: EntryHandle<K, V>,
    ): V?
}

fun <K : Comparable<K>, V> mutableStableMapOf(
    vararg pairs: Pair<K, V>,
): MutableStableMap<K, V> = mutableTreeMapOf(*pairs)
