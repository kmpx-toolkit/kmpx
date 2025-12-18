package dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.getChildLocation
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.resolve

/**
 * A navigator that can be used to find a specific location in the binary tree. Navigators typically take some
 * assumptions about the structure of the tree, i.e. how the node's position in the order corresponds to its payload.
 */
internal interface OrderedBinaryTreeNavigator<in PayloadT> {
    /**
     * An instruction on how to proceed with the search in a binary tree.
     */
    sealed class Command {
        /**
         * An instruction to turn to (recurse to) a side of the tree.
         */
        data class Turn(
            /**
             * The side of the tree to turn to
             */
            val side: OrderedBinaryTree.Side,
        ) : Command()

        /**
         * An instruction to stop, meaning that the payload has been found
         */
        data object Stop : Command()

        companion object {
            fun <T : Comparable<T>> comparing(
                expected: T,
                actual: T,
            ): Command {
                val result = expected.compareTo(actual)

                return when {
                    result == 0 -> Stop

                    else -> Turn(
                        side = when {
                            result < 0 -> OrderedBinaryTree.Side.Left
                            else -> OrderedBinaryTree.Side.Right
                        },
                    )
                }
            }

            fun <T> comparing(
                expected: T,
                actual: T,
                comparator: Comparator<T>,
            ): Command {
                val result = comparator.compare(expected, actual)

                return when {
                    result == 0 -> Stop

                    else -> Turn(
                        side = when {
                            result < 0 -> OrderedBinaryTree.Side.Left
                            else -> OrderedBinaryTree.Side.Right
                        },
                    )
                }
            }
        }
    }

    /**
     * Instructs on how to proceed with the given [payload].
     */
    fun instruct(
        payload: PayloadT,
    ): Command
}

internal fun <PayloadT> OrderedBinaryTree<PayloadT>.findLocationGuided(
    navigator: OrderedBinaryTreeNavigator<PayloadT>,
): OrderedBinaryTree.Location<PayloadT> = this.findLocationGuidedRecursive(
    location = OrderedBinaryTree.RootLocation.cast(),
    navigator = navigator,
)

private tailrec fun <PayloadT> OrderedBinaryTree<PayloadT>.findLocationGuidedRecursive(
    location: OrderedBinaryTree.Location<PayloadT>,
    navigator: OrderedBinaryTreeNavigator<PayloadT>,
): OrderedBinaryTree.Location<PayloadT> {
    val node = resolve(
        location = location,
    ) ?: return location

    val payload = node.payload

    val command = navigator.instruct(
        payload = payload,
    )

    when (command) {
        OrderedBinaryTreeNavigator.Command.Stop -> return location

        is OrderedBinaryTreeNavigator.Command.Turn -> {
            val childLocation = node.getChildLocation(
                side = command.side,
            )

            return findLocationGuidedRecursive(
                location = childLocation,
                navigator = navigator,
            )
        }
    }
}
