package dev.kmpx.collections.lists

import dev.kmpx.collections.StableCollection.Handle
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.getNextInOrderFreeLocation
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.getRank
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.getSideMostFreeLocation
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.insert
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.insertAll
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.insertRelative
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.select
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.takeOut
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.traverse
import kotlin.jvm.JvmInline

/**
 * A [List] implementation internally based on a self-balancing binary tree with order statistic.
 *
 * @param E the type of elements contained in the collection
 */
class TreeList<E>() : AbstractMutableList<E>(), MutableStableList<E> {
    @JvmInline
    internal value class TreeListHandle<E> internal constructor(
        val node: OrderedBinaryTree.Node<E>,
    ) : Handle<E>

    private val elementTree = OrderedBinaryTree.create<E>()

    override val size: Int
        get() = elementTree.size

    override val handles: Sequence<Handle<E>>
        get() = elementTree.traverse().map { it.pack() }

    override fun resolveFirst(
        element: E,
    ): Handle<E>? = elementTree.traverse().find { node ->
        node.payload == element
    }?.pack()

    override fun resolveAt(
        index: Int,
    ): Handle<E>? {
        val node = elementTree.select(index = index) ?: return null

        return node.pack()
    }

    /**
     * Returns the element at the specified index in the list.
     *
     * Guarantees logarithmic time complexity.
     */
    override fun get(
        index: Int,
    ): E {
        val node = elementTree.select(index = index) ?: throw IndexOutOfBoundsException(
            "Index $index is out of bounds for size ${size}."
        )

        return node.payload
    }

    /**
     * Returns the element corresponding to the given handle.
     *
     * Guarantees constant time complexity.
     */
    override fun getVia(
        handle: Handle<E>,
    ): E? {
        val node = handle.unpack() ?: return null

        return node.payload
    }

    /**
     * Replaces the element at the specified position in this list with the specified element.
     *
     * Guarantees logarithmic time complexity.
     *
     * @return the element previously at the specified position.
     */
    override fun set(
        index: Int,
        element: E,
    ): E {
        val node = elementTree.select(index = index) ?: throw IndexOutOfBoundsException(
            "Index $index is out of bounds for size ${size}."
        )

        val previousElement = node.payload

        node.setPayload(
            payload = element,
        )

        return previousElement
    }

    override fun setVia(
        handle: Handle<E>,
        element: E,
    ): E? {
        val node = handle.unpack() ?: return null

        val previousElement = node.payload

        node.setPayload(
            payload = element,
        )

        return previousElement
    }

    /**
     * Adds the specified element to the end of this list.
     *
     * Guarantees logarithmic time complexity.
     *
     * @return `true` because the list is always modified as the result of this operation.
     */
    override fun add(
        element: E,
    ): Boolean {
        insert(element = element)

        return true
    }

    /**
     * Adds the specified element to the end of this list in exchange for a handle.
     *
     * Guarantees logarithmic time complexity.
     *
     * @return the handle to the added element.
     */
    override fun insert(
        element: E,
    ): Handle<E> {
        val freeLocation = elementTree.getSideMostFreeLocation(
            side = OrderedBinaryTree.Side.Right,
        )

        val insertedNode = elementTree.insert(
            location = freeLocation,
            payload = element,
        )

        return insertedNode.pack()
    }

    /**
     * Inserts an element into the list at the specified [index].
     *
     * Guarantees logarithmic time complexity.
     */
    override fun add(
        index: Int,
        element: E,
    ) {
        insertAt(
            index = index,
            element = element,
        )
    }

    /**
     * Inserts all the elements of the specified collection [elements] into this list at the specified [index].
     * Guarantees logarithmic time complexity.
     *
     * @return `true` because the list is always modified as the result of this operation.
     */
    override fun addAll(
        index: Int,
        elements: Collection<E>,
    ): Boolean {
        addAllAt(
            index = index,
            elements = elements.toList(),
        )

        return true
    }

    /**
     * Inserts all the elements of the specified collection [elements] into this list at the specified [index].
     * Guarantees logarithmic time complexity.
     */
    fun addAllAt(
        index: Int,
        elements: List<E>,
    ) {
        if (index !in 0..size) {
            throw IndexOutOfBoundsException(
                "Index $index is out of bounds for size ${size}."
            )
        }

        val location = when (val node = elementTree.select(index = index)) {
            // index == size, we have to append the elements
            null -> elementTree.getSideMostFreeLocation(side = OrderedBinaryTree.Side.Right)

            // Otherwise, we'll start inserting on the left side of the given node
            else -> elementTree.getNextInOrderFreeLocation(
                node = node,
                side = OrderedBinaryTree.Side.Left,
            )
        }

        elementTree.insertAll(
            location = location,
            payloads = elements,
        )
    }

    /**
     * Inserts an element into the list at the specified [index] in exchange for a handle.
     * Guarantees logarithmic time complexity.
     *
     * @return the handle to the added element.
     */
    override fun insertAt(
        index: Int,
        element: E,
    ): Handle<E> {
        if (index !in 0..size) {
            throw IndexOutOfBoundsException(
                "Index $index is out of bounds for size ${size}."
            )
        }

        val referenceNode = elementTree.select(
            index = index,
        )

        val insertedNode = when (referenceNode) {
            null -> elementTree.insert(
                location = elementTree.getSideMostFreeLocation(
                    side = OrderedBinaryTree.Side.Right,
                ),
                payload = element,
            )

            else -> elementTree.insertRelative(
                node = referenceNode,
                side = OrderedBinaryTree.Side.Left,
                payload = element,
            )
        }

        return insertedNode.pack()
    }

    /**
     * Removes an element at the specified [index] from the list.
     * Guarantees logarithmic time complexity.
     *
     * @return the element that has been removed.
     */
    override fun removeAt(
        index: Int,
    ): E {
        val node = elementTree.select(index = index) ?: throw IndexOutOfBoundsException(
            "Index $index is out of bounds for size ${size}."
        )

        return elementTree.takeOut(node = node)
    }

    /**
     * Removes the element corresponding to the given handle from the list.
     * Guarantees logarithmic time complexity.
     *
     * @return the element that has been removed.
     */
    override fun removeVia(
        handle: Handle<E>,
    ): E? {
        val node = handle.unpack() ?: return null

        return elementTree.takeOut(node = node)
    }

    /**
     * Returns the index of the element corresponding to the given handle in the list.
     *
     * Guarantees logarithmic time complexity.
     *
     * @return the index of the element or null if the corresponding element has
     * already been removed
     */
    override fun indexOfVia(
        handle: Handle<E>,
    ): Int? {
        val node = handle.unpack() ?: return null

        return elementTree.getRank(node = node)
    }

    /**
     * Returns the index of the first emission of the specified element in the list, or -1 if the specified
     * element is not contained in the list.
     *
     * Guarantees only linear time complexity, as the list can contain duplicates. For logarithmic time complexity,
     * use [indexOfVia] with a handle.
     */
    @Suppress("RedundantOverride")
    override fun indexOf(element: E): Int = super.indexOf(element)
}

fun <E> treeListOf(
    vararg elements: E,
): TreeList<E> {
    val treeList = TreeList<E>()

    elements.forEach { element ->
        treeList.add(element)
    }

    return treeList
}

private fun <E> Handle<E>.unpack(): OrderedBinaryTree.Node<E>? {
    this as? TreeList.TreeListHandle ?: throw IllegalArgumentException(
        "Handle is not a TreeListHandle: $this"
    )

    return when {
        node.isValid -> node
        else -> null
    }
}

private fun <E> OrderedBinaryTree.Node<E>.pack(): Handle<E> = TreeList.TreeListHandle(
    node = this,
)
