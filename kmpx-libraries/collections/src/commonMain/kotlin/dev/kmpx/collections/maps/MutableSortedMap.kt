package dev.kmpx.collections.maps

import dev.kmpx.collections.sets.MutableSortedSet

interface MutableSortedMap<K: Any, V> : MutableMap<K, V>, SortedMap<K, V> {
    override fun floorEntry(key: K): MutableMap.MutableEntry<K, V>?

    override fun ceilingEntry(key: K): MutableMap.MutableEntry<K, V>?

    override val sortedKeys: MutableSortedSet<K>
}
