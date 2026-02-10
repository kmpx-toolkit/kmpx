package dev.kmpx.collections.lists

import dev.kmpx.collections.StableCollection.Handle
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.getInOrderSuccessor
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.getRank
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.insert
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.insertRelative
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup.bind
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup.findLeaningWith
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.remove
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.resolve
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.resolveRelative
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.select
import dev.kmpx.collections.internal.iterators.OrderedBinaryTreeIterator
import dev.kmpx.collections.lists.TreeSortedList.TreeSortedListHandle
import kotlin.jvm.JvmInline

/**
 * A [MutableSortedList] implementation internally based on a self-balancing binary tree with order statistic.
 *
 * @param E the type of elements contained in the collection
 */
class TreeSortedList<E>(
    private val comparator: Comparator<E>,
) : AbstractList<E>(), MutableStableSortedList<E> {
    @JvmInline
    internal value class TreeSortedListHandle<E> internal constructor(
        val node: OrderedBinaryTree.Node<E>,
    ) : Handle<E>

    private val elementTree = OrderedBinaryTree.create<E>()

    override fun iterator(): MutableIterator<E> = OrderedBinaryTreeIterator(
        tree = elementTree,
    )

    override val handles: Sequence<Handle<E>>
        get() = TODO("Not yet implemented")

    override val size: Int
        get() = elementTree.size

    override fun get(index: Int): E {
        val node = elementTree.select(index = index)
            ?: throw IndexOutOfBoundsException("Index $index is out of bounds for size ${size}.")

        return node.payload
    }

    override fun resolveAt(index: Int): Handle<E>? {
        val nodeAtIndex = elementTree.select(index = index) ?: return null

        return nodeAtIndex.pack()
    }

    override fun getVia(
        handle: Handle<E>,
    ): E {
        val node = handle.unpack() ?: throw IllegalArgumentException("Handle is invalid: $handle")

        return node.payload
    }

    override fun indexOf(element: E): Int {
        // Search for the first instance of the element
        val firstNode = findFirstNodeWithPayloadEqualLiterally(
            payload = element,
        ) ?: return -1 // No such element is present

        // Get the rank (index) of the found node
        return elementTree.getRank(
            node = firstNode,
        )
    }

    override fun indexOfVia(
        handle: Handle<E>,
    ): Int {
        val node = handle.unpack() ?: throw IllegalArgumentException("Handle is invalid: $handle")

        return elementTree.getRank(
            node = node,
        )
    }

    override fun resolveFirst(
        element: E,
    ): Handle<E>? {
        val firstNode = findFirstNodeWithPayloadEqualLiterally(
            payload = element,
        ) ?: return null

        return firstNode.pack()
    }

    override fun predictIndexOf(newElement: E): Int {
        // Find the location where the element is (or would be if it was present)
        val location = elementTree.findLeaningWith(
            comparator = comparator.bind(newElement),
            leanSide = OrderedBinaryTree.Side.Right,
        )

        when (location) {
            OrderedBinaryTree.RootLocation -> {
                // If the tree is empty, the potential index of a new element _must_ be 0
                return 0
            }

            is OrderedBinaryTree.RelativeLocation -> {
                val referenceNode = elementTree.resolveRelative(
                    location = location,
                )

                when (referenceNode) {
                    null -> { // There's no element that compares equal to the considered element
                        val parentNode: OrderedBinaryTree.Node<E> = location.parentNode

                        // Get the rank (index) of the parent node
                        val parentNodeIndex = elementTree.getRank(
                            node = parentNode,
                        )

                        return when (location.side) {
                            // If the new element is predicted to belong on the left side of the parent, it means it's
                            // going to replace it at its index.
                            OrderedBinaryTree.Side.Left -> parentNodeIndex

                            // If the new element is predicted to belong on the right side of the parent, it means it's
                            // going to be inserted just after it.
                            OrderedBinaryTree.Side.Right -> parentNodeIndex + 1
                        }
                    }

                    else -> {
                        // There are some elements (at least one) that compare equal to the considered element, and we
                        // found the last such element (possibly the only one).

                        // Get the rank (index) of the found node
                        val referenceNodeIndex = elementTree.getRank(
                            node = referenceNode,
                        )

                        // The new element would be inserted just after the last existing element that compares equal.
                        return referenceNodeIndex + 1
                    }
                }
            }
        }
    }

    override fun add(element: E): Boolean {
        insertNewNodeWithPayload(
            payload = element,
        )

        return true
    }

    override fun insert(element: E): Handle<E> {
        val insertedNode = insertNewNodeWithPayload(
            payload = element,
        )

        return insertedNode.pack()
    }

    override fun remove(element: E): Boolean {
        // Find the location of the first element literally equal to the element we want to remove
        val firstNode: OrderedBinaryTree.Node<E> = findFirstNodeWithPayloadEqualLiterally(
            payload = element,
        ) ?: return false // The element we wanted to remove is not actually present

        elementTree.remove(
            node = firstNode,
        )

        return true
    }

    override fun removeVia(handle: Handle<E>): E {
        val node = handle.unpack() ?: throw IllegalArgumentException("Handle is invalid: $handle")
        val element = node.payload

        elementTree.remove(
            node = node,
        )

        return element
    }

    override fun addAll(elements: Collection<E>): Boolean {
        if (elements.isEmpty()) {
            return false
        }

        for (element in elements) {
            add(element)
        }

        return true
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        val self: MutableIterable<E> = this
        return self.removeAll { it in elements }
    }

    override fun retainAll(elements: Collection<E>): Boolean {
        val self: MutableIterable<E> = this
        return self.retainAll { it in elements }
    }

    override fun clear() {
        elementTree.cutOffRoot()
    }

    private fun findFirstNodeWithPayloadEqualLiterally(
        payload: E,
    ): OrderedBinaryTree.Node<E>? {
        // Find the location of the first element equal order-wise to the element we want to remove
        val location = elementTree.findLeaningWith(
            comparator = comparator.bind(payload),
            leanSide = OrderedBinaryTree.Side.Left,
        )

        val referenceNode = elementTree.resolve(
            location = location,
        ) ?: return null // If the location is empty, the searched element is not actually present

        return generateSequence(referenceNode) { node ->
            node.getInOrderSuccessor()
        }.takeWhile { node ->
            comparator.compare(payload, node.payload) == 0
        }.firstOrNull { node ->
            node.payload == payload
        }
    }

    /**
     * Inserts a new node with the given [payload] into the tree, maintaining the sorted order.
     *
     * @return the newly inserted node
     */
    private fun insertNewNodeWithPayload(
        payload: E,
    ): OrderedBinaryTree.Node<E> {
        // Search for the last instance of the element
        val location = elementTree.findLeaningWith(
            comparator = comparator.bind(payload),
            leanSide = OrderedBinaryTree.Side.Right,
        )

        val existingNode = elementTree.resolve(
            location = location,
        )

        when (existingNode) {
            null -> { // There's no element that compares equal to this one yet
                // Just insert a new element at the given location
                return elementTree.insert(
                    location = location,
                    payload = payload,
                )
            }

            else -> { // There's already an element that compares equal to this one (e.g. with the same key)
                // Insert the new element on the right side of the existing one
                return elementTree.insertRelative(
                    node = existingNode,
                    side = OrderedBinaryTree.Side.Right,
                    payload = payload,
                )
            }
        }
    }
}

private fun <E> Handle<E>.unpack(): OrderedBinaryTree.Node<E>? {
    this as? TreeSortedListHandle ?: throw IllegalArgumentException("Handle is not a TreeSortedListHandle: $this")

    return when {
        node.isValid -> node
        else -> null
    }
}

private fun <E> OrderedBinaryTree.Node<E>.pack(): Handle<E> = TreeSortedListHandle(
    node = this,
)

fun <E> treeSortedListOf(
    comparator: Comparator<E>,
    vararg elements: E,
): TreeSortedList<E> {
    val mutableTreeList = TreeSortedList(
        comparator = comparator,
    )

    elements.forEach { element ->
        mutableTreeList.add(element)
    }

    return mutableTreeList
}

fun <E> treeSortedListOf(
    comparator: Comparator<E>,
    elements: Collection<E>,
): TreeSortedList<E> {
    val mutableTreeList = TreeSortedList(
        comparator = comparator,
    )

    elements.forEach { element ->
        mutableTreeList.add(element)
    }

    return mutableTreeList
}
