package dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree.Node

fun <PayloadT : Comparable<PayloadT>> OrderedBinaryTree<PayloadT>.attachVerified(
    location: OrderedBinaryTree.Location<PayloadT>,
    payload: PayloadT,
    color: Node.Color,
): Node<PayloadT> {
    val insertedNode = this.attach(
        location = location,
        payload = payload,
        color = color,
    )

    verifyIntegrity()

    return insertedNode
}

fun <PayloadT : Comparable<PayloadT>> OrderedBinaryTree<PayloadT>.swapVerified(
    node: Node<PayloadT>,
    side: OrderedBinaryTree.Side,
) {
    this.swap(
        sourceNode = node,
        side = side,
    )

    verifyIntegrity()
}

fun <PayloadT : Comparable<PayloadT>> OrderedBinaryTree<PayloadT>.cutOffVerified(
    leafNode: Node<PayloadT>,
): OrderedBinaryTree.Location<PayloadT> {
    val cutOffLeafLocation = this.cutOffLeaf(
        leafNode = leafNode,
    )

    if (leafNode.isValid) {
        throw AssertionError("The leaf handle should be invalid after being cut off")
    }

    verifyIntegrity()

    return cutOffLeafLocation
}

fun <PayloadT : Comparable<PayloadT>> OrderedBinaryTree<PayloadT>.rotateVerified(
    pivotNode: Node<PayloadT>,
    direction: OrderedBinaryTree.RotationDirection,
): Node<PayloadT> {
    val newSubtreeRootNode = this.rotate(
        pivotNode = pivotNode,
        direction = direction,
    )

    verifyIntegrity()

    return newSubtreeRootNode
}

fun <PayloadT : Comparable<PayloadT>> OrderedBinaryTree<PayloadT>.collapseVerified(
    node: Node<PayloadT>,
    side: OrderedBinaryTree.Side,
): Node<PayloadT> {
    val inOrderNeighbourDescendant = this.collapse(
        topNode = node,
        side = side,
    )

    if (node.isValid) {
        throw AssertionError("The node should be invalid after collapsing")
    }

    verifyIntegrity()

    return inOrderNeighbourDescendant
}
