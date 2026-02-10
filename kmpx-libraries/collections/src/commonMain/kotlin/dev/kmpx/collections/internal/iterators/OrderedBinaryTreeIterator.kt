package dev.kmpx.collections.internal.iterators

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree

internal class OrderedBinaryTreeIterator<ElementT> internal constructor(
    tree: OrderedBinaryTree<ElementT>,
) : AbstractOrderedBinaryTreeIterator<ElementT, ElementT>(
    tree = tree,
) {
    override fun extract(payload: ElementT): ElementT = payload
}
