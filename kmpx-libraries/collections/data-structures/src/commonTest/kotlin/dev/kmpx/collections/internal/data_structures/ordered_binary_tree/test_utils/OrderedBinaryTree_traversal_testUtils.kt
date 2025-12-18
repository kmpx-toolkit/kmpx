package dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.getChild
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.traverse

fun <PayloadT> OrderedBinaryTree<PayloadT>.traverseNaively(): Sequence<OrderedBinaryTree.Node<PayloadT>> =
    this.traverseNaivelyOrEmpty(
        subtreeRootNode = rootNode,
    )

fun <PayloadT> OrderedBinaryTree<PayloadT>.traverseNaively(
    subtreeRootNode: OrderedBinaryTree.Node<PayloadT>,
): Sequence<OrderedBinaryTree.Node<PayloadT>> {
    val leftChild = subtreeRootNode.getChild(
        side = OrderedBinaryTree.Side.Left,
    )

    val rightChild = subtreeRootNode.getChild(
        side = OrderedBinaryTree.Side.Right,
    )

    return sequence {
        yieldAll(traverseNaivelyOrEmpty(subtreeRootNode = leftChild))
        yield(subtreeRootNode)
        yieldAll(traverseNaivelyOrEmpty(subtreeRootNode = rightChild))
    }
}

fun <PayloadT> OrderedBinaryTree<PayloadT>.getNodeTraversing(
    payload: PayloadT,
): OrderedBinaryTree.Node<PayloadT> = traverse().single {
    it.payload == payload
}

private fun <PayloadT> OrderedBinaryTree<PayloadT>.traverseNaivelyOrEmpty(
    subtreeRootNode: OrderedBinaryTree.Node<PayloadT>?,
): Sequence<OrderedBinaryTree.Node<PayloadT>> {
    if (subtreeRootNode == null) return emptySequence()

    return this.traverseNaively(
        subtreeRootNode = subtreeRootNode,
    )
}
