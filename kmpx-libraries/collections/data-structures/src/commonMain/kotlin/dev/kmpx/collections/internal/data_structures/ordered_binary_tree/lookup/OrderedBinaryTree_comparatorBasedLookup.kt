package dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree

/**
 * Finds the location of a payload in a binary tree by a comparator. Assumes that the tree's structural order
 * corresponds to the order defined by the comparator.
 */
fun <PayloadT> OrderedBinaryTree<PayloadT>.findWith(
    payload: PayloadT,
    comparator: Comparator<PayloadT>,
): OrderedBinaryTree.Location<PayloadT> = findLocationGuided(
    navigator = ComparatorOrderNavigator(
        locatedPayload = payload,
        comparator = comparator,
    ),
)

private class ComparatorOrderNavigator<PayloadT>(
    private val locatedPayload: PayloadT,
    private val comparator: Comparator<PayloadT>,
) : OrderedBinaryTreeNavigator<PayloadT> {
    override fun instruct(
        payload: PayloadT,
    ): OrderedBinaryTreeNavigator.Command = OrderedBinaryTreeNavigator.Command.comparing(
        expected = locatedPayload,
        actual = payload,
        comparator = comparator,
    )
}
