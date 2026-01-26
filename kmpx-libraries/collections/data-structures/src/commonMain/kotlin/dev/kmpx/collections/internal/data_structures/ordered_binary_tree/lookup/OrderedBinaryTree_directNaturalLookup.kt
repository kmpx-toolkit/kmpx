package dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree.Node
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.insert
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.resolve

/**
 * Finds the location of the [payload] in a binary tree, assuming that the payloads are fully comparable and that the
 * tree's structural order is the same as the natural order of the payloads.
 */
fun <PayloadT : Comparable<PayloadT>> OrderedBinaryTree<PayloadT>.find(
    payload: PayloadT,
): OrderedBinaryTree.Location<PayloadT> = findLocationGuided(
    navigator = IntrinsicOrderNavigator(
        locatedPayload = payload,
    ),
)

fun <PayloadT: Comparable<PayloadT>> OrderedBinaryTree<PayloadT>.insertFindingLocation(
    payload: PayloadT,
): Node<PayloadT>? {
    val location = find(payload)

    val existingNode = resolve(location = location)

    if (existingNode != null) {
        return null
    }

    val insertedNode = insert(
        location = location,
        payload = payload,
    )

    return insertedNode
}

private class IntrinsicOrderNavigator<PayloadT : Comparable<PayloadT>>(
    private val locatedPayload: PayloadT,
) : OrderedBinaryTreeNavigator<PayloadT> {
    override fun instruct(
        payload: PayloadT,
    ): OrderedBinaryTreeNavigator.Command = OrderedBinaryTreeNavigator.Command.comparing(
        expected = locatedPayload,
        actual = payload,
    )
}
