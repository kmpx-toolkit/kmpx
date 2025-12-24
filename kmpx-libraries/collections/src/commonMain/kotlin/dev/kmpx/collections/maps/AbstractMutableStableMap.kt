package dev.kmpx.collections.maps

abstract class AbstractMutableStableMap<K, V>() : AbstractMutableMap<K, V>(), MutableStableMap<K, V> {
    final override fun isEmpty(): Boolean = size == 0

    final override fun get(key: K): V? {
        val entryHandle = resolve(key = key) ?: return null

        return getValueVia(entryHandle = entryHandle)
    }

    abstract override val size: Int
}
