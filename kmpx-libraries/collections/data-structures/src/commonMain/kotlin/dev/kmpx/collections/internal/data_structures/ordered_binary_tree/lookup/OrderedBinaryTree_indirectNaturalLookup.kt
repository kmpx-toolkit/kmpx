package dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree

/**
 * Finds the location of a payload in a binary tree by a comparable key. Assumes that the tree's structural order
 * corresponds to the natural order of the payload key.
 *
 * @param key The key to look for.
 * @param selector A function extracting a comparable key from the payload.
 */
fun <PayloadT, KeyT : Comparable<KeyT>> OrderedBinaryTree<PayloadT>.findBy(
    key: KeyT,
    selector: (PayloadT) -> KeyT,
): OrderedBinaryTree.Location<PayloadT> = findLocationGuided(
    navigator = KeyOrderNavigator(
        locatedKey = key,
        selector = selector,
    ),
)

private class KeyOrderNavigator<PayloadT, KeyT : Comparable<KeyT>>(
    private val locatedKey: KeyT,
    private val selector: (PayloadT) -> KeyT,
) : OrderedBinaryTreeNavigator<PayloadT> {
    override fun instruct(
        payload: PayloadT,
    ): OrderedBinaryTreeNavigator.Command = OrderedBinaryTreeNavigator.Command.comparing(
        expected = locatedKey,
        actual = selector(payload),
    )
}
