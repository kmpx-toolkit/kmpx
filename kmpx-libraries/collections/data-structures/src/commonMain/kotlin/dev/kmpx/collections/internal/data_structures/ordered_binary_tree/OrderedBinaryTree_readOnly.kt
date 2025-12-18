package dev.kmpx.collections.internal.data_structures.ordered_binary_tree

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree.Node
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree.RelativeLocation
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree.RootLocation

fun <PayloadT> OrderedBinaryTree<PayloadT>.isEmpty(): Boolean = rootNode == null

fun <PayloadT> OrderedBinaryTree<PayloadT>.traverse(): Sequence<Node<PayloadT>> {
    val minimalNode = getLeftMostDescendant() ?: return emptySequence()

    return generateSequence(
        minimalNode,
    ) { node ->
        node.getInOrderSuccessor()
    }
}

fun <PayloadT> OrderedBinaryTree<PayloadT>.resolve(
    location: OrderedBinaryTree.Location<PayloadT>,
): Node<PayloadT>? = when (location) {
    RootLocation -> rootNode

    is RelativeLocation<PayloadT> -> {
        val parent = location.parentNode
        val side = location.side

        parent.getChild(
            side = side,
        )
    }
}

/**
 * Get the child node on the given [side] of the given [this@getChild].
 */
fun <PayloadT> Node<PayloadT>.getChild(
    side: OrderedBinaryTree.Side,
): Node<PayloadT>? = when (side) {
    OrderedBinaryTree.Side.Left -> leftChild
    OrderedBinaryTree.Side.Right -> rightChild
}

/**
 * Get children of this node, starting from the given [side]. The first child will be the "closer" child, the second
 * one will be the "distant" child.
 */
fun <PayloadT> Node<PayloadT>.getChildren(
    side: OrderedBinaryTree.Side,
): Pair<Node<PayloadT>?, Node<PayloadT>?> = when (side) {
    OrderedBinaryTree.Side.Left -> Pair(leftChild, rightChild)
    OrderedBinaryTree.Side.Right -> Pair(rightChild, leftChild)
}

/**
 * Get a relative location of the child on the given [side] of the node
 */
fun <PayloadT> Node<PayloadT>.getChildLocation(
    side: OrderedBinaryTree.Side,
): RelativeLocation<PayloadT> = RelativeLocation(
    parentNode = this,
    side = side,
)

fun <PayloadT> Node<PayloadT>.getLeftChildLocation(): RelativeLocation<PayloadT> =
    getChildLocation(
        side = OrderedBinaryTree.Side.Left,
    )

fun <PayloadT> Node<PayloadT>.getRightChildLocation(): RelativeLocation<PayloadT> =
    getChildLocation(
        side = OrderedBinaryTree.Side.Right,
    )

fun <PayloadT> Node<PayloadT>.getChildSide(
    childNode: Node<PayloadT>,
): OrderedBinaryTree.Side {
    val leftChildNode = getChild(
        side = OrderedBinaryTree.Side.Left,
    )

    val rightChildNode = getChild(
        side = OrderedBinaryTree.Side.Right,
    )

    return when (childNode) {
        leftChildNode -> OrderedBinaryTree.Side.Left

        rightChildNode -> OrderedBinaryTree.Side.Right

        else -> {
            throw IllegalArgumentException("The given node is not a child of the given parent")
        }
    }
}

fun <PayloadT> Node<PayloadT>.locate(): OrderedBinaryTree.Location<PayloadT> =
    locateRelatively() ?: RootLocation.cast()

/**
 * @return A relative location of the node associated with [this@locateRelatively] in the tree, or null if the node is the root of
 * the tree (i.e. has no parent).
 */
fun <PayloadT> Node<PayloadT>.locateRelatively(): RelativeLocation<PayloadT>? {
    val parentNode = parent ?: return null

    val side = parentNode.getChildSide(
        childNode = this,
    )

    return RelativeLocation(
        parentNode = parentNode,
        side = side,
    )
}

fun <PayloadT> OrderedBinaryTree<PayloadT>.getLeftMostDescendant(): Node<PayloadT>? = getSideMostDescendant(
    side = OrderedBinaryTree.Side.Left,
)

fun <PayloadT> OrderedBinaryTree<PayloadT>.getRightMostDescendant(): Node<PayloadT>? = getSideMostDescendant(
    side = OrderedBinaryTree.Side.Right,
)

fun <PayloadT> OrderedBinaryTree<PayloadT>.getSideMostDescendant(
    side: OrderedBinaryTree.Side,
): Node<PayloadT>? = getSideMostFreeLocation(
    side = side,
).parentNode

fun <PayloadT> OrderedBinaryTree<PayloadT>.getSideMostFreeLocation(
    side: OrderedBinaryTree.Side,
): OrderedBinaryTree.Location<PayloadT> {
    val root = this.rootNode ?: return RootLocation.cast()

    return getSideMostFreeLocation(
        node = root,
        side = side,
    )
}

fun <PayloadT> OrderedBinaryTree<PayloadT>.getSideMostFreeLocation(
    node: Node<PayloadT>,
    side: OrderedBinaryTree.Side,
): RelativeLocation<PayloadT> {
    val sideChildNode = node.getChild(
        side = side,
    ) ?: return RelativeLocation(
        parentNode = node,
        side = side,
    )

    return getSideMostFreeLocation(
        node = sideChildNode,
        side = side,
    )
}

fun <PayloadT> Node<PayloadT>.getInOrderPredecessor(): Node<PayloadT>? = getInOrderNeighbour(
    side = OrderedBinaryTree.Side.Left,
)

fun <PayloadT> Node<PayloadT>.hasInOrderPredecessor(): Boolean = hasInOrderNeighbour(
    side = OrderedBinaryTree.Side.Left,
)

fun <PayloadT> Node<PayloadT>.getInOrderSuccessor(): Node<PayloadT>? = getInOrderNeighbour(
    side = OrderedBinaryTree.Side.Right,
)

fun <PayloadT> Node<PayloadT>.hasInOrderSuccessor(): Boolean = hasInOrderNeighbour(
    side = OrderedBinaryTree.Side.Right,
)

fun <PayloadT> OrderedBinaryTree<PayloadT>.getNextInOrderFreeLocation(
    node: Node<PayloadT>,
    side: OrderedBinaryTree.Side,
): RelativeLocation<PayloadT> {
    val sideChildLocation = node.getChildLocation(side = side)
    val sideChildNode = sideChildLocation.occupyingNode ?: return sideChildLocation

    return getSideMostFreeLocation(
        node = sideChildNode,
        side = side.opposite,
    )
}
