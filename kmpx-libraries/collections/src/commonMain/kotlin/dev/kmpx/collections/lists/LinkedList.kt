package dev.kmpx.collections.lists

import dev.kmpx.collections.StableCollection.Handle
import dev.kmpx.collections.internal.data_structures.linked_path.LinkedPath
import dev.kmpx.collections.internal.data_structures.linked_path.LinkedPath.Node
import dev.kmpx.collections.internal.data_structures.linked_path.appendAll
import dev.kmpx.collections.internal.data_structures.linked_path.getNodeAt
import dev.kmpx.collections.internal.data_structures.linked_path.insertAllBefore
import dev.kmpx.collections.internal.data_structures.linked_path.traverse
import kotlin.jvm.JvmInline

/**
 * A [List] implementation internally based on a double-linked list data structure.
 *
 * @param E the type of elements contained in the collection
 */
class LinkedList<E>() : AbstractMutableList<E>(), MutableStableList<E> {
    @JvmInline
    internal value class LinkedListHandle<E>(
        val node: Node<E>,
    ) : Handle<E>

    private var mutableSize: Int = 0

    private val linkedPath = LinkedPath.create<E>()

    override val size: Int
        get() = mutableSize

    override val handles: Sequence<Handle<E>>
        get() = linkedPath.traverse().map { it.pack() }

    override fun resolveFirst(
        element: E,
    ): Handle<E>? = linkedPath.traverse().find { node ->
        node.payload == element
    }?.pack()

    override fun resolveAt(
        index: Int,
    ): Handle<E>? {
        if (index !in 0..<mutableSize) {
            return null
        }

        val node = linkedPath.getNodeAt(index)

        return node.pack()
    }

    /**
     * Returns the element at the specified index in the list.
     *
     * Guarantees linear time complexity.
     */
    override fun get(
        index: Int,
    ): E {
        if (index !in 0..<mutableSize) {
            throw IndexOutOfBoundsException("Index $index is out of bounds for size ${size}.")
        }

        val node = linkedPath.getNodeAt(index)

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
        val node = handle.unpack()

        return node.payload
    }

    /**
     * Replaces the element at the specified position in this list with the specified element.
     *
     * Guarantees linear time complexity.
     *
     * @return the element previously at the specified position.
     */
    override fun set(
        index: Int,
        element: E,
    ): E {
        val node = linkedPath.getNodeAt(index)

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
        val node = handle.unpack()

        val previousElement = node.payload

        node.setPayload(
            payload = element,
        )

        return previousElement
    }

    /**
     * Adds the specified element to the end of this list.
     *
     * Guarantees constant time complexity.
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
     * Guarantees constant time complexity.
     *
     * @return the handle to the added element.
     */
    override fun insert(
        element: E,
    ): Handle<E> {
        val appendedNode = linkedPath.append(element)

        mutableSize += 1

        return appendedNode.pack()
    }

    /**
     * Inserts an element into the list at the specified [index].
     *
     * Guarantees linear time complexity.
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
     *
     * Guarantees linear time complexity.
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
     *
     * Guarantees linear time complexity.
     */
    fun addAllAt(
        index: Int,
        elements: List<E>,
    ) {
        if (index !in 0..size) {
            throw IndexOutOfBoundsException("Index $index is out of bounds for size ${size}.")
        }

        when {
            index == size -> {
                linkedPath.appendAll(elements)
            }

            else -> {
                val referenceNode = linkedPath.getNodeAt(index)

                linkedPath.insertAllBefore(
                    referenceNode = referenceNode,
                    payloads = elements,
                )
            }
        }

        mutableSize += elements.size
    }

    /**
     * Inserts an element into the list at the specified [index] in exchange for a handle.
     *
     * Guarantees linear time complexity.
     *
     * @return the handle to the added element.
     */
    override fun insertAt(
        index: Int,
        element: E,
    ): Handle<E> {
        if (index !in 0..size) {
            throw IndexOutOfBoundsException("Index $index is out of bounds for size ${size}.")
        }

        val addedNode = when {
            index == size -> {
                linkedPath.append(element)
            }

            else -> {
                val referenceNode = linkedPath.getNodeAt(index)

                linkedPath.insertBefore(
                    referenceNode = referenceNode,
                    payload = element,
                )
            }
        }

        mutableSize += 1

        return addedNode.pack()
    }

    /**
     * Removes an element at the specified [index] from the list.
     *
     * Guarantees linear time complexity.
     *
     * @return the element that has been removed.
     */
    override fun removeAt(
        index: Int,
    ): E {
        if (index !in 0..size) {
            throw IndexOutOfBoundsException("Index $index is out of bounds for size ${size}.")
        }

        val node = linkedPath.getNodeAt(index)

        val removedElement = node.payload

        linkedPath.cutOff(node)

        mutableSize -= 1

        return removedElement
    }

    /**
     * Removes the element corresponding to the given handle from the list.
     *
     * Guarantees linear time complexity.
     *
     * @return the element that has been removed.
     */
    override fun removeVia(
        handle: Handle<E>,
    ): E? {
        val node = handle.unpack()

        val removedElement = node.payload

        linkedPath.cutOff(node)

        mutableSize -= 1

        return removedElement
    }

    /**
     * Returns the index of the element corresponding to the given handle in the list.
     *
     * Guarantees linear time complexity.
     *
     * @return the index of the element or null if the corresponding element has already been removed
     */
    override fun indexOfVia(
        handle: Handle<E>,
    ): Int? {
        val node: Node<E> = handle.unpack()

        val index = linkedPath.traverse().indexOf(node)

        return when {
            index < 0 -> null
            else -> index
        }
    }
}

fun <E> linkedListOf(
    vararg elements: E,
): LinkedList<E> {
    val linkedList = LinkedList<E>()

    elements.forEach { element ->
        linkedList.add(element)
    }

    return linkedList
}

private fun <E> Handle<E>.unpack(): Node<E> {
    this as? LinkedList.LinkedListHandle ?: throw IllegalArgumentException("Handle is not a LinkedListHandle: $this")

    return node
}

private fun <E> Node<E>.pack(): Handle<E> = LinkedList.LinkedListHandle(
    node = this,
)
