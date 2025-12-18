package dev.kmpx.collections.internal.data_structures.ordered_binary_tree

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree.Node
import dev.kmpx.collections.internal.utils.assert
import dev.kmpx.collections.internal.utils.fail

internal fun <PayloadT> OrderedBinaryTree<PayloadT>.rebalanceAfterCutOff(
    cutOffLeafLocation: OrderedBinaryTree.RelativeLocation<PayloadT>,
    cutOffLeafColor: Node.Color,
) {
    when (cutOffLeafColor) {
        Node.Color.Black -> {
            this.fixBlackViolationRecursively(
                node = null,
                relativeLocation = cutOffLeafLocation,
            )
        }

        Node.Color.Red -> {}
    }
}

internal fun <PayloadT> OrderedBinaryTree<PayloadT>.rebalanceAfterCollapse(
    elevatedNode: Node<PayloadT>,
) {
    // As the elevated node was a single child of its parent, it must be a red node
    elevatedNode.paint(
        newColor = Node.Color.Black,
    )
}

internal fun <PayloadT> OrderedBinaryTree<PayloadT>.rebalanceAfterAttach(
    attachedNode: Node<PayloadT>,
) {
    this.fixPotentialRedViolationRecursively(
        node = attachedNode,
    )
}

private fun <PayloadT> OrderedBinaryTree<PayloadT>.fixBlackViolationRecursively(
    /**
     * A handle to the node to be fixed, null represents a null node
     */
    node: Node<PayloadT>?,
    /**
     * The relative location of the node to be fixed
     */
    relativeLocation: OrderedBinaryTree.RelativeLocation<PayloadT>,
) {
    val color = node?.color

    assert(color != Node.Color.Red) {
        "Black violation fixup phase cannot start with a red node"
    }

    // The parent of the fixed node doesn't change during a single fixup phase. Other parts of the close family
    // (sibling, nephews) may change.
    val parentNode = relativeLocation.parentNode

    // The side of the fixed node in relation to its parent. It also doesn't change during a single fixup phase.
    val side = relativeLocation.side

    // The primary cases considered below (#3 - #6) are quasi-final, i.e. are either final or lead to a final case.
    // All these cases require that the fixed node has a proper sibling and leave the tree in the state when it
    // still has a proper sibling.

    // Case #3: The sibling S is red, so P and the nephews C and D have to be black
    val wasCase3Applied = run {
        // If the node is proper, it has a proper sibling from Conclusion 2. If it's a null node (which is possible
        // on the first recursion level), its sibling also must be proper, as it must have a black height one,
        // which was the black height of the node we deleted.
        val siblingNode = relativeLocation.occupyingNodeSibling ?: fail("The node has no sibling")

        val siblingColor = siblingNode.color

        if (siblingColor != Node.Color.Red) return@run false

        this@fixBlackViolationRecursively.rotate(
            pivotNode = parentNode,
            direction = side.directionTo,
        )

        parentNode.paint(
            newColor = Node.Color.Red,
        )

        siblingNode.paint(
            newColor = Node.Color.Black,
        )

        // Now the parent is red and the sibling (old close nephew) is black. Depending on the color of the nephews,
        // it's a match for cases #4, #5 or #6
        true
    }

    // Case #4: P is red (the sibling S is black) and S’s children are black
    run {
        val parentColor = parentNode.color
        if (parentColor != Node.Color.Red) return@run

        val siblingNode = relativeLocation.occupyingNodeSibling ?: fail("The node has no sibling")

        val siblingColor = siblingNode.color

        assert(siblingColor == Node.Color.Black) {
            "The sibling must be black, as the parent is red"
        }

        val (closeNephewNode, distantNephewNode) = siblingNode.getChildren(
            side = side,
        )

        val closeNephewColor = closeNephewNode?.color

        val distantNephewColor = distantNephewNode?.color

        if (closeNephewColor == Node.Color.Red) return@run
        if (distantNephewColor == Node.Color.Red) return@run

        parentNode.paint(
            newColor = Node.Color.Black,
        )

        siblingNode.paint(
            newColor = Node.Color.Red,
        )

        // The violation was fixed!
        return
    }

    // Case #5 S’s close child C is red (the sibling S is black), and S’s distant child D is black
    val wasCase5Applied = run {
        val siblingNode = relativeLocation.occupyingNodeSibling ?: fail("The node has no sibling")

        val siblingColor = siblingNode.color

        val (closeNephewNode, distantNephewNode) = siblingNode.getChildren(
            side = side,
        )

        val closeNephewColor = closeNephewNode?.color

        val distantNephewColor = distantNephewNode?.color

        if (closeNephewColor != Node.Color.Red) return@run false
        if (distantNephewColor == Node.Color.Red) return@run false

        // From now on, we know that the close nephew is red and the distant nephew is effectively black

        assert(siblingColor == Node.Color.Black) {
            "The sibling must be black, as the close nephew is red"
        }

        this@fixBlackViolationRecursively.rotate(
            pivotNode = siblingNode,
            direction = side.directionFrom,
        )

        closeNephewNode.paint(
            newColor = Node.Color.Black,
        )

        siblingNode.paint(
            newColor = Node.Color.Red,
        )

        // Now the parent color is unchanged and the new sibling (old close nephew) is black. The distant nephew
        // (old sibling) is now red. This is a fit for case #6.
        true
    }

    // Case #6: S’s distant child D is red (the sibling S is black)
    run {
        val parentColor = parentNode.color

        val siblingNode = relativeLocation.occupyingNodeSibling ?: fail("The node has no sibling")

        val siblingColor = siblingNode.color

        val (_, distantNephewNode) = siblingNode.getChildren(
            side = side,
        )

        val distantNephewColor = distantNephewNode?.color

        if (distantNephewColor != Node.Color.Red) return@run

        // From now on, we know that the distant nephew is red

        assert(siblingColor == Node.Color.Black) {
            "The sibling must be black, as the distant nephew is red"
        }

        this@fixBlackViolationRecursively.rotate(
            pivotNode = parentNode,
            direction = side.directionTo,
        )

        parentNode.setColor(
            color = Node.Color.Black,
        )

        siblingNode.setColor(
            color = parentColor,
        )

        distantNephewNode.paint(
            newColor = Node.Color.Black,
        )

        // The violation was fixed!
        return
    }

    if (wasCase3Applied) {
        fail("Case #3 application should always lead to Case #4 or Case #6 application")
    }

    if (wasCase5Applied) {
        fail("Case #5 application should always lead to Case #6 application")
    }

    // Now we know that none of the primary cases applied

    val parentColor = parentNode.color

    assert(parentColor == Node.Color.Black) {
        // If the parent was red, it should've triggered case #4 (if both nephews were black) or cases #5/#6
        // (otherwise)
        "The parent is not black, which is unexpected at this point"
    }

    val siblingNode = relativeLocation.occupyingNodeSibling ?: fail("The node has no sibling")

    val siblingColor = siblingNode.color

    assert(siblingColor == Node.Color.Black) {
        // If the sibling was red, it should've triggered case #3 (and later one of the final cases)
        "The sibling is not black, which is unexpected at this point"
    }

    val (closeNephewNode, distantNephewNode) = siblingNode.getChildren(
        side = side,
    )

    val closeNephewColor = closeNephewNode?.color

    val distantNephewColor = distantNephewNode?.color

    assert(distantNephewColor != Node.Color.Red) {
        // If the distant nephew was red, it should've triggered case #6 (a final case)
        "The distant nephew is red, which is unexpected at this point"
    }

    assert(closeNephewColor != Node.Color.Red) {
        // We just checked that the distant nephew is black. If the close nephew was red, it should've triggered
        // case #5 (and later case #6)
        "The close nephew is red, which is unexpected at this point"
    }

    // Case #2: P, S, and S’s children are black
    siblingNode.paint(
        newColor = Node.Color.Red,
    )

    // After paining the sibling red, the subtree starting at this node is balanced

    when (val parentRelativeLocation = parentNode.locateRelatively()) {
        null -> {
            // Case #1: The parent is root
            // The violation was fixed!
            return
        }

        else -> {
            // Although the subtree is balanced (has the same black height on each path), it's still one less than
            // all the other paths in the whole tree. We need to fix it recursively.
            this.fixBlackViolationRecursively(
                node = parentNode,
                relativeLocation = parentRelativeLocation,
            )
        }
    }
}

/**
 * Fix a (potential) red violation in the subtree with the root corresponding to the [node].
 */
private fun <PayloadT> OrderedBinaryTree<PayloadT>.fixPotentialRedViolationRecursively(
    node: Node<PayloadT>,
) {
    val color = node.color

    assert(color == Node.Color.Red) {
        "Red violation fixup must start with a red node"
    }

    val relativeLocation = node.locateRelatively() ?: run {
        // Case #3
        // If this is the root, it can't be in a red violation with its parent, as it has no parent. We can fix the
        // red violation by simply changing the root's color to black.

        node.paint(
            newColor = Node.Color.Black,
        )

        return
    }

    val parentNode = relativeLocation.parentNode

    val side = relativeLocation.side

    if (parentNode.color == Node.Color.Black) {
        // Case #1
        // If the parent is black, there's no red violation between this node and its parent
        return
    }

    // From now on, we know that the parent is red

    val parentRelativeLocation = parentNode.locateRelatively() ?: run {
        // Case #4
        // The parent is the root, so it can't get into a red violation with its parent (as it has no parent). We
        // can fix the red violation by simply changing the root's color to black.

        parentNode.paint(
            newColor = Node.Color.Black,
        )

        // Although we touched the parent's color, we didn't really move it
        return
    }

    val grandparentNode = parentRelativeLocation.parentNode

    assert(grandparentNode.color == Node.Color.Black) {
        "The grandparent must be black, as the parent is red"
    }

    val uncleNode = parentRelativeLocation.occupyingNodeSibling
    val uncleSide = parentRelativeLocation.siblingSide
    val uncleColor = uncleNode?.color

    when (uncleColor) {
        Node.Color.Red -> {
            // Case #2

            // As the uncle is also red (like this node and its parent), we can swap the color of the grandparent
            // (black) with the color of its children (red). This fixed the red violation between this node and its
            // parent.

            parentNode.paint(
                newColor = Node.Color.Black,
            )

            uncleNode.paint(
                newColor = Node.Color.Black,
            )

            grandparentNode.paint(
                newColor = Node.Color.Red,
            )

            // The subtree starting at the fixed node is now balanced

            // While we fixed one red violation, we might've introduced another. Let's fix this recursively.
            this.fixPotentialRedViolationRecursively(
                node = grandparentNode,
            )
        }

        else -> {
            // N and P are red, he uncle is black

            if (side == uncleSide) {
                // Case #5: N is the closer grandchild of G.
                // We can reduce this to a fit for case #6 by a single rotation
                rotate(
                    pivotNode = parentNode,
                    direction = uncleSide.directionFrom,
                )

                // This operation pushes the fixed node one level down, but this doesn't affect the remaining logic
            }

            // Case #6: N is the distant grandchild of G
            val newSubtreeRootNode = rotate(
                pivotNode = grandparentNode,
                direction = uncleSide.directionTo,
            )

            assert(newSubtreeRootNode == node || newSubtreeRootNode == parentNode) {
                "The new subtree root must be either this node or its parent"
            }

            newSubtreeRootNode.paint(
                newColor = Node.Color.Black,
            )

            grandparentNode.paint(
                newColor = Node.Color.Red,
            )
        }
    }
}

private fun <PayloadT> Node<PayloadT>.paint(
    newColor: Node.Color,
) {
    val color = color

    if (color == newColor) {
        throw IllegalStateException("The node is already painted $newColor")
    }

    setColor(
        color = newColor,
    )
}
