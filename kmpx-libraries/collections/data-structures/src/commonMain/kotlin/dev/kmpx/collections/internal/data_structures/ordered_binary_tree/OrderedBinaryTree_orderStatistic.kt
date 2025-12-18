/**
 * Utils implementing the order statistic on ordered binary trees
 */
package dev.kmpx.collections.internal.data_structures.ordered_binary_tree

/**
 * Select the node at the given [index] in the binary tree's order.
 *
 * This operation is logarithmic in the size of the tree.
 *
 * @return the node at the given index, or null if the index is out of bounds.
 */
fun <PayloadT> OrderedBinaryTree<PayloadT>.select(
    index: Int,
): OrderedBinaryTree.Node<PayloadT>? {
    val rootNode = this.rootNode ?: return null

    return rootNode.selectRecursively(
        index = index,
    )
}

private tailrec fun <PayloadT> OrderedBinaryTree.Node<PayloadT>.selectRecursively(
    index: Int,
): OrderedBinaryTree.Node<PayloadT>? {
    val downRank = getDownRank()

    val leftChildNode = leftChild
    val rightChildNode = rightChild

    return when {
        index == downRank -> this

        index < downRank -> when (leftChildNode) {
            null -> null

            else -> leftChildNode.selectRecursively(
                index = index,
            )
        }

        else -> when (rightChildNode) {
            null -> null

            else -> rightChildNode.selectRecursively(
                index = index - downRank - 1,
            )
        }
    }
}

/**
 * Get the rank of the node corresponding to the given [node] in the whole tree (the number of nodes that are
 * preceding it in an in-order traversal)
 *
 * This operation is logarithmic in the size of the tree.
 *
 * @return the rank of the given node
 */
@Suppress("UnusedReceiverParameter")
fun <PayloadT> OrderedBinaryTree<PayloadT>.getRank(
    node: OrderedBinaryTree.Node<PayloadT>,
): Int = node.getRankRecursively()

private fun <PayloadT> OrderedBinaryTree.Node<PayloadT>.getRankRecursively(): Int {
    val downRank = this.getDownRank()
    val upRank = this.getUpRankRecursively()

    return downRank + upRank
}

/**
 * Get the rank of the node corresponding to the given [this@getUpRank] in its supertree (the whole tree minus the node's
 * descendants)
 */
private tailrec fun <PayloadT> OrderedBinaryTree.Node<PayloadT>.getUpRankRecursively(): Int {
    val relativeLocation = locateRelatively() ?: return 0

    val parentNode = relativeLocation.parentNode
    val side = relativeLocation.side

    return when (side) {
        OrderedBinaryTree.Side.Left -> parentNode.getUpRankRecursively()
        OrderedBinaryTree.Side.Right -> parentNode.getRankRecursively() + 1
    }
}

/**
 * Get the rank of the node corresponding to the given [this@getDownRank] in its own subtree
 */
private fun <PayloadT> OrderedBinaryTree.Node<PayloadT>.getDownRank(): Int {
    val leftChildNode = leftChild

    return leftChildNode?.subtreeSize ?: 0
}
