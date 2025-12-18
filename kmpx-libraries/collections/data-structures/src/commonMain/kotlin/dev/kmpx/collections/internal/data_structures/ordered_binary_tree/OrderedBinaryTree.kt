package dev.kmpx.collections.internal.data_structures.ordered_binary_tree

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree.Node.Companion.linkDownAscendantNeighbour
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree.Node.Companion.linkDownChildSymmetrically
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree.Node.Companion.linkDownTransplantingSymmetrically
import dev.kmpx.collections.internal.utils.assert
import dev.kmpx.collections.internal.utils.fail
import kotlin.jvm.JvmInline

/**
 * An ordered binary tree with red-black node coloring and subtree size caching.
 *
 * This data structure is best known under the name "binary search tree", but this implementation supports trees that do
 * not have an inherent order and, in consequence, are not searchable. Utilities for BST-style lookup are provided
 * separately.
 *
 * This class offers only fundamental operations to manipulate the tree structure. It doesn't guarantee actual balancing
 * on its own. The high-level insertion/removal algorithms with proper red-black tree balancing are provided separately.
 *
 * Each node maintains the size of its subtree, enabling efficient order statistic operations. A data structure with
 * such capabilities is known as an "order statistic tree". The order statistic algorithms are provided separately.
 */
class OrderedBinaryTree<PayloadT> private constructor() {
    /**
     * A side of the tree or the tree's node, either left or right. Each node stores all nodes that compares less than
     * it (or equal) on the left side, and all nodes that compares greater than it (or equal) on the right side.
     */
    sealed class Side {
        data object Left : Side() {
            override val opposite: Side = Right

            override val directionTo = RotationDirection.CounterClockwise
        }

        data object Right : Side() {
            override val opposite: Side = Left

            override val directionTo = RotationDirection.Clockwise
        }

        /**
         * The opposite side to this side.
         */
        internal abstract val opposite: Side

        /**
         * The rotation direction that makes the parent take the position of the child on this side
         */
        internal abstract val directionTo: RotationDirection

        /**
         * The rotation direction that makes the child on this side take the position of its parent
         */
        internal val directionFrom: RotationDirection
            get() = directionTo.opposite
    }

    /**
     * A direction of rotation of a subtree around a pivot node.
     */
    sealed class RotationDirection {
        /**
         * Clockwise rotation direction
         */
        data object Clockwise : RotationDirection() {
            override val opposite = CounterClockwise

            override val startSide = Side.Left
        }

        /**
         * Counter-clockwise rotation direction
         */
        data object CounterClockwise : RotationDirection() {
            override val opposite = Clockwise

            override val startSide = Side.Right
        }

        /**
         * The opposite rotation direction
         */
        abstract val opposite: RotationDirection

        /**
         * The side of the pivot node where the ascending child is located
         */
        abstract val startSide: Side

        /**
         * The side of the pivot node where the descending child is located
         */
        val endSide: Side
            get() = startSide.opposite
    }

    /**
     * Location of a node inside the tree. The location can describe either a node currently present in the tree, or a
     * free location where a new node can be inserted.
     */
    sealed interface Location<PayloadT> {
        val parentNode: Node<PayloadT>?
    }

    /**
     * Location of the root node
     */
    data object RootLocation : Location<Nothing> {
        override val parentNode: Node<Nothing>? = null

        @Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
        inline fun <PayloadT> cast(): Location<PayloadT> = this as Location<PayloadT>
    }

    /**
     * Location relative to the parent node
     */
    data class RelativeLocation<PayloadT>(
        /**
         * The parent node
         */
        override val parentNode: Node<PayloadT>,
        /**
         * The side of the parent node where the child node is (or would be) located
         */
        val side: Side,
    ) : Location<PayloadT> {
        /**
         * The node currently occupying this location, or `null` if the location is free.
         */
        val occupyingNode: Node<PayloadT>?
            get() = parentNode.getChild(side)

        /**
         * The sibling node of the node occupying this location, or `null` if there is no sibling.
         */
        val occupyingNodeSibling: Node<PayloadT>?
            get() = parentNode.getChild(siblingSide)

        val siblingSide: Side
            get() = side.opposite
    }

    /**
     * A node in the ordered binary tree. Each node contains a payload of type [PayloadT]. The node also contains
     * metadata required to maintain the tree structure and balance.
     */
    class Node<PayloadT> internal constructor(
        initialParent: Node<PayloadT>?,
        initialColor: Color,
        initialPayload: PayloadT,
    ) {
        enum class Color {
            Red, Black,
        }

        enum class Validity {
            Valid, Invalid,
        }

        companion object {
            internal fun <PayloadT> linkDownAscendantNeighbour(
                descendantNode: Node<PayloadT>,
                side: Side,
                ascendantNeighbourNode: Node<PayloadT>,
            ) {
                linkDownAscendantNeighbour(
                    descendantNode = descendantNode,
                    side = side,
                    ascendantNeighbourLink = ProperNeighbourLink(
                        linkedNeighbour = ascendantNeighbourNode,
                    ),
                )
            }

            internal fun <PayloadT> linkDownAscendantNeighbour(
                descendantNode: Node<PayloadT>,
                side: Side,
                ascendantNeighbourLink: NeighbourLink<PayloadT>,
            ) {
                descendantNode.setDownLink(
                    downLink = ascendantNeighbourLink,
                    side = side,
                )
            }

            internal fun <PayloadT> linkDownChildSymmetrically(
                parentNode: Node<PayloadT>,
                side: Side,
                childNode: Node<PayloadT>,
            ) {
                parentNode.setDownLink(
                    downLink = ChildLink(child = childNode),
                    side = side,
                )

                childNode.setParent(
                    parent = parentNode,
                )
            }

            internal fun <PayloadT> linkDownTransplantingSymmetrically(
                upperNode: Node<PayloadT>,
                side: Side,
                transplantedLink: DownLink<PayloadT>,
            ) {
                upperNode.setDownLink(
                    downLink = transplantedLink,
                    side = side,
                )

                if (transplantedLink is ChildLink<PayloadT>) {
                    transplantedLink.child.setParent(
                        parent = upperNode,
                    )

                    val transplantedChild = transplantedLink.child

                    val transplantedNeighbourDescendant = transplantedChild.getSideMostDescendantOrSelf(
                        side = side.opposite,
                    )

                    linkDownAscendantNeighbour(
                        descendantNode = transplantedNeighbourDescendant,
                        side = side.opposite,
                        ascendantNeighbourNode = upperNode,
                    )
                }
            }
        }

        private var mutableValidity = Validity.Valid

        val isValid: Boolean
            get() = mutableValidity == Validity.Valid

        private fun requireValid() {
            if (mutableValidity == Validity.Invalid) {
                throw IllegalStateException("The node is already invalidated")
            }
        }

        fun invalidate() {
            if (mutableValidity == Validity.Invalid) {
                throw IllegalStateException("The node is already invalidated")
            }

            mutableValidity = Validity.Invalid
        }

        private var mutableParent: Node<PayloadT>? = initialParent

        val parent: Node<PayloadT>?
            get() = mutableParent

        internal fun setParent(
            parent: Node<PayloadT>?,
        ) {
            requireValid()

            if (parent == this) {
                throw IllegalArgumentException("Cannot set a node as its own parent")
            }

            mutableParent = parent
        }

        private var mutableLeftDownLink: DownLink<PayloadT> = NilNeighbourLink.cast()

        internal val leftDownLink: DownLink<PayloadT>
            get() = mutableLeftDownLink

        val leftChild: Node<PayloadT>?
            get() = mutableLeftDownLink.child

        private var mutableRightDownLink: DownLink<PayloadT> = NilNeighbourLink.cast()

        internal val rightDownLink: DownLink<PayloadT>
            get() = mutableRightDownLink

        val rightChild: Node<PayloadT>?
            get() = mutableRightDownLink.child

        internal fun getDownLink(
            side: Side,
        ): DownLink<PayloadT> = when (side) {
            Side.Left -> mutableLeftDownLink
            Side.Right -> mutableRightDownLink
        }

        internal fun getDownLinks(
            side: Side,
        ): Pair<DownLink<PayloadT>?, DownLink<PayloadT>?> = when (side) {
            Side.Left -> Pair(mutableLeftDownLink, mutableRightDownLink)
            Side.Right -> Pair(mutableRightDownLink, mutableLeftDownLink)
        }

        private fun setDownLink(
            downLink: DownLink<PayloadT>,
            side: Side,
        ) {
            requireValid()

            when (side) {
                Side.Left -> mutableLeftDownLink = downLink
                Side.Right -> mutableRightDownLink = downLink
            }
        }

        private var mutableSubtreeSize: Int = 1

        val subtreeSize: Int
            get() = mutableSubtreeSize

        internal fun setSubtreeSize(
            size: Int,
        ) {
            requireValid()

            require(size >= 0)

            mutableSubtreeSize = size
        }

        internal fun updateSubtreeSizeRecursively(
            /**
             * The number of gained descendants. If negative, it means the node lost descendants.
             */
            delta: Int,
        ) {
            requireValid()

            setSubtreeSize(subtreeSize + delta)

            parent?.updateSubtreeSizeRecursively(
                delta = delta,
            )
        }

        private var mutableColor: Color = initialColor

        val color: Color
            get() = mutableColor

        internal fun setColor(
            color: Color,
        ) {
            requireValid()

            mutableColor = color
        }

        private var mutablePayload: PayloadT = initialPayload

        val payload: PayloadT
            get() = mutablePayload

        fun setPayload(
            payload: PayloadT,
        ) {
            requireValid()

            mutablePayload = payload
        }

        fun getInOrderNeighbour(
            side: Side,
        ): Node<PayloadT>? = when (val sideLink = getDownLink(side = side)) {
            is ChildLink -> sideLink.child.getSideMostDescendantOrSelf(side.opposite)
            is NeighbourLink -> sideLink.linkedNeighbour
        }

        fun getSideMostDescendantOrSelf(
            side: Side,
        ): Node<PayloadT> {
            val sideChild = getChild(
                side = side,
            ) ?: return this

            return sideChild.getSideMostDescendantOrSelf(
                side = side,
            )
        }

        fun hasInOrderNeighbour(
            side: Side,
        ): Boolean {
            val downLink = getDownLink(side = side)

            return downLink != NilNeighbourLink
        }
    }

    /**
     * A link to either a child or a neighbour. This is a classic technique to represent binary trees, allowing
     * efficient tree traversal.
     */
    internal sealed interface DownLink<PayloadT> {
        val child: Node<PayloadT>?

        val linkedNeighbour: Node<PayloadT>?
    }

    /**
     * A link to a child on the given side.
     */
    @JvmInline
    internal value class ChildLink<PayloadT>(
        override val child: Node<PayloadT>,
    ) : DownLink<PayloadT> {
        override val linkedNeighbour: Nothing?
            get() = null
    }

    /**
     * A neighbour link pointing to the in-order neighbour (if present). A node is linking to its neighbour only if it
     * doesn't have a child on that side.
     */
    internal sealed interface NeighbourLink<PayloadT> : DownLink<PayloadT> {
        companion object {
            fun <PayloadT> of(neighbour: Node<PayloadT>?): NeighbourLink<PayloadT> = when (neighbour) {
                null -> NilNeighbourLink.cast()
                else -> ProperNeighbourLink(linkedNeighbour = neighbour)
            }
        }
    }

    /**
     * A proper link to the in-order neighbour.
     */
    @JvmInline
    internal value class ProperNeighbourLink<PayloadT>(
        override val linkedNeighbour: Node<PayloadT>,
    ) : NeighbourLink<PayloadT> {
        override val child: Nothing?
            get() = null
    }

    /**
     * A singleton representing the absence of an in-order neighbour. A node with such a link must be either the
     * left-most or the right-most node in the tree.
     */
    internal data object NilNeighbourLink : NeighbourLink<Nothing?> {
        @Suppress("NOTHING_TO_INLINE")
        inline fun <PayloadT> cast(): NeighbourLink<PayloadT> {
            @Suppress("UNCHECKED_CAST") return this as NeighbourLink<PayloadT>
        }

        override val child: Node<Nothing?>? = null

        override val linkedNeighbour: Node<Nothing?>? = null
    }

    companion object {
        internal fun <PayloadT> create(): OrderedBinaryTree<PayloadT> = OrderedBinaryTree()
    }

    private var mutableRootNode: Node<PayloadT>? = null

    /**
     * The current root node of the tree, or `null` if the tree is empty.
     */
    val rootNode: Node<PayloadT>?
        get() = mutableRootNode

    /**
     * The number of nodes currently present in the tree.
     */
    val size: Int
        get() = rootNode?.subtreeSize ?: 0

    /**
     * Removes the root from the tree (together with all its descendants), effectively emptying the tree. If the tree
     * is already empty, this operation has no effect.
     */
    fun cutOffRoot() {
        setRoot(
            newRoot = null,
        )
    }

    /**
     * Attach a new leaf with the given [payload] and [color] at the given free [location].
     *
     * @return The attached leaf
     * @throws IllegalArgumentException if the location is taken
     */
    internal fun attach(
        location: Location<PayloadT>,
        payload: PayloadT,
        color: Node.Color,
    ): Node<PayloadT> {
        val attachedNode = when (location) {
            RootLocation -> {
                if (rootNode != null) {
                    throw IllegalArgumentException("The tree already has a root")
                }

                val rootNode = Node(
                    initialParent = null,
                    initialColor = color,
                    initialPayload = payload,
                )

                setRoot(
                    newRoot = rootNode,
                )

                rootNode
            }

            is RelativeLocation<PayloadT> -> {
                val parent = location.parentNode
                val side = location.side

                val existingDownLink =
                    parent.getDownLink(side = side) as? NeighbourLink ?: throw IllegalArgumentException(
                        "Cannot insert leaf to a non-empty location"
                    )

                val newNode = Node(
                    initialParent = parent,
                    initialColor = color,
                    initialPayload = payload,
                )

                linkDownChildSymmetrically(
                    parentNode = parent,
                    side = side,
                    childNode = newNode,
                )

                linkDownAscendantNeighbour(
                    descendantNode = newNode,
                    side = side.opposite,
                    ascendantNeighbourNode = parent,
                )

                linkDownAscendantNeighbour(
                    descendantNode = newNode,
                    side = side,
                    ascendantNeighbourLink = existingDownLink,
                )

                parent.updateSubtreeSizeRecursively(
                    delta = +1,
                )

                newNode
            }
        }

        return attachedNode
    }

    /**
     * Remove the given [leafNode] from the tree.
     *
     * @throws IllegalArgumentException if the node is not really a leaf
     * @return The location where the leaf was before being cut off
     */
    internal fun cutOffLeaf(
        leafNode: Node<PayloadT>,
    ): Location<PayloadT> {
        val leafLocation = leafNode.locate()

        when (leafLocation) {
            is RelativeLocation -> {
                val parent = leafLocation.parentNode
                val side = leafLocation.side

                val (sideLink, oppositeLink) = leafNode.getDownLinks(side = side)

                val loopLink =
                    oppositeLink as? NeighbourLink ?: throw IllegalArgumentException("The node is not a leaf")

                assert(loopLink.linkedNeighbour == parent) {
                    "Inconsistent link structure"
                }

                val farLink = sideLink as? NeighbourLink ?: throw IllegalArgumentException("The node is not a leaf")

                linkDownAscendantNeighbour(
                    descendantNode = parent,
                    side = side,
                    ascendantNeighbourLink = farLink,
                )

                parent.updateSubtreeSizeRecursively(
                    delta = -1,
                )
            }

            RootLocation -> {
                val firstLink =
                    leafNode.leftDownLink as? NeighbourLink? ?: throw IllegalArgumentException("The node is not a leaf")

                val secondLink = leafNode.rightDownLink as? NeighbourLink?
                    ?: throw IllegalArgumentException("The node is not a leaf")

                assert(firstLink == NilNeighbourLink && secondLink == NilNeighbourLink) {
                    "Inconsistent link structure"
                }

                setRoot(null)
            }
        }

        leafNode.invalidate()

        return leafLocation
    }

    /**
     * Remove [topNode] from the tree by replacing it with its only child on the given [side].
     *
     * @throws IllegalArgumentException if the node is a leaf, has one child (but on the other side), or has two children
     * @return The node's in-order neighbour descendant on the given [side]
     */
    internal fun collapse(
        topNode: Node<PayloadT>,
        side: Side,
    ): Node<PayloadT> {
        val topNodeLocation = topNode.locate()

        val oppositeDownLink = topNode.getDownLink(
            side = side.opposite,
        ) as? NeighbourLink ?: throw IllegalArgumentException("The node has a child on the opposite side")

        val childLink = topNode.getDownLink(
            side = side,
        ) as? ChildLink ?: throw IllegalArgumentException("The node doesn't have a child on the given side")

        val singleChild = childLink.child

        val descendantNeighbour = singleChild.getSideMostDescendantOrSelf(side = side.opposite)

        val loopLink = descendantNeighbour.getDownLink(side = side.opposite)

        assert(loopLink.linkedNeighbour == topNode) {
            "Inconsistent loop link"
        }

        linkUpSymmetrically(
            nodeLocation = topNodeLocation,
            newNode = singleChild,
        )

        linkDownAscendantNeighbour(
            descendantNode = descendantNeighbour,
            side = side.opposite,
            ascendantNeighbourLink = oppositeDownLink,
        )

        topNodeLocation.parentNode?.updateSubtreeSizeRecursively(
            delta = -1,
        )

        topNode.invalidate()

        return descendantNeighbour
    }

    /**
     * Swap the [sourceNode] with its in-order neighbour descendant on the given [side]. This is a fundamental operation
     * required for removing nodes with two children.
     *
     * Doesn't affect the colors from the tree's structure perspective, meaning that the first node will have the second
     * node's original color after the swap and the second node will have the first node's original color.
     *
     * This method actually re-links the nodes instead of swapping their payloads. Both nodes will carry the original
     * payloads after the operation.
     *
     * @throws IllegalArgumentException if the node doesn't have any children on the given [side]
     * @return The in-order neighbour descendant node on the given [side] that the [sourceNode] was swapped with
     */
    internal fun swap(
        sourceNode: Node<PayloadT>,
        side: Side,
    ): Node<PayloadT> {
        // Phase 1: Remember all the necessary links and nodes before the swap

        // The source node's location before the swap
        val originalSourceNodeLocation = sourceNode.locate()

        // The source node's color & subtree size before the swap
        val originalSourceNodeColor = sourceNode.color
        val originalSourceNodeSubtreeSize = sourceNode.subtreeSize

        // The child on the side we're swapping on
        val originalSourceNodeSwapSideChild = sourceNode.getChild(side = side) ?: throw IllegalArgumentException(
            "The node doesn't have an in-order descendant neighbour on the $side side"
        )

        // The down-link on the opposite side (can be either a child link or a neighbour link)
        val originalSourceNodeOppositeSideDownLink = sourceNode.getDownLink(side = side.opposite)

        // The neighbour we'll be swapping with
        val targetNode = originalSourceNodeSwapSideChild.getSideMostDescendantOrSelf(side = side.opposite)

        // The target node's color & subtree size before the swap
        val originalTargetNodeColor = targetNode.color
        val originalTargetNodeSubtreeSize = targetNode.subtreeSize

        // The target's parent before the swap. If the source and the target node are separated by exactly one node,
        // this will be the top node's swap-side child.
        val originalTargetNodeParent =
            targetNode.parent ?: fail("The target node is not expected to be the root")

        // As the target node is the in-order neighbour descendant, its opposite down-link should be a neighbour link...
        val originalTargetLoopLink = targetNode.getDownLink(
            side = side.opposite,
        ) as? NeighbourLink ?: fail("The target node is not actually side-most")

        // ...pointing to the source node
        assert(originalTargetLoopLink.linkedNeighbour == sourceNode) {
            "Inconsistent loop link"
        }

        // The target's down-link on the swap side (can be either a child link or a neighbour link)
        val originalTargetSwapSideDownLink = targetNode.getDownLink(side = side)

        // Phase 2: Perform the swap

        // Link the target node with the original source node's parent (if present)
        linkUpSymmetrically(
            nodeLocation = originalSourceNodeLocation,
            newNode = targetNode,
        )

        // Move the source node's color & subtree size into the target node
        targetNode.setColor(originalSourceNodeColor)
        targetNode.setSubtreeSize(originalSourceNodeSubtreeSize)

        // Transplant the original source's down-link from the opposite side
        linkDownTransplantingSymmetrically(
            upperNode = targetNode,
            side = side.opposite,
            transplantedLink = originalSourceNodeOppositeSideDownLink,
        )

        // Link the interconnection(s)
        when {
            // The corner case when the nodes are adjacent. We reverse the parent/child link between them.
            originalSourceNodeSwapSideChild == targetNode -> {
                linkDownChildSymmetrically(
                    parentNode = targetNode,
                    side = side,
                    childNode = sourceNode,
                )
            }

            // The standard case, when they are separated by at least one node. We create two separate links.
            else -> {
                linkDownChildSymmetrically(
                    parentNode = targetNode,
                    side = side,
                    childNode = originalSourceNodeSwapSideChild,
                )

                linkDownChildSymmetrically(
                    parentNode = originalTargetNodeParent,
                    side = side.opposite,
                    childNode = sourceNode,
                )
            }
        }

        // Move the target node's color & subtree size into the source node
        sourceNode.setColor(originalTargetNodeColor)
        sourceNode.setSubtreeSize(originalTargetNodeSubtreeSize)

        // Reverse the loop link
        linkDownAscendantNeighbour(
            descendantNode = sourceNode,
            side = side.opposite,
            ascendantNeighbourNode = targetNode,
        )

        // Transplant the original target's down-link from the swap side
        linkDownTransplantingSymmetrically(
            upperNode = sourceNode,
            side = side,
            transplantedLink = originalTargetSwapSideDownLink,
        )

        return targetNode
    }

    /**
     * Rotate the subtree starting at node corresponding to [pivotNode] in the given direction.
     *
     * @return The root of the subtree after the rotation
     * @throws IllegalArgumentException if the pivot has no child on the respective side
     */
    internal fun rotate(
        pivotNode: Node<PayloadT>,
        direction: RotationDirection,
    ): Node<PayloadT> {
        val pivotNodeLocation = pivotNode.locate()

        val ascendingChild = pivotNode.getChild(side = direction.startSide)
            ?: throw IllegalArgumentException("The pivot node has no child on the ${direction.startSide} side")

        // If the downlink in the close grandchild's place was a neighbour link, it must link to the pivot node. This
        // link should be discarded.
        val closeGrandchild = ascendingChild.getChild(side = direction.endSide)

        val distantGrandchild = ascendingChild.getChild(side = direction.startSide)

        when (closeGrandchild) {
            null -> {
                linkDownAscendantNeighbour(
                    descendantNode = pivotNode,
                    side = direction.startSide,
                    ascendantNeighbourNode = ascendingChild,
                )
            }

            else -> {
                linkDownChildSymmetrically(
                    parentNode = pivotNode,
                    childNode = closeGrandchild,
                    side = direction.startSide,
                )
            }
        }

        linkDownChildSymmetrically(
            parentNode = ascendingChild,
            childNode = pivotNode,
            side = direction.endSide,
        )

        linkUpSymmetrically(
            nodeLocation = pivotNodeLocation,
            newNode = ascendingChild,
        )

        val originalPivotNodeSubtreeSize = pivotNode.subtreeSize
        val originalDistantGrandchildSubtreeSize = distantGrandchild?.subtreeSize ?: 0

        // The ascending node has exactly the same set of descendants as the pivot node had before (with the exception
        // that the parent-child relation inverted, but that doesn't affect the subtree size)
        ascendingChild.setSubtreeSize(
            size = originalPivotNodeSubtreeSize,
        )

        // The pivot node lost descendants in the subtree of its original distant grandchild. It also lost the ascending
        // child.
        pivotNode.setSubtreeSize(
            size = originalPivotNodeSubtreeSize - originalDistantGrandchildSubtreeSize - 1,
        )

        return ascendingChild
    }

    private fun linkUpSymmetrically(
        nodeLocation: Location<PayloadT>,
        newNode: Node<PayloadT>,
    ) {
        when (nodeLocation) {
            RootLocation -> {
                setRoot(newNode)

                newNode.setParent(null)
            }

            is RelativeLocation -> {
                val parent = nodeLocation.parentNode
                val childSide = nodeLocation.side

                linkDownChildSymmetrically(
                    parentNode = parent,
                    side = childSide,
                    childNode = newNode,
                )
            }
        }
    }

    private fun setRoot(
        newRoot: Node<PayloadT>?,
    ) {
        mutableRootNode = newRoot
    }
}
