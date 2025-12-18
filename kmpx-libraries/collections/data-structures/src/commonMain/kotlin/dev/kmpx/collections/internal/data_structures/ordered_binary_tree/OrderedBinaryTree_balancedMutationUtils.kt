package dev.kmpx.collections.internal.data_structures.ordered_binary_tree

fun <PayloadT> OrderedBinaryTree<PayloadT>.insertAll(
    location: OrderedBinaryTree.Location<PayloadT>,
    payloads: Iterable<PayloadT>,
) {
    insertAllRecursively(
        location = location,
        payloadIterator = payloads.iterator(),
    )
}

private tailrec fun <PayloadT> OrderedBinaryTree<PayloadT>.insertAllRecursively(
    location: OrderedBinaryTree.Location<PayloadT>,
    payloadIterator: Iterator<PayloadT>,
) {
    if (!payloadIterator.hasNext()) return

    val nextPayload = payloadIterator.next()

    val insertedNode = insert(
        location = location,
        payload = nextPayload,
    )

    insertAllRecursively(
        location = getNextInOrderFreeLocation(
            node = insertedNode,
            side = OrderedBinaryTree.Side.Right,
        ),
        payloadIterator = payloadIterator,
    )
}

fun <PayloadT> OrderedBinaryTree<PayloadT>.takeOut(
    node: OrderedBinaryTree.Node<PayloadT>,
): PayloadT {
    val takenPayload = node.payload

    remove(node = node)

    return takenPayload
}

fun <PayloadT> OrderedBinaryTree<PayloadT>.insertRelative(
    node: OrderedBinaryTree.Node<PayloadT>,
    side: OrderedBinaryTree.Side,
    payload: PayloadT,
): OrderedBinaryTree.Node<PayloadT> {
    val location = getNextInOrderFreeLocation(
        node = node,
        side = side,
    )

    return insert(
        location = location,
        payload = payload,
    )
}
