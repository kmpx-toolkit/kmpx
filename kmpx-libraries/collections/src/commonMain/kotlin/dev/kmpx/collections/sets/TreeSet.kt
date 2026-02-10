package dev.kmpx.collections.sets

import dev.kmpx.collections.StableCollection.Handle
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.insert
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup.BoundComparator
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup.findCeilWith
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup.findExactWith
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup.findFloorWith
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.remove
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.resolve
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.select
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.traverse
import dev.kmpx.collections.internal.iterators.OrderedBinaryTreeIterator
import dev.kmpx.collections.SortedCollections.RankResult
import dev.kmpx.collections.findRank
import kotlin.jvm.JvmInline

/**
 * A [Set] implementation internally based on a self-balancing binary tree.
 *
 * @param E the type of elements contained in the collection
 */
class TreeSet<E : Comparable<E>> internal constructor() : AbstractMutableSet<E>(), MutableStableSet<E>,
    MutableSortedSet<E> {
    @JvmInline
    internal value class TreeSetHandle<E> internal constructor(
        internal val node: OrderedBinaryTree.Node<E>,
    ) : Handle<E>

    private val elementTree = OrderedBinaryTree.create<E>()

    override val size: Int
        get() = elementTree.size

    override fun iterator(): MutableIterator<E> = OrderedBinaryTreeIterator(
        tree = elementTree,
    )

    override val handles: Sequence<Handle<E>>
        get() = elementTree.traverse().map { it.pack() }

    override fun lookup(element: E): Handle<E>? {
        val location = elementTree.findExactWith(
            comparator = BoundComparator.compareTo(boundElement = element),
        )

        val node = elementTree.resolve(location = location) ?: return null

        return node.pack()
    }

    override fun getVia(handle: Handle<E>): E {
        val node = handle.unpack() ?: throw IllegalArgumentException("Handle is invalid: $handle")
        return node.payload
    }

    override fun add(
        element: E,
    ): Boolean = insert(element) != null

    override fun insert(element: E): Handle<E>? {
        val location = elementTree.findExactWith(
            comparator = BoundComparator.compareTo(boundElement = element),
        )

        val existingNode = elementTree.resolve(location = location)

        if (existingNode != null) {
            return null
        }

        val insertedNode = elementTree.insert(
            location = location,
            payload = element,
        )

        return insertedNode.pack()
    }

    override fun remove(
        element: E,
    ): Boolean {
        val handle = lookup(element = element) ?: return false

        removeVia(handle = handle)

        return true
    }

    override fun removeVia(
        handle: Handle<E>,
    ): E {
        val node = handle.unpack() ?: throw IllegalArgumentException("Handle is invalid: $handle")

        val removedElement = node.payload

        elementTree.remove(node = node)

        return removedElement
    }

    override fun contains(element: E): Boolean {
        val location = elementTree.findExactWith(
            comparator = BoundComparator.compareTo(boundElement = element),
        )

        return elementTree.resolve(location = location) != null
    }

    override fun floor(
        element: E,
    ): E? {
        val floorNode = elementTree.findFloorWith(
            comparator = BoundComparator.compareTo(boundElement = element),
        ) ?: return null

        return floorNode.payload
    }

    override fun ceiling(
        element: E,
    ): E? {
        val ceilingNode = elementTree.findCeilWith(
            comparator = BoundComparator.compareTo(boundElement = element),
        ) ?: return null

        return ceilingNode.payload
    }

    override fun select(
        rank: Int,
    ): E? {
        val node = elementTree.select(index = rank) ?: return null

        return node.payload
    }

    override fun findRank(
        element: E,
    ): RankResult = elementTree.findRank(
        comparator = BoundComparator.compareTo(boundElement = element),
    )

    override val asList: List<E>
        get() = TODO("Not yet implemented")
}

fun <E : Comparable<E>> treeSetOf(
    vararg elements: E,
): TreeSet<E> {
    val set = TreeSet<E>()

    for (element in elements) {
        set.add(element)
    }

    return set
}

private fun <E> Handle<E>.unpack(): OrderedBinaryTree.Node<E>? {
    this as? TreeSet.TreeSetHandle<E> ?: throw IllegalArgumentException(
        "Handle is not a TreeSetHandle: $this"
    )

    return when {
        node.isValid -> node
        else -> null
    }
}

private fun <E> OrderedBinaryTree.Node<E>.pack(): Handle<E> = TreeSet.TreeSetHandle(
    node = this,
)
