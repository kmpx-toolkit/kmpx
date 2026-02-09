package dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.getChildLocation
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.resolve

/**
 * Finds the location of a node in a binary tree by a comparator bound to the searched payload. Assumes that the tree's
 * structural order corresponds to the order defined by the comparator (meaning that the defined order must be strict).
 *
 * The first node considered equal will be returned, which means that this operator will give non-deterministic results
 * in the case of multiple existing payloads equal order-wise. If it's known that the tree's order is non-strict,
 * [findWithLeaning] should be used instead to guarantee deterministic results.
 *
 * In the case when no payload equal order-wise to the searched payload exists in the tree, the empty location where
 * such payload could be inserted is returned.
 */
fun <PayloadT> OrderedBinaryTree<PayloadT>.findWith(
    comparator: BoundComparator<PayloadT>,
): OrderedBinaryTree.Location<PayloadT> = findWithRecursive(
    comparator = comparator,
    location = OrderedBinaryTree.RootLocation.cast(),
)

/**
 * Starting from the given [location], search for the location of searched payload using the given [comparator].
 */
private tailrec fun <PayloadT> OrderedBinaryTree<PayloadT>.findWithRecursive(
    comparator: BoundComparator<PayloadT>,
    location: OrderedBinaryTree.Location<PayloadT>,
): OrderedBinaryTree.Location<PayloadT> {
    val resolvedNode: OrderedBinaryTree.Node<PayloadT> = resolve(
        location = location,
    ) ?: return location

    val comparisonResult: Int = comparator.compare(resolvedNode.payload)

    when {
        // (resolved payload < searched payload)
        comparisonResult < 0 -> {
            // Turn right
            return findWithRecursive(
                comparator = comparator,
                location = resolvedNode.getChildLocation(
                    side = OrderedBinaryTree.Side.Right,
                ),
            )
        }

        // (resolved payload > searched payload)
        comparisonResult > 0 -> {
            // Turn left
            return findWithRecursive(
                comparator = comparator,
                location = resolvedNode.getChildLocation(
                    side = OrderedBinaryTree.Side.Left,
                ),
            )
        }

        // (resolved payload = searched payload) [order-wise]
        else -> {
            // Return the current location
            return location
        }
    }
}
