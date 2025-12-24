package dev.kmpx.collections.maps

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.insert
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup.findBy
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.remove
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.resolve
import dev.kmpx.collections.internal.iterators.OrderedBinaryTreeIterator
import dev.kmpx.collections.maps.StableMap.EntryHandle
import kotlin.jvm.JvmInline

/**
 * A [Map] implementation internally based on a self-balancing binary tree.
 *
 * @param K the type of map keys
 * @param V the type of map values
 */
class TreeMap<K : Comparable<K>, V> internal constructor(
    private val entryTree: OrderedBinaryTree<MutableMap.MutableEntry<K, V>> = OrderedBinaryTree.create(),
) : AbstractMutableStableMap<K, V>() {
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

    override fun addEntryEx(
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
        val location = entryTree.findBy(
            key = key,
            selector = MutableMapEntry.Companion::selectKey,
        )

        val existingNode = entryTree.resolve(
            location = location,
        )

        return Pair(location, existingNode)
    }
}

fun <K : Comparable<K>, V> mutableTreeMapOf(
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
