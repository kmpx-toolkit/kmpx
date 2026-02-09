package dev.kmpx.collections.maps

interface MutableSortedMap<K, V> : MutableMap<K, V>, SortedMap<K, V> {
    override fun floorEntry(key: K): MutableMap.MutableEntry<K, V>?

    override fun ceilingEntry(key: K): MutableMap.MutableEntry<K, V>?
}
