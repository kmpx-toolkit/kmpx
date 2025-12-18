package dev.kmpx.collections.internal.data_structures.ordered_binary_tree

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree.Node
import dev.kmpx.collections.internal.utils.assert

fun <PayloadT> OrderedBinaryTree<PayloadT>.remove(
    node: Node<PayloadT>,
): Node<PayloadT>? {
    val leftChildNode = node.leftChild
    val rightChildNode = node.rightChild

    when {
        leftChildNode != null -> {
            when {
                rightChildNode != null -> { // left: non-null, right: non-null
                    // If the node has two children, we can't directly remove it, but we can swap it with its
                    // in-order neighbor. As the node has children on both sides, we can pick the neighbour we prefer.
                    // As the removal procedure has to return a handle to the removed node's predecessor, we choose
                    // the predecessor here (i.e. left side).
                    val predecessorNode = swap(
                        sourceNode = node,
                        side = OrderedBinaryTree.Side.Left,
                    )

                    assert(node.rightChild == null) {
                        "Node shouldn't have a right child after the swap"
                    }

                    when (val newLeftChild = node.leftChild) {
                        // The node is a leaf after the swap
                        null -> {
                            cutOffLeafAndRebalance(
                                leafNode = node,
                            )
                        }

                        // The node has only one child after the swap
                        else -> {
                            collapseAndRebalance(
                                topNode = node,
                                bottomNode = newLeftChild,
                                side = OrderedBinaryTree.Side.Left,
                            )
                        }
                    }

                    return predecessorNode
                }

                else -> { // left: non-null, right: null
                    // The node has a single child on the left side, so we can collapse it
                    val predecessorNode = collapseAndRebalance(
                        topNode = node,
                        bottomNode = leftChildNode,
                        side = OrderedBinaryTree.Side.Left,
                    )

                    return predecessorNode
                }
            }
        }

        else -> {
            when {
                rightChildNode != null -> { // left: null, right: non-null
                    // The node has a single child on the right side, so we can collapse it
                    val successorNode = collapseAndRebalance(
                        topNode = node,
                        bottomNode = rightChildNode,
                        side = OrderedBinaryTree.Side.Right,
                    )

                    // The in-order predecessor (if present) will be directly accessible via the up-link, as the
                    // successor can't have a left child
                    return successorNode.getInOrderPredecessor()
                }

                else -> { // left: null, right: null
                    // The in-order predecessor (if present) will be directly accessible via the up-link, as a leaf
                    // node has no children
                    val predecessorNode = node.getInOrderPredecessor()

                    // The node is a leaf, so we can simply cut it off
                    cutOffLeafAndRebalance(
                        leafNode = node,
                    )

                    return predecessorNode
                }
            }
        }
    }
}


fun <PayloadT> OrderedBinaryTree<PayloadT>.insert(
    location: OrderedBinaryTree.Location<PayloadT>,
    payload: PayloadT,
): Node<PayloadT> {
    val attachedNode = attach(
        location = location,
        payload = payload,
        color = Node.Color.Red,
    )

    // Rebalance the tree after insertion
    this.rebalanceAfterAttach(
        attachedNode = attachedNode,
    )

    return attachedNode
}


private fun <PayloadT> OrderedBinaryTree<PayloadT>.cutOffLeafAndRebalance(
    leafNode: Node<PayloadT>,
): OrderedBinaryTree.Location<PayloadT> {
    val leafColor = leafNode.color

    val cutOffLeafLocation = cutOffLeaf(
        leafNode = leafNode,
    )

    if (cutOffLeafLocation is OrderedBinaryTree.RelativeLocation) {
        rebalanceAfterCutOff(
            cutOffLeafLocation = cutOffLeafLocation,
            cutOffLeafColor = leafColor,
        )
    }

    return cutOffLeafLocation
}

private fun <PayloadT> OrderedBinaryTree<PayloadT>.collapseAndRebalance(
    topNode: Node<PayloadT>,
    bottomNode: Node<PayloadT>,
    side: OrderedBinaryTree.Side,
): Node<PayloadT> {
    val neighbourNode = collapse(
        topNode = topNode,
        side = side,
    )

    this.rebalanceAfterCollapse(
        elevatedNode = bottomNode,
    )

    return neighbourNode
}
