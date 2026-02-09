package dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.getChildLocation
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.locate
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.resolve

/**
 * Finds the location of a node in a binary tree by a comparator bound to the searched payload. Assumes that the tree's
 * structural order agrees with the order defined by the comparator, but doesn't require that order to be strict.
 *
 * In the case where multiple existing payloads are equal order-wise to the searched payload, the [leanSide] parameter
 * defines which side to lean to when searching for the utmost payload equal order-wise. If it's known that the tree's
 * order is strict (never containing multiple payloads equal order-wise), [findWith] should be used instead for better
 * performance and simplicity.
 *
 * In the case when no payload equal order-wise to the searched payload exists in the tree, the empty location where
 * such payload could be inserted is returned.
 */
fun <PayloadT> OrderedBinaryTree<PayloadT>.findWithLeaning(
    comparator: BoundComparator<PayloadT>,
    leanSide: OrderedBinaryTree.Side,
): OrderedBinaryTree.Location<PayloadT> = findWithLeaningRecursive(
    comparator = comparator,
    leanSide = leanSide,
    location = OrderedBinaryTree.RootLocation.cast(),
    bestFoundNode = null,
)

/**
 * Starting from the given [location], search for the location of searched payload using the given [comparator],
 * leaning to the given [leanSide] in case of multiple payloads equal order-wise.
 */
private tailrec fun <PayloadT> OrderedBinaryTree<PayloadT>.findWithLeaningRecursive(
    comparator: BoundComparator<PayloadT>,
    leanSide: OrderedBinaryTree.Side,
    location: OrderedBinaryTree.Location<PayloadT>,
    bestFoundNode: OrderedBinaryTree.Node<PayloadT>?,
): OrderedBinaryTree.Location<PayloadT> {
    val resolvedNode: OrderedBinaryTree.Node<PayloadT> = resolve(
        location = location,
    ) ?: return when (bestFoundNode) {
            // We didn't find any node equal-order wise, so let's return the location appropriate for insertion
            null -> location

            // We found at least one node equal-order wise, so let's return its location
            else -> bestFoundNode.locate()
        }

    val comparisonResult: Int = comparator.compare(resolvedNode.payload)

    when {
        // (resolved payload < searched payload)
        comparisonResult < 0 -> {
            // Turn right, discard the resolved node
            return findWithLeaningRecursive(
                comparator = comparator,
                leanSide = leanSide,
                location = resolvedNode.getChildLocation(
                    side = OrderedBinaryTree.Side.Right,
                ),
                bestFoundNode = bestFoundNode,
            )
        }

        // (resolved payload > searched payload)
        comparisonResult > 0 -> {
            // Turn left, discard the resolved node
            return findWithLeaningRecursive(
                comparator = comparator,
                leanSide = leanSide,
                location = resolvedNode.getChildLocation(
                    side = OrderedBinaryTree.Side.Left,
                ),
                bestFoundNode = bestFoundNode,
            )
        }

        // (resolved payload = searched payload) [order-wise]
        else -> {
            // Turn in the lean direction, consider the found node the best one so far
            return findWithLeaningRecursive(
                comparator = comparator,
                leanSide = leanSide,
                location = resolvedNode.getChildLocation(
                    side = leanSide,
                ),
                bestFoundNode = resolvedNode,
            )
        }
    }
}
