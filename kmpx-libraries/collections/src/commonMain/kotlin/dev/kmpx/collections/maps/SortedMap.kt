package dev.kmpx.collections.maps

interface SortedMap<K, out V> : Map<K, V> {
    fun floorEntry(key: K): Map.Entry<K, V>?

    fun ceilingEntry(key: K): Map.Entry<K, V>?

    val sortedValues: List<V>
}
