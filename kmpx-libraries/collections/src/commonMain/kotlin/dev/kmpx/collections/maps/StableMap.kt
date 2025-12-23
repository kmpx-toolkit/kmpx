package dev.kmpx.collections.maps

/**
 * A map providing stable handles to its entries.
 *
 * Functions in this interface support only read-only access to the map; read/write access is supported through the
 * [MutableStableMap] interface.
 *
 * @param K the type of map keys
 * @param V the type of map values
 */
interface StableMap<K, out V> : Map<K, V> {
    interface EntryHandle<K, out V> {
        /**
         * The key of the entry corresponding to this handle. The entry's key is immutable.
         */
        val key: K
    }

    /**
     * Returns a handle to the entry corresponding to the given key. The handle is valid right after being resolved.
     *
     * Guarantees linear time complexity or better.
     *
     * @return the handle to the entry corresponding to the key, or null if there is no such entry
     */
    fun resolve(
        key: K,
    ): EntryHandle<K, @UnsafeVariance V>?

    /**
     * @return the value corresponding to the given [entryHandle], or null if the entry has been removed
     */
    fun getValueVia(
        entryHandle: EntryHandle<K, @UnsafeVariance V>,
    ): V?
}
