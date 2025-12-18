package dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree.Node
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.getLeftChildLocation
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.getRightChildLocation

data class NodeData<PayloadT>(
    val payload: PayloadT,
    val color: Node.Color,
    val leftChild: NodeData<PayloadT>? = null,
    val rightChild: NodeData<PayloadT>? = null,
) {
    fun put(
        tree: OrderedBinaryTree<PayloadT>,
        location: OrderedBinaryTree.Location<PayloadT>,
    ) {
        val node = tree.attach(
            location = location,
            payload = payload,
            color = color,
        )

        leftChild?.put(
            tree = tree,
            location = node.getLeftChildLocation(),
        )

        rightChild?.put(
            tree = tree,
            location = node.getRightChildLocation(),
        )
    }
}

fun <PayloadT> OrderedBinaryTree<PayloadT>.dump(): NodeData<PayloadT>? = rootNode?.let { dump(it) }

fun <PayloadT> OrderedBinaryTree<PayloadT>.dump(
    node: Node<PayloadT>,
): NodeData<PayloadT> {
    val payload = node.payload
    val color = node.color
    val leftChild = node.leftChild
    val rightChild = node.rightChild

    return NodeData(
        payload = payload,
        color = color,
        leftChild = leftChild?.let { dump(node = it) },
        rightChild = rightChild?.let { dump(node = it) },
    )
}

fun <PayloadT> OrderedBinaryTree.Companion.load(
    rootData: NodeData<PayloadT>,
): OrderedBinaryTree<PayloadT> {
    val tree = OrderedBinaryTree.create<PayloadT>()

    rootData.put(
        tree = tree,
        location = OrderedBinaryTree.RootLocation.cast(),
    )

    return tree
}
