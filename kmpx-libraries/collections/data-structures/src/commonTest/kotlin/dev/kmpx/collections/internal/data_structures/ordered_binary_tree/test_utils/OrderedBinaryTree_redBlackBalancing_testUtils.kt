package dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree.Node
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.insert
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.remove
import kotlin.random.Random

private typealias RedBlackBinaryTree<PayloadT> = OrderedBinaryTree<PayloadT>

private typealias MutableRedBlackBinaryTree<PayloadT> = OrderedBinaryTree<PayloadT>

private data class ColorVerificationResult(
    val blackHeight: Int,
)

fun <PayloadT : Comparable<PayloadT>> MutableRedBlackBinaryTree<PayloadT>.insertVerified(
    location: OrderedBinaryTree.Location<PayloadT>,
    payload: PayloadT,
): Node<PayloadT> {
    val insertedNode = insert(
        location = location,
        payload = payload,
    )

    verify()

    return insertedNode
}

fun <PayloadT : Comparable<PayloadT>> MutableRedBlackBinaryTree<PayloadT>.removeVerified(
    node: Node<PayloadT>,
): Node<PayloadT>? {
    val predecessorNode = remove(
        node = node,
    )

    verify()

    return predecessorNode
}

fun <PayloadT> RedBlackBinaryTree<PayloadT>.verify() {
    verifyIntegrity()
    verifyBalance()
    verifyColor()
}

private fun <PayloadT> RedBlackBinaryTree<PayloadT>.verifyColor() {
    val rootNode = this.rootNode ?: return

    verifySubtreeColor(parentColor = null, rootNode)
}

private fun <PayloadT> RedBlackBinaryTree<PayloadT>.verifySubtreeColor(
    parentColor: Node.Color?,
    node: Node<PayloadT>,
): ColorVerificationResult {
    val nodeColor = node.color

    if (parentColor == Node.Color.Red && nodeColor == Node.Color.Red) {
        throw AssertionError("Red node cannot have a red parent")
    }

    val leftChildNode = node.leftChild
    val rightChildNode = node.rightChild

    val leftSubtreeVerificationResult = leftChildNode?.let {
        verifySubtreeColor(
            parentColor = nodeColor,
            node = it,
        )
    }

    val rightSubtreeVerificationResult = rightChildNode?.let {
        verifySubtreeColor(
            parentColor = nodeColor,
            node = it,
        )
    }

    val leftSubtreeBlackHeight = leftSubtreeVerificationResult?.blackHeight ?: 1
    val rightSubtreeBlackHeight = rightSubtreeVerificationResult?.blackHeight ?: 1

    val ownBlackHeight = when (nodeColor) {
        Node.Color.Red -> 0
        Node.Color.Black -> 1
    }

    if (leftSubtreeBlackHeight != rightSubtreeBlackHeight) {
        throw AssertionError("Left and right subtrees must have the same black height, but left: $leftSubtreeBlackHeight, right: $rightSubtreeBlackHeight")
    } else {
        return ColorVerificationResult(
            blackHeight = leftSubtreeBlackHeight + ownBlackHeight,
        )
    }
}

object RedBlackTreeTestUtils {
    fun <PayloadT> loadVerified(
        rootData: NodeData<PayloadT>,
    ): MutableRedBlackBinaryTree<PayloadT> {
        val tree = OrderedBinaryTree.load(
            rootData = rootData,
        )

        tree.verify()

        return tree
    }

    fun buildBalance(
        requiredBlackDepth: Int,
        payloadRange: ClosedFloatingPointRange<Double>,
    ): NodeData<Double>? = buildBalance(
        random = Random(seed = 0), // Pass an explicit seed to make things deterministic
        requiredBlackDepth = requiredBlackDepth,
        payloadRange = payloadRange,
        parentColor = Node.Color.Red, // Assume a red parent to avoid red violation
    )

    private fun buildBalance(
        random: Random,
        requiredBlackDepth: Int,
        payloadRange: ClosedFloatingPointRange<Double>,
        parentColor: Node.Color = Node.Color.Red,
    ): NodeData<Double>? {
        require(requiredBlackDepth >= 1)

        if (requiredBlackDepth == 1) {
            return null
        }

        val (leftPayloadRange, rightPayloadRange) = payloadRange.split()

        val color = when (parentColor) {
            Node.Color.Red -> Node.Color.Black

            else -> {
                val x = random.nextDouble()

                when {
                    x < 0.4 -> Node.Color.Red
                    else -> Node.Color.Black
                }
            }
        }

        val newRequiredBlackDepth = when (color) {
            Node.Color.Black -> requiredBlackDepth - 1
            else -> requiredBlackDepth
        }

        return NodeData(
            payload = rightPayloadRange.start,
            color = color,
            leftChild = buildBalance(
                random = random,
                requiredBlackDepth = newRequiredBlackDepth,
                payloadRange = leftPayloadRange,
                parentColor = color,
            ),
            rightChild = buildBalance(
                random = random,
                requiredBlackDepth = newRequiredBlackDepth,
                payloadRange = rightPayloadRange,
                parentColor = color,
            ),
        )
    }
}

private fun ClosedFloatingPointRange<Double>.split(): Pair<ClosedFloatingPointRange<Double>, ClosedFloatingPointRange<Double>> {
    val midpoint = this.midpoint

    return Pair(
        start..midpoint,
        midpoint..endInclusive,
    )
}

private val ClosedFloatingPointRange<Double>.midpoint: Double
    get() = averageOf(start, endInclusive)

private fun averageOf(
    a: Double,
    b: Double,
): Double = (a + b) / 2.0
