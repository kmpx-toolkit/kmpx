package dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.getInOrderPredecessor
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.getInOrderSuccessor
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.getRank
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.select
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.traverse

private data class IntegrityVerificationResult(
    val computedSubtreeSize: Int,
)

private data class BalanceVerificationResult(
    val computedSubtreeHeight: Int,
)

fun <PayloadT> OrderedBinaryTree<PayloadT>.verifyIntegrity() {
    val rootResult = this.rootNode?.let { rootNode ->
        verifySubtreeIntegrity(
            subtreeRootNode = rootNode,
            expectedParentNode = null,
        )
    }

    val computedTreeSize = rootResult?.computedSubtreeSize ?: 0

    val naivelyTraversedNodes = traverseNaively().toList()

    val traversedNodes = traverse().toList()

    if (naivelyTraversedNodes != traversedNodes) {
        throw AssertionError("Inconsistent traversal")
    }

    if (traversedNodes.size != size) {
        throw AssertionError("Inconsistent tree size, computed: ${computedTreeSize}, declared: $size")
    }

    if (traversedNodes.size != computedTreeSize) {
        throw AssertionError("Inconsistent tree size, computed: ${computedTreeSize}, traversal: ${traversedNodes.size}")
    }

    val uniqueNodes = traversedNodes.toSet()

    if (traversedNodes.size != uniqueNodes.size) {
        throw AssertionError("Traversal contains duplicate nodes")
    }

    naivelyTraversedNodes.asSequence().withNeighboursOrNull()
        .forEachIndexed { index, (naivePredecessorNode, node, naiveSuccessorNode) ->
            val predecessorNode = node.getInOrderPredecessor()

            if (predecessorNode != naivePredecessorNode) {
                throw AssertionError("Inconsistent predecessor for node $node, naive: $naivePredecessorNode, actual: $predecessorNode")
            }

            val successorNode = node.getInOrderSuccessor()

            if (successorNode != naiveSuccessorNode) {
                throw AssertionError("Inconsistent successor for node $node, naive: $naiveSuccessorNode, actual: $successorNode")
            }

            val selectedNode = select(index = index)

            if (selectedNode != node) {
                throw AssertionError("Inconsistent selection for index $index, naive: $node, actual: $selectedNode")
            }

            val rank = getRank(node = node)

            if (rank != index) {
                throw AssertionError("Inconsistent rank for node $node, expected: $index, actual: $rank")
            }
        }
}

private fun <PayloadT> OrderedBinaryTree<PayloadT>.verifySubtreeIntegrity(
    subtreeRootNode: OrderedBinaryTree.Node<PayloadT>,
    expectedParentNode: OrderedBinaryTree.Node<PayloadT>?,
): IntegrityVerificationResult {
    val actualParentNode = subtreeRootNode.parent

    if (!subtreeRootNode.isValid) {
        throw AssertionError("Invalid node: $subtreeRootNode")
    }

    if (actualParentNode != expectedParentNode) {
        throw AssertionError("Inconsistent parent")
    }

    val leftChildNode = subtreeRootNode.leftChild
    val rightChildNode = subtreeRootNode.rightChild

    val leftSubtreeVerificationResult = leftChildNode?.let {
        verifySubtreeIntegrity(
            subtreeRootNode = it,
            expectedParentNode = subtreeRootNode,
        )
    }

    val rightSubtreeVerificationResult = rightChildNode?.let {
        verifySubtreeIntegrity(
            subtreeRootNode = it,
            expectedParentNode = subtreeRootNode,
        )
    }

    val computedLeftSubtreeSize = leftSubtreeVerificationResult?.computedSubtreeSize ?: 0
    val computedRightSubtreeSize = rightSubtreeVerificationResult?.computedSubtreeSize ?: 0
    val computedTotalSubtreeSize = computedLeftSubtreeSize + computedRightSubtreeSize + 1

    val cachedSubtreeSize = subtreeRootNode.subtreeSize

    if (cachedSubtreeSize != computedTotalSubtreeSize) {
        throw AssertionError("Inconsistent subtree size, computed: $computedTotalSubtreeSize, cached: $cachedSubtreeSize")
    }

    return IntegrityVerificationResult(
        computedSubtreeSize = computedTotalSubtreeSize,
    )
}

fun <PayloadT> OrderedBinaryTree<PayloadT>.verifyBalance() {
    val rootNode = this.rootNode ?: return

    verifySubtreeBalance(
        subtreeRootNode = rootNode,
    )
}

private fun <PayloadT> OrderedBinaryTree<PayloadT>.verifySubtreeBalance(
    subtreeRootNode: OrderedBinaryTree.Node<PayloadT>,
): BalanceVerificationResult {
    val leftChildNode = subtreeRootNode.leftChild
    val rightChildNode = subtreeRootNode.rightChild

    val leftSubtreeVerificationResult = leftChildNode?.let {
        verifySubtreeBalance(
            subtreeRootNode = it,
        )
    }

    val rightSubtreeVerificationResult = rightChildNode?.let {
        verifySubtreeBalance(
            subtreeRootNode = it,
        )
    }

    val (minPathLength, maxPathLength) = Pair(
        leftSubtreeVerificationResult?.computedSubtreeHeight ?: 1,
        rightSubtreeVerificationResult?.computedSubtreeHeight ?: 1,
    ).sorted()

    if (maxPathLength > 2 * minPathLength) {
        throw AssertionError("Unbalanced subtree, min subtree height: $minPathLength, max subtree height: $maxPathLength")
    }

    return BalanceVerificationResult(
        computedSubtreeHeight = maxPathLength,
    )
}

fun <PayloadT : Comparable<PayloadT>> OrderedBinaryTree<PayloadT>.verifyOrder() {
    verifyOrderBy { it }
}

fun <PayloadT, KeyT : Comparable<KeyT>> OrderedBinaryTree<PayloadT>.verifyOrderBy(
    selector: (PayloadT) -> KeyT,
) {
    val rootNode = this.rootNode ?: return

    verifySubtreeOrder(
        subtreeRootNode = rootNode,
        selector = selector,
    )
}

private fun <PayloadT, KeyT : Comparable<KeyT>> OrderedBinaryTree<PayloadT>.verifySubtreeOrder(
    subtreeRootNode: OrderedBinaryTree.Node<PayloadT>,
    selector: (PayloadT) -> KeyT,
) {
    val payload = subtreeRootNode.payload
    val key = selector(payload)

    val leftChildNode = subtreeRootNode.leftChild
    val rightChildNode = subtreeRootNode.rightChild

    leftChildNode?.let {
        val leftPayload = it.payload
        val leftKey = selector(leftPayload)

        if (leftKey >= key) {
            throw AssertionError("Left child payload $leftPayload is not less than parent payload $payload")
        }

        verifySubtreeOrder(
            subtreeRootNode = it,
            selector = selector,
        )
    }

    rightChildNode?.let {
        val rightPayload = it.payload
        val rightKey = selector(rightPayload)

        if (rightKey <= key) {
            throw AssertionError("Right child payload $rightPayload is not greater than parent payload $payload")
        }

        verifySubtreeOrder(
            subtreeRootNode = it,
            selector = selector,
        )
    }
}
