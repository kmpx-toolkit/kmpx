package dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree

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
