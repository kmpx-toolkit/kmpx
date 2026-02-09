package dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree

/**
 * Finds the ceil node (if it exists) for the reference payload in a binary tree using a comparator bound to the
 * reference payload. Assumes that the tree's structural order corresponds to the order defined by the comparator
 * (meaning that the defined order must be strict).
 *
 * The _ceil node_ for a given payload is the left-most node in the tree whose payload is greater than or equal to that
 * payload.
 *
 * When a node equal order-wise is encountered during the search, it will be immediately returned as the result, which
 * means that this operator will give non-deterministic results in the case of multiple existing payloads equal
 * order-wise.
 *
 * In the case when no payload greater than or equal to the reference payload exists in the tree, `null` will be returned.
 */
fun <PayloadT> OrderedBinaryTree<PayloadT>.findCeilWith(
    comparator: BoundComparator<PayloadT>,
): OrderedBinaryTree.Node<PayloadT>? = findCeilWithRecursive(
    comparator = comparator,
    node = rootNode,
    bestFoundNode = null,
)

/**
 * Starting from the given [node], search for the ceil node using the given [comparator].
 */
private tailrec fun <PayloadT> OrderedBinaryTree<PayloadT>.findCeilWithRecursive(
    comparator: BoundComparator<PayloadT>,
    node: OrderedBinaryTree.Node<PayloadT>?,
    bestFoundNode: OrderedBinaryTree.Node<PayloadT>?,
): OrderedBinaryTree.Node<PayloadT>? {
    val resolvedNode: OrderedBinaryTree.Node<PayloadT> = node ?: return bestFoundNode

    val comparisonResult: Int = comparator.compare(resolvedNode.payload)

    when {
        // (reference payload < resolved payload)
        comparisonResult < 0 -> {
            // Turn right, discard the resolved node
            return findCeilWithRecursive(
                comparator = comparator,
                node = resolvedNode.rightChild,
                bestFoundNode = bestFoundNode,
            )
        }

        // (resolved payload > reference payload)
        comparisonResult > 0 -> {
            // Turn left, consider the found node the best one so far
            return findCeilWithRecursive(
                comparator = comparator,
                node = resolvedNode.leftChild,
                bestFoundNode = resolvedNode,
            )
        }

        // (resolved payload = reference payload) [order-wise]
        else -> {
            // Found exact match, return immediately
            return resolvedNode
        }
    }
}

