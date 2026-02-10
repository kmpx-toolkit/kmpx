package dev.kmpx.collections

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.getRank
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup.BoundComparator
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup.findExactWith
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.resolve
import dev.kmpx.collections.SortedCollections.RankKind
import dev.kmpx.collections.SortedCollections.RankResult

object SortedCollections {
    enum class RankKind {
        /**
         * Rank of an element or entry contained in the container.
         */
        Existing,

        /**
         * Rank of an element or entry that is not contained in the container, but would be inserted at the given rank.
         */
        Potential,
    }

    data class RankResult(
        val rank: Int,
        val kind: RankKind,
    )
}

internal fun <PayloadT> OrderedBinaryTree<PayloadT>.findRank(
    comparator: BoundComparator<PayloadT>
): RankResult {
    val location = findExactWith(
        comparator = comparator,
    )

    val resolvedNode = resolve(location = location)

    return when {
        resolvedNode != null -> RankResult(
            rank = getRank(node = resolvedNode),
            kind = RankKind.Existing,
        )

        else -> RankResult(
            rank = when (location) {
                OrderedBinaryTree.RootLocation -> 0

                is OrderedBinaryTree.RelativeLocation -> {
                    val potentialParentRank = getRank(location.parentNode)

                    when (location.side) {
                        OrderedBinaryTree.Side.Left -> potentialParentRank
                        OrderedBinaryTree.Side.Right -> potentialParentRank + 1
                    }
                }
            },
            kind = RankKind.Potential,
        )
    }
}
