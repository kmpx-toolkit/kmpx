package dev.kmpx.collections.maps

import dev.kmpx.collections.SortedCollections.RankKind
import dev.kmpx.collections.SortedCollections.RankResult
import dev.kmpx.collections.sets.SortedSet

/**
 * A [Map] that maintains its entries sorted by their keys. The sorting is determined by the natural ordering of the
 * keys or by a specified comparator.
 */
interface SortedMap<K : Any, out V> : Map<K, V> {
    /**
     * Returns the entry with the greatest key that is less than or equal to the given key, or `null` if there is no
     * such entry. The returned entry may have a key equal to the given key if it is present in the map.
     */
    fun floorEntry(key: K): Map.Entry<K, V>?

    /**
     * Returns the entry with the least key that is greater than or equal to the given key, or `null` if there is no
     * such entry. The returned entry may have a key equal to the given key if it is present in the map.
     */
    fun ceilingEntry(key: K): Map.Entry<K, V>?

    /**
     * Returns the entry at the given rank in the sorted map, or `null` if the rank is out of bounds. The entry with
     * the smallest key has rank 0.
     */
    fun selectEntry(entryRank: Int): Map.Entry<K, V>?

    /**
     * Finds the rank of the given key in the sorted map. If an entry with this key is present in the map, returns its
     * rank with [RankKind.Existing]. If the key is not present, returns the rank where an entry with this key would
     * be inserted with [RankKind.Potential].
     */
    fun findKeyRank(key: K): RankResult

    /**
     * Returns a list of the entries in the sorted map, in sorted order by key. The first entry of the list has the
     * smallest key, and the last entry has the largest key.
     */
    val sortedEntries: SortedSet<Map.Entry<K, @UnsafeVariance V>>

    /**
     * Returns the keys as a [SortedSet], ordered in the same way as the keys in the sorted map.
     */
    val sortedKeys: SortedSet<K>

    /**
     * Returns a list of the values in the sorted map, ordered by their corresponding keys. The first value corresponds
     * to the smallest key, and the last value corresponds to the largest key.
     */
    val sortedValues: List<V>
}
