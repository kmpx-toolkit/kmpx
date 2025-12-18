package dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree
import kotlin.random.Random

/**
 * Finds a random free location in the binary tree by navigating randomly until a free location is reached. Takes no
 * assumptions about the structure of the tree.
 *
 * The probability distribution is non-uniform, meaning that the chance on reaching a given free location might be
 * different for different locations.
 */
fun <PayloadT> OrderedBinaryTree<PayloadT>.getRandomFreeLocation(
    random: Random,
): OrderedBinaryTree.Location<PayloadT> = findLocationGuided(
    navigator = RandomNavigator(
        random = random,
    ),
)

private class RandomNavigator<PayloadT>(
    private val random: Random,
) : OrderedBinaryTreeNavigator<PayloadT> {
    override fun instruct(
        payload: PayloadT,
    ): OrderedBinaryTreeNavigator.Command = OrderedBinaryTreeNavigator.Command.Turn(
        side = random.nextSide(),
    )
}

private fun Random.nextSide(): OrderedBinaryTree.Side = when (nextBoolean()) {
    true -> OrderedBinaryTree.Side.Left
    false -> OrderedBinaryTree.Side.Right
}
