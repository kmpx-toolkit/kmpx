package dev.kmpx.collections.maps

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.insert
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup.BoundComparator
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup.findCeilWith
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup.findExactWith
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup.findFloorWith
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.remove
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.resolve
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.select
import dev.kmpx.collections.internal.iterators.OrderedBinaryTreeIterator
import dev.kmpx.collections.SortedCollections.RankResult
import dev.kmpx.collections.findRank
import dev.kmpx.collections.maps.StableMap.EntryHandle
import dev.kmpx.collections.sets.MutableSortedSet
import dev.kmpx.collections.sets.SortedSet
import kotlin.jvm.JvmInline

/**
 * A [Map] implementation internally based on a self-balancing binary tree.
 *
 * @param K the type of map keys
 * @param V the type of map values
 */
class TreeMap<K : Comparable<K>, V> internal constructor(
    private val entryTree: OrderedBinaryTree<MutableMap.MutableEntry<K, V>> = OrderedBinaryTree.create(),
) : AbstractMutableStableMap<K, V>(), MutableSortedMap<K, V> {
    internal class MutableMapEntry<K, V>(
        override val key: K,
        initialValue: V,
    ) : MutableMap.MutableEntry<K, V> {
        companion object {
            fun <K : Comparable<K>, V> selectKey(
                entry: MutableMap.MutableEntry<K, V>,
            ): K = entry.key
        }

        private var mutableValue: V = initialValue

        override val value: V
            get() = mutableValue

        override fun setValue(newValue: V): V {
            val previousValue = mutableValue

            mutableValue = newValue

            return previousValue
        }
    }

    @JvmInline
    internal value class TreeMapHandle<K : Comparable<K>, V> internal constructor(
        internal val node: EntryNode<K, V>,
    ) : EntryHandle<K, V> {
        override val key: K
            get() = node.payload.key
    }

    private class EntrySet<K : Comparable<K>, V>(
        private val entryTree: OrderedBinaryTree<MutableMap.MutableEntry<K, V>>,
    ) : AbstractMutableCollection<MutableMap.MutableEntry<K, V>>(), MutableSet<MutableMap.MutableEntry<K, V>> {
        override val size: Int
            get() = entryTree.size

        override fun iterator(): MutableIterator<MutableMap.MutableEntry<K, V>> =
            OrderedBinaryTreeIterator.iterate(tree = entryTree)

        override fun add(element: MutableMap.MutableEntry<K, V>): Boolean {
            // Following the behavior of the built-in `MutableMap.entries`
            throw UnsupportedOperationException()
        }
    }

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>> = EntrySet(
        entryTree = entryTree,
    )

    override val size: Int
        get() = entryTree.size

    override fun put(
        key: K,
        value: V,
    ): V? {
        val (location, existingNode) = findByKey(key = key)

        return when (existingNode) {
            null -> {
                entryTree.insert(
                    location = location,
                    payload = MutableMapEntry(
                        key = key,
                        initialValue = value,
                    ),
                )

                null
            }

            else -> {
                val existingEntry = existingNode.payload
                val previousValue = existingEntry.value

                existingEntry.setValue(value)

                previousValue
            }
        }
    }

    override fun insertEntry(
        key: K,
        value: V,
    ): EntryHandle<K, V>? {
        val (location, existingNode) = findByKey(key = key)

        if (existingNode != null) {
            return null
        }

        val insertedNode = entryTree.insert(
            location = location,
            payload = MutableMapEntry(
                key = key,
                initialValue = value,
            ),
        )

        return insertedNode.pack()
    }

    override fun setValueVia(
        entryHandle: EntryHandle<K, V>,
        newValue: V,
    ): V? {
        val node = entryHandle.unpack() ?: return null

        val entry = node.payload

        return entry.setValue(newValue)
    }

    override fun removeEntryVia(
        entryHandle: EntryHandle<K, V>,
    ): V? {
        val node = entryHandle.unpack() ?: return null
        val removedEntry = node.payload

        entryTree.remove(node = node)

        return removedEntry.value
    }

    override fun resolve(
        key: K,
    ): EntryHandle<K, V>? {
        val (_, node) = findByKey(key = key)
        return node?.pack()
    }

    override fun getValueVia(
        entryHandle: EntryHandle<K, V>,
    ): V? {
        val node = entryHandle.unpack() ?: return null
        return node.payload.value
    }

    private fun findByKey(
        key: K,
    ): Pair<EntryLocation<K, V>, EntryNode<K, V>?> {
        val location = entryTree.findExactWith(
            comparator = BoundComparator.compareBy(
                boundKey = key,
                keySelector = MutableMap.MutableEntry<K, V>::key,
            ),
        )

        val existingNode = entryTree.resolve(
            location = location,
        )

        return Pair(location, existingNode)
    }

    override fun floorEntry(
        key: K,
    ): MutableMap.MutableEntry<K, V>? {
        val floorNode = entryTree.findFloorWith(
            comparator = BoundComparator.compareBy(
                boundKey = key,
                keySelector = MutableMap.MutableEntry<K, V>::key,
            ),
        )

        return floorNode?.payload
    }

    override fun ceilingEntry(
        key: K,
    ): MutableMap.MutableEntry<K, V>? {
        val ceilNode = entryTree.findCeilWith(
            comparator = BoundComparator.compareBy(
                boundKey = key,
                keySelector = MutableMap.MutableEntry<K, V>::key,
            ),
        )

        return ceilNode?.payload
    }

    override fun selectEntry(entryRank: Int): Map.Entry<K, V>? {
        val node = entryTree.select(index = entryRank) ?: return null
        return node.payload
    }

    override fun findKeyRank(key: K): RankResult = entryTree.findRank(
         comparator = BoundComparator.compareBy(
             boundKey = key,
             keySelector = MutableMap.MutableEntry<K, V>::key,
         ),
     )

    override val sortedEntries: SortedSet<Map.Entry<K, V>>
        get() = TODO("Not yet implemented")

    override val sortedKeys: MutableSortedSet<K>
        get() = TODO("Not yet implemented")

    override val sortedValues: List<V>
        get() = TODO("Not yet implemented")
}

fun <K : Comparable<K>, V> treeMapOf(
    vararg pairs: Pair<K, V>,
): TreeMap<K, V> {
    val map = TreeMap<K, V>()

    for ((key, value) in pairs) {
        map[key] = value
    }

    return map
}

private typealias EntryLocation<K, V> = OrderedBinaryTree.Location<MutableMap.MutableEntry<K, V>>

private typealias EntryNode<K, V> = OrderedBinaryTree.Node<MutableMap.MutableEntry<K, V>>

private fun <K : Comparable<K>, V> EntryHandle<K, V>.unpack(): EntryNode<K, V>? {
    this as? TreeMap.TreeMapHandle<K, V> ?: throw IllegalArgumentException(
        "Handle is not a TreeMapHandle: $this"
    )

    return when {
        node.isValid -> node
        else -> null
    }
}

private fun <K : Comparable<K>, V> EntryNode<K, V>.pack(): EntryHandle<K, V> = TreeMap.TreeMapHandle(
    node = this,
)
