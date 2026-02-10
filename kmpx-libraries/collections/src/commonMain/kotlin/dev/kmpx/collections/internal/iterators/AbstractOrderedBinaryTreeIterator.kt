package dev.kmpx.collections.internal.iterators

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.getInOrderSuccessor
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.getLeftMostDescendant
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.hasInOrderSuccessor
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.isEmpty
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.remove

internal abstract class AbstractOrderedBinaryTreeIterator<PayloadT, ElementT> internal constructor(
    /**
     * The tree being iterated over.
     */
    private val tree: OrderedBinaryTree<PayloadT>,
) : MutableIterator<ElementT> {
    /**
     * The handle to the _current_ node, which is a baseline for determining the next node in the iteration order. If
     * it's `null`, it means that the iteration is at its initial point (either `next` hasn't been called yet, or every
     * `next` call was followed by a `remove` call).
     */
    private var currentNode: OrderedBinaryTree.Node<PayloadT>? = null

    /**
     * Indicates whether the most recent `next` call has already been followed by a `remove` call.
     */
    private var wasNodeRemoved: Boolean = false

    override fun remove() {
        if (wasNodeRemoved) {
            throw IllegalStateException("The most recent `next` call has already been followed by a `remove` call")
        }

        val currentNode = this.currentNode ?: throw IllegalStateException("`next` has not been called yet")

        val predecessorNode = tree.remove(
            node = currentNode,
        )

        this.currentNode = predecessorNode
        this.wasNodeRemoved = true
    }

    override fun next(): ElementT {
        val successorNode = when (val currentNode = this.currentNode) {
            null -> tree.getLeftMostDescendant()
            else -> currentNode.getInOrderSuccessor()
        } ?: throw NoSuchElementException("The iteration has no next element")

        this.wasNodeRemoved = false
        this.currentNode = successorNode

        val payload = successorNode.payload

        return extract(payload = payload)
    }

    override fun hasNext(): Boolean = when (val currentNode = this.currentNode) {
        null -> !tree.isEmpty()
        else -> currentNode.hasInOrderSuccessor()
    }

    abstract fun extract(
        payload: PayloadT,
    ): ElementT
}
