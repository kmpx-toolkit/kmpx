package dev.kmpx.collections.internal.data_structures.ordered_binary_tree

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree.Node
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.NodeData
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.RedBlackTreeTestUtils
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.dump
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.getNodeTraversing
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.insertVerified
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.removeVerified
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@Suppress("ClassName")
class OrderedBinaryTree_redBlackBalancing_tests {
    @Test
    fun testInitial() {
        val tree = OrderedBinaryTree.create<Int>()

        assertNull(
            actual = tree.dump(),
        )
    }

    @Test
    fun testInsert_root() {
        val tree = OrderedBinaryTree.create<Int>()

        val node100 = tree.insertVerified(
            location = OrderedBinaryTree.RootLocation.cast(),
            payload = 100,
        )

        assertEquals(
            expected = NodeData(
                payload = 100,
                color = Node.Color.Black,
            ),
            actual = tree.dump(),
        )

        assertNull(
            actual = node100.parent,
        )

        assertEquals(
            expected = OrderedBinaryTree.RootLocation.cast(),
            actual = node100.locate(),
        )
    }

    /**
     * Insertion
     * Case #1: Parent is black (and is root)
     */
    @Test
    fun testInsert_blackRootParent() {
        val tree = RedBlackTreeTestUtils.loadVerified(
            rootData = NodeData(
                payload = 10,
                color = Node.Color.Black,
            ),
        )

        val parentNode = tree.rootNode!!

        tree.insertVerified(
            location = parentNode.getRightChildLocation(),
            payload = 1000,
        )
    }

    /**
     * Insertion
     * Case #1: Parent is black
     *
     * No fixup required
     */
    @Test
    fun testInsert_ordinaryBlackParent() {
        val tree = RedBlackTreeTestUtils.loadVerified(
            rootData = NodeData(
                payload = 100,
                color = Node.Color.Red,
                leftChild = NodeData(
                    payload = 50,
                    color = Node.Color.Black,
                ),
                rightChild = NodeData(
                    payload = 150,
                    color = Node.Color.Black,
                ),
            ),
        )

        val parentNode = tree.getNodeTraversing(50)

        tree.insertVerified(
            location = parentNode.getRightChildLocation(),
            payload = 1000,
        )
    }

    /**
     * Insertion
     * Case #2: Parent and uncle are red, grandparent's parent is black
     *
     * Leads to Case #1
     */
    @Test
    fun testInsert_redParentRedUncle_blackGreatGrandparent() {
        // Black height: 3
        val tree = RedBlackTreeTestUtils.loadVerified(
            // Grandparent's grandparent
            rootData = NodeData(
                payload = 100.0,
                color = Node.Color.Red,
                leftChild = RedBlackTreeTestUtils.buildBalance(
                    requiredBlackDepth = 3,
                    payloadRange = 0.0..100.0,
                ),
                // Grandparent's parent
                rightChild = NodeData(
                    payload = 500.0,
                    color = Node.Color.Black,
                    // Grandparent
                    leftChild = NodeData(
                        payload = 250.0,
                        color = Node.Color.Black,
                        // Parent
                        leftChild = NodeData(
                            payload = 150.0,
                            color = Node.Color.Red,
                        ),
                        // Uncle
                        rightChild = NodeData(
                            payload = 400.0,
                            color = Node.Color.Red,
                        ),
                    ),
                    rightChild = RedBlackTreeTestUtils.buildBalance(
                        requiredBlackDepth = 2,
                        payloadRange = 500.0..1000.0,
                    ),
                ),
            )
        )

        val parentNode = tree.getNodeTraversing(payload = 150.0)

        tree.insertVerified(
            location = parentNode.getLeftChildLocation(),
            payload = 125.0,
        )
    }

    /**
     * Insertion
     * Case #2: Parent and uncle are red, grandparent's parent and grandparent's
     * uncle are also red
     *
     * Leads to another Case #2
     *
     * First Case #2 from the left side, second Case #2 from the right side.
     *
     */
    @Test
    fun testInsert_redParentRedUncle_redGreatGrandparentRedGreatUncle() {
        // Black height: 3
        val tree = RedBlackTreeTestUtils.loadVerified(
            // Grandparent's grandparent
            rootData = NodeData(
                payload = 1000.0,
                color = Node.Color.Black,
                // Grandparent's parent
                leftChild = NodeData(
                    payload = 500.0,
                    color = Node.Color.Red,
                    leftChild = RedBlackTreeTestUtils.buildBalance(
                        requiredBlackDepth = 2,
                        payloadRange = 0.0..500.0,
                    ),
                    // Grandparent
                    rightChild = NodeData(
                        payload = 750.0,
                        color = Node.Color.Black,
                        // Parent
                        leftChild = NodeData(
                            payload = 600.0,
                            color = Node.Color.Red,
                        ),
                        // Uncle
                        rightChild = NodeData(
                            payload = 850.0,
                            color = Node.Color.Red,
                        ),
                    ),
                ),
                // Grandparent's uncle
                rightChild = NodeData(
                    payload = 1500.0,
                    color = Node.Color.Red,
                    leftChild = RedBlackTreeTestUtils.buildBalance(
                        requiredBlackDepth = 2,
                        payloadRange = 1000.0..1500.0,
                    ),
                    rightChild = RedBlackTreeTestUtils.buildBalance(
                        requiredBlackDepth = 2,
                        payloadRange = 1500.0..2000.0,
                    ),
                ),
            ),
        )

        val parentNode = tree.getNodeTraversing(payload = 600.0)

        tree.insertVerified(
            location = parentNode.getRightChildLocation(),
            payload = 650.0,
        )
    }

    /**
     * Insertion
     * Case #2: Parent and uncle are red, the grandparent is a root
     *
     * Leads to Case #3, a red root
     *
     * Case #2 from the left side
     */
    @Test
    fun testInsert_redParentRedUncle_rootGrandparent() {
        // Black height: 2
        val tree = RedBlackTreeTestUtils.loadVerified(
            // Grandparent
            rootData = NodeData(
                payload = 750.0,
                color = Node.Color.Black,
                // Uncle
                leftChild = NodeData(
                    payload = 600.0,
                    color = Node.Color.Red,
                ),
                // Parent
                rightChild = NodeData(
                    payload = 850.0,
                    color = Node.Color.Red,
                ),
            ),
        )

        val parentNode = tree.getNodeTraversing(payload = 850.0)

        tree.insertVerified(
            location = parentNode.getLeftChildLocation(),
            payload = 800.0,
        )
    }

    /**
     * Insertion
     * Case #2: Parent and uncle are red, the grandparent's parent is root and is red
     *
     * Leads to Case #4
     *
     * Case #2 from the right side,
     */
    @Test
    fun testInsert_redParentRedUncle_redRootGreatGrandparent() {
        // Black height: 2
        val tree = RedBlackTreeTestUtils.loadVerified(
            // Grandparent's parent
            rootData = NodeData(
                payload = 500.0,
                color = Node.Color.Red,
                leftChild = RedBlackTreeTestUtils.buildBalance(
                    requiredBlackDepth = 2,
                    payloadRange = 0.0..500.0,
                ),
                // Grandparent
                rightChild = NodeData(
                    payload = 1000.0,
                    color = Node.Color.Black,
                    // Uncle
                    leftChild = NodeData(
                        payload = 750.0,
                        color = Node.Color.Red,
                    ),
                    // Parent
                    rightChild = NodeData(
                        payload = 1500.0,
                        color = Node.Color.Red,
                    ),
                ),
            ),
        )

        val parentNode = tree.getNodeTraversing(payload = 1500.0)

        tree.insertVerified(
            location = parentNode.getRightChildLocation(),
            payload = 2000.0,
        )
    }

    /**
     * Insertion
     * Case #2: Parent and uncle are red, the grandparent's parent is red and the
     * grandparent's uncle is black, the grandparent is the inner grandchild of
     * its own grandparent.
     *
     * Leads to Case #5
     */
    @Test
    fun testInsert_redParentRedUncle_redGreatGrandparentBlackGreatUncle_inner() {
        // Black height: 3
        val tree = RedBlackTreeTestUtils.loadVerified(
            // Grandparent's grandparent
            rootData = NodeData(
                payload = 1000.0,
                color = Node.Color.Black,
                // Grandparent's uncle
                leftChild = NodeData(
                    payload = 500.0,
                    color = Node.Color.Black,
                ),
                // Grandparent's parent
                rightChild = NodeData(
                    payload = 2000.0,
                    color = Node.Color.Red,
                    // Grandparent
                    leftChild = NodeData(
                        payload = 1500.0,
                        color = Node.Color.Black,
                        // Parent
                        leftChild = NodeData(
                            payload = 1250.0,
                            color = Node.Color.Red,
                        ),
                        // Uncle
                        rightChild = NodeData(
                            payload = 1750.0,
                            color = Node.Color.Red,
                        ),
                    ),
                    rightChild = RedBlackTreeTestUtils.buildBalance(
                        requiredBlackDepth = 2,
                        payloadRange = 2000.0..3000.0,
                    ),
                ),
            )
        )

        val parentNode = tree.getNodeTraversing(payload = 1250.0)

        tree.insertVerified(
            location = parentNode.getRightChildLocation(),
            payload = 1300.0,
        )
    }

    /**
     * Insertion
     * Case #2: Parent and uncle are red, the grandparent's parent is red and the
     * grandparent's uncle is black, the grandparent is the outer grandchild of its own grandparent
     *
     * Leads to Case #6
     */
    @Test
    fun testInsert_redParentRedUncle_redGreatGrandparentBlackGreatUncle_outer() {
        // Black height: 3
        val tree = RedBlackTreeTestUtils.loadVerified(
            // Grandparent's grandparent
            rootData = NodeData(
                payload = 1000.0,
                color = Node.Color.Black,
                // Grandparent's uncle
                leftChild = NodeData(
                    payload = 500.0,
                    color = Node.Color.Black,
                ),
                // Grandparent's parent
                rightChild = NodeData(
                    payload = 2000.0,
                    color = Node.Color.Red,
                    leftChild = RedBlackTreeTestUtils.buildBalance(
                        requiredBlackDepth = 2,
                        payloadRange = 1000.0..2000.0,
                    ),
                    // Grandparent
                    rightChild = NodeData(
                        payload = 3000.0,
                        color = Node.Color.Black,
                        // Parent
                        leftChild = NodeData(
                            payload = 2500.0,
                            color = Node.Color.Red,
                        ),
                        // Uncle
                        rightChild = NodeData(
                            payload = 4000.0,
                            color = Node.Color.Red,
                        ),
                    ),
                ),
            )
        )

        val parentNode = tree.getNodeTraversing(payload = 2500.0)

        tree.insertVerified(
            location = parentNode.getLeftChildLocation(),
            payload = 2250.0,
        )
    }

    /**
     * Insertion
     * Case #4: Parent is a root and is red
     *
     * Immediate fixup
     */
    @Test
    fun testInsert_redRootParent() {
        // Black height: 1
        val tree = RedBlackTreeTestUtils.loadVerified(
            rootData = NodeData(
                payload = 1000.0,
                color = Node.Color.Red,
            ),
        )

        val parentNode = tree.rootNode!!

        tree.insertVerified(
            location = parentNode.getLeftChildLocation(),
            payload = 500.0,
        )
    }

    /**
     * Insertion
     * Case #5: Parent is red and uncle is black, node is the inner grandchild of
     * its grandparent
     *
     * Goes through Case #6 internally
     */
    @Test
    fun testInsert_redParentBlackUncle_inner() {
        // Black height: 2
        val tree = RedBlackTreeTestUtils.loadVerified(
            // Grandparent's parent
            rootData = NodeData(
                payload = 1000.0,
                color = Node.Color.Red,
                // Grandparent
                leftChild = NodeData(
                    payload = 500.0,
                    color = Node.Color.Black,
                    // Uncle: nil
                    // Parent
                    rightChild = NodeData(
                        payload = 750.0,
                        color = Node.Color.Red,
                    ),
                ),
                rightChild = RedBlackTreeTestUtils.buildBalance(
                    requiredBlackDepth = 2,
                    payloadRange = 1000.0..2000.0,
                ),
            ),
        )


        val parentNode = tree.getNodeTraversing(payload = 750.0)

        tree.insertVerified(
            location = parentNode.getLeftChildLocation(),
            payload = 700.0,
        )
    }

    /**
     * Insertion
     * Case #6: Parent is red and uncle is nil (effectively black), node is the outer grandchild of
     * its grandparent
     *
     * Immediate fixup
     */
    @Test
    fun testInsert_redParentBlackUncle_outer() {
        // Black height: 2
        val tree = RedBlackTreeTestUtils.loadVerified(
            // Grandparent's parent
            rootData = NodeData(
                payload = 1000.0,
                color = Node.Color.Red,
                leftChild = RedBlackTreeTestUtils.buildBalance(
                    requiredBlackDepth = 2,
                    payloadRange = 0.0..1000.0,
                ),
                // Grandparent
                rightChild = NodeData(
                    payload = 2000.0,
                    color = Node.Color.Black,
                    // Parent
                    leftChild = NodeData(
                        payload = 1500.0,
                        color = Node.Color.Red,
                    ),
                    // Uncle: nil
                ),
            ),
        )

        val parentNode = tree.getNodeTraversing(payload = 1500.0)

        tree.insertVerified(
            location = parentNode.getLeftChildLocation(),
            payload = 1250.0,
        )
    }

    /**
     * Simple removal (two children, successor: leaf)
     */
    @Test
    fun testRemove_twoChildren_leaf() {
        // Black height: 4
        val tree = RedBlackTreeTestUtils.loadVerified(
            rootData = NodeData(
                payload = 1000.0,
                color = Node.Color.Black,
                leftChild = RedBlackTreeTestUtils.buildBalance(
                    requiredBlackDepth = 4,
                    payloadRange = 0.0..1000.0,
                ),
                // Removed node
                rightChild = NodeData(
                    payload = 2000.0,
                    color = Node.Color.Red,
                    leftChild = NodeData(
                        payload = 1500.0,
                        color = Node.Color.Black,
                        leftChild = RedBlackTreeTestUtils.buildBalance(
                            requiredBlackDepth = 3,
                            payloadRange = 1000.0..1500.0,
                        ),
                        rightChild = NodeData(
                            payload = 1750.0,
                            color = Node.Color.Black,
                            leftChild = RedBlackTreeTestUtils.buildBalance(
                                requiredBlackDepth = 2,
                                payloadRange = 1500.0..1750.0,
                            ),
                            // Predecessor
                            rightChild = NodeData(
                                payload = 1800.0,
                                color = Node.Color.Black,
                                leftChild = NodeData(
                                    payload = 1775.0,
                                    color = Node.Color.Red,
                                ),
                            ),
                        ),
                    ),
                    rightChild = NodeData(
                        payload = 3000.0,
                        color = Node.Color.Black,
                        leftChild = NodeData(
                            payload = 2800.0,
                            color = Node.Color.Black,
                            // Successor
                            leftChild = NodeData(
                                payload = 2500.0,
                                color = Node.Color.Black,
                            ),
                            rightChild = RedBlackTreeTestUtils.buildBalance(
                                requiredBlackDepth = 2,
                                payloadRange = 2800.0..3000.0,
                            ),
                        ),
                        rightChild = RedBlackTreeTestUtils.buildBalance(
                            requiredBlackDepth = 3,
                            payloadRange = 3000.0..4000.0,
                        ),
                    ),
                ),
            ),
        )

        val node = tree.getNodeTraversing(payload = 2000.0)
        val expectedPredecessorNode = tree.getNodeTraversing(payload = 1800.0)

        val predecessorNode = tree.removeVerified(
            node = node,
        )

        assertEquals(
            expected = expectedPredecessorNode,
            actual = predecessorNode,
        )
    }

    /**
     * Simple removal (two children, successor: one child)
     */
    @Test
    fun testRemove_twoChildren_oneChild() {
        // Black height: 4
        val tree = RedBlackTreeTestUtils.loadVerified(
            rootData = NodeData(
                payload = 1000.0,
                color = Node.Color.Black,
                leftChild = RedBlackTreeTestUtils.buildBalance(
                    requiredBlackDepth = 3,
                    payloadRange = 0.0..1000.0,
                ),
                // Removed node
                rightChild = NodeData(
                    payload = 2000.0,
                    color = Node.Color.Red,
                    leftChild = NodeData(
                        payload = 1500.0,
                        color = Node.Color.Black,
                        leftChild = RedBlackTreeTestUtils.buildBalance(
                            requiredBlackDepth = 2,
                            payloadRange = 1000.0..1500.0,
                        ),
                        // Predecessor
                        rightChild = NodeData(
                            payload = 1750.0,
                            color = Node.Color.Black,
                            leftChild = NodeData(
                                payload = 1650.0,
                                color = Node.Color.Red,
                            ),
                        ),
                    ),
                    rightChild = NodeData(
                        payload = 3000.0,
                        color = Node.Color.Black,
                        // Successor
                        leftChild = NodeData(
                            payload = 2800.0,
                            color = Node.Color.Black,
                            rightChild = NodeData(
                                payload = 2900.0,
                                color = Node.Color.Red,
                            ),
                        ),
                        rightChild = RedBlackTreeTestUtils.buildBalance(
                            requiredBlackDepth = 2,
                            payloadRange = 3000.0..4000.0,
                        ),
                    ),
                ),
            ),
        )

        val node = tree.getNodeTraversing(payload = 2000.0)
        val expectedPredecessorNode = tree.getNodeTraversing(payload = 1750.0)

        val predecessorNode = tree.removeVerified(
            node = node,
        )

        assertEquals(
            expected = expectedPredecessorNode,
            actual = predecessorNode,
        )
    }

    /**
     * Simple removal (one child)
     */
    @Test
    fun testRemove_oneChild() {
        // Black height: 3
        val tree = RedBlackTreeTestUtils.loadVerified(
            rootData = NodeData(
                payload = 1000.0,
                color = Node.Color.Red,
                leftChild = RedBlackTreeTestUtils.buildBalance(
                    requiredBlackDepth = 3,
                    payloadRange = 0.0..1000.0,
                ),
                rightChild = NodeData(
                    payload = 2000.0,
                    color = Node.Color.Black,
                    // Removed node
                    leftChild = NodeData(
                        payload = 1500.0,
                        color = Node.Color.Black,
                        // Predecessor
                        leftChild = NodeData(
                            payload = 1250.0,
                            color = Node.Color.Red,
                        ),
                    ),
                    rightChild = RedBlackTreeTestUtils.buildBalance(
                        requiredBlackDepth = 2,
                        payloadRange = 2000.0..4000.0,
                    ),
                ),
            ),
        )

        val node = tree.getNodeTraversing(payload = 1500.0)
        val expectedPredecessorNode = tree.getNodeTraversing(payload = 1250.0)

        val predecessorNode = tree.removeVerified(
            node = node,
        )

        assertEquals(
            expected = expectedPredecessorNode,
            actual = predecessorNode,
        )
    }

    /**
     * Simple removal (leaf, red root)
     */
    @Test
    fun testRemove_leaf_redRoot() {
        val tree = RedBlackTreeTestUtils.loadVerified(
            rootData = NodeData(
                payload = 1000.0,
                color = Node.Color.Red,
            ),
        )

        val node = tree.rootNode!!

        val predecessorNode = tree.removeVerified(
            node = node,
        )

        assertNull(
            actual = predecessorNode,
        )
    }

    /**
     * Simple removal (leaf, red root)
     */
    @Test
    fun testRemove_leaf_blackRoot() {
        val tree = RedBlackTreeTestUtils.loadVerified(
            rootData = NodeData(
                payload = 2000.0,
                color = Node.Color.Black,
            ),
        )

        val node = tree.rootNode!!

        val predecessorNode = tree.removeVerified(
            node = node,
        )

        assertNull(
            actual = predecessorNode,
        )
    }

    /**
     * Simple removal (leaf, red)
     */
    @Test
    fun testRemove_leaf_red() {
        // Black height: 3
        val tree = RedBlackTreeTestUtils.loadVerified(
            // Predecessor
            rootData = NodeData(
                payload = 1000.0,
                color = Node.Color.Red,
                leftChild = RedBlackTreeTestUtils.buildBalance(
                    requiredBlackDepth = 3,
                    payloadRange = 0.0..1000.0,
                ),
                rightChild = NodeData(
                    payload = 2000.0,
                    color = Node.Color.Black,
                    leftChild = NodeData(
                        payload = 1500.0,
                        color = Node.Color.Black,
                        // Removed node
                        leftChild = NodeData(
                            payload = 1200.0,
                            color = Node.Color.Red,
                        ),
                    ),
                    rightChild = RedBlackTreeTestUtils.buildBalance(
                        requiredBlackDepth = 2,
                        payloadRange = 2000.0..4000.0,
                    ),
                ),
            ),
        )

        val node = tree.getNodeTraversing(payload = 1200.0)
        val expectedPredecessorNode = tree.rootNode!!

        val predecessorNode = tree.removeVerified(
            node = node,
        )

        assertEquals(
            expected = expectedPredecessorNode,
            actual = predecessorNode,
        )
    }

    /**
     * Complex removal (black leaf)
     * Case #1: parent is root
     *
     * No fixup required
     */
    @Test
    fun testRemove_blackLeaf_rootParent() {
        // Black height: 3
        val tree = RedBlackTreeTestUtils.loadVerified(
            // Predecessor
            rootData = NodeData(
                payload = 1000.0,
                color = Node.Color.Red,
                leftChild = RedBlackTreeTestUtils.buildBalance(
                    requiredBlackDepth = 2,
                    payloadRange = 0.0..1000.0,
                ),
                // Removed node
                rightChild = NodeData(
                    payload = 2000.0,
                    color = Node.Color.Black,
                ),
            ),
        )

        val node = tree.getNodeTraversing(payload = 2000.0)
        val expectedPredecessorNode = tree.rootNode!!

        val predecessorNode = tree.removeVerified(
            node = node,
        )

        assertEquals(
            expected = expectedPredecessorNode,
            actual = predecessorNode,
        )
    }

    /**
     * Complex removal (black leaf)
     * Case #2: parent and sibling are black, nephews are nil (effectively black)
     *
     * Leads to Case #1
     */
    @Test
    fun testRemove_blackLeaf_blackCloseFamily_to_rootParent() {
        // Black height: ?
        val tree = RedBlackTreeTestUtils.loadVerified(
            // Parent
            rootData = NodeData(
                payload = 2000.0,
                color = Node.Color.Black,
                // Removed node
                leftChild = NodeData(
                    payload = 1500.0,
                    color = Node.Color.Black,
                ),
                // Sibling
                rightChild = NodeData(
                    payload = 3000.0,
                    color = Node.Color.Black,
                ),
            ),
        )

        val node = tree.getNodeTraversing(payload = 1500.0)

        val predecessorNode = tree.removeVerified(
            node = node,
        )

        assertNull(
            actual = predecessorNode,
        )
    }

    /**
     * Complex removal (black leaf)
     * Case #2: parent and sibling are black, nephews are nil (effectively black)
     *
     * Leads to another Case #2, parent and sibling are black, nephews are now
     * proper black nodes
     */
    @Test
    fun testRemove_blackLeaf_blackCloseFamily_to_blackCloseFamily() {
        // Black height: 4
        val tree = RedBlackTreeTestUtils.loadVerified(
            // Parent's parent (grandparent) / predecessor
            rootData = NodeData(
                payload = 1000.0,
                color = Node.Color.Black,
                // Parent's sibling (uncle)
                leftChild = NodeData(
                    payload = 500.0,
                    color = Node.Color.Black,
                    // Parent's distant nephew
                    leftChild = NodeData(
                        payload = 250.0,
                        color = Node.Color.Black,
                    ),
                    // Parent's close nephew
                    rightChild = NodeData(
                        payload = 750.0,
                        color = Node.Color.Black,
                    ),
                ),
                // Parent
                rightChild = NodeData(
                    payload = 1500.0,
                    color = Node.Color.Black,
                    // Removed node
                    leftChild = NodeData(
                        payload = 1250.0,
                        color = Node.Color.Black,
                    ),
                    // Sibling
                    rightChild = NodeData(
                        payload = 2000.0,
                        color = Node.Color.Black,
                    ),
                ),
            ),
        )

        val node = tree.getNodeTraversing(payload = 1250.0)
        val expectedPredecessorNode = tree.getNodeTraversing(payload = 1000.0)

        val predecessorNode = tree.removeVerified(
            node = node,
        )

        assertEquals(
            expected = expectedPredecessorNode,
            actual = predecessorNode,
        )
    }

    /**
     * Complex removal (black leaf)
     * Case #2: parent and sibling are black, nephews are nil (effectively black)
     *
     * Leads to Case #3, then Cases #4/#5/#6, then done
     */
    @Test
    fun testRemove_blackLeaf_blackCloseFamily_to_redSibling() {
        // Black height: 4
        val tree = RedBlackTreeTestUtils.loadVerified(
            // Parent's grandparent
            rootData = NodeData(
                payload = 2000.0,
                color = Node.Color.Red,
                // Parent's parent (grandparent)
                leftChild = NodeData(
                    payload = 1000.0,
                    color = Node.Color.Black,
                    // Parent / predecessor
                    leftChild = NodeData(
                        payload = 500.0,
                        color = Node.Color.Black,
                        // Sibling
                        leftChild = NodeData(
                            payload = 250.0,
                            color = Node.Color.Black,
                        ),
                        // Removed node
                        rightChild = NodeData(
                            payload = 750.0,
                            color = Node.Color.Black,
                        ),
                    ),
                    // Parent's sibling (uncle)
                    rightChild = NodeData(
                        payload = 1500.0,
                        color = Node.Color.Red,
                        // Parent's close nephew
                        leftChild = RedBlackTreeTestUtils.buildBalance(
                            requiredBlackDepth = 3,
                            payloadRange = 1000.0..1500.0,
                        ),
                        // Parent's distant nephew
                        rightChild = RedBlackTreeTestUtils.buildBalance(
                            requiredBlackDepth = 3,
                            payloadRange = 1500.0..2000.0,
                        ),
                    ),
                ),
                rightChild = RedBlackTreeTestUtils.buildBalance(
                    requiredBlackDepth = 4,
                    payloadRange = 2000.0..4000.0,
                ),
            ),
        )

        val node = tree.getNodeTraversing(payload = 750.0)
        val expectedPredecessorNode = tree.getNodeTraversing(payload = 500.0)

        val predecessorNode = tree.removeVerified(
            node = node,
        )

        assertEquals(
            expected = expectedPredecessorNode,
            actual = predecessorNode,
        )
    }

    /**
     * Complex removal (black leaf)
     * Case #2: parent and sibling are black, nephews are nil (effectively black)
     *
     * Leads to Case #4, then done
     */
    @Test
    fun testRemove_blackLeaf_blackCloseFamily_to_blackParentBlackNephews() {
        // Black height: 3
        val tree = RedBlackTreeTestUtils.loadVerified(
            // Parent's parent (grandparent) / predecessor
            rootData = NodeData(
                payload = 2000.0,
                color = Node.Color.Red,
                // Parent's sibling (uncle)
                leftChild = NodeData(
                    payload = 1000.0,
                    color = Node.Color.Black,
                    // Parent's distant nephew
                    leftChild = NodeData(
                        payload = 500.0,
                        color = Node.Color.Black,
                    ),
                    // Parent's close nephew
                    rightChild = NodeData(
                        payload = 1500.0,
                        color = Node.Color.Black,
                    ),
                ),
                // Parent
                rightChild = NodeData(
                    payload = 3000.0,
                    color = Node.Color.Black,
                    // Removed node
                    leftChild = NodeData(
                        payload = 2500.0,
                        color = Node.Color.Black,
                    ),
                    // Sibling
                    rightChild = NodeData(
                        payload = 3500.0,
                        color = Node.Color.Black,
                    ),
                ),
            ),
        )

        val node = tree.getNodeTraversing(payload = 2500.0)
        val expectedPredecessorNode = tree.rootNode!!

        val predecessorNode = tree.removeVerified(
            node = node,
        )

        assertEquals(
            expected = expectedPredecessorNode,
            actual = predecessorNode,
        )
    }

    /**
     * Complex removal (black leaf)
     * Case #2: parent and sibling are black, nephews are nil (effectively black)
     *
     * Leads to Case #5, then (internally) Case 6, then done
     */
    @Test
    fun testRemove_blackLeaf_blackCloseFamily_to_redCloseNephewBlackDistantNephew() {
        // Black height: 3
        val tree = RedBlackTreeTestUtils.loadVerified(
            // Parent's parent (grandparent)
            rootData = NodeData(
                payload = 2000.0,
                color = Node.Color.Red,
                // Parent's sibling (uncle)
                leftChild = NodeData(
                    payload = 1000.0,
                    color = Node.Color.Black,
                    // Parent's distant nephew
                    leftChild = NodeData(
                        payload = 500.0,
                        color = Node.Color.Black,
                    ),
                    // Parent's close nephew
                    rightChild = NodeData(
                        payload = 1500.0,
                        color = Node.Color.Red,
                        leftChild = RedBlackTreeTestUtils.buildBalance(
                            requiredBlackDepth = 2,
                            payloadRange = 1000.0..1500.0,
                        ),
                        rightChild = RedBlackTreeTestUtils.buildBalance(
                            requiredBlackDepth = 2,
                            payloadRange = 1500.0..2000.0,
                        ),
                    ),
                ),
                // Parent / predecessor
                rightChild = NodeData(
                    payload = 3000.0,
                    color = Node.Color.Black,
                    // Sibling
                    leftChild = NodeData(
                        payload = 2500.0,
                        color = Node.Color.Black,
                    ),
                    // Removed node
                    rightChild = NodeData(
                        payload = 3500.0,
                        color = Node.Color.Black,
                    ),
                ),
            ),
        )

        val node = tree.getNodeTraversing(payload = 3500.0)
        val expectedPredecessorNode = tree.getNodeTraversing(payload = 3000.0)

        val predecessorNode = tree.removeVerified(
            node = node,
        )

        assertEquals(
            expected = expectedPredecessorNode,
            actual = predecessorNode,
        )
    }

    /**
     * Complex removal (black leaf)
     * Case #2: parent and sibling are black, nephews are nil (effectively black)
     *
     * Leads to Case #6, then done
     */
    @Test
    fun testRemove_blackLeaf_blackCloseFamily_to_redDistantNephew() {
        // Black height: 4
        val tree = RedBlackTreeTestUtils.loadVerified(
            // Parent's parent (grandparent)
            rootData = NodeData(
                payload = 2000.0,
                color = Node.Color.Black,
                // Parent
                leftChild = NodeData(
                    payload = 1500.0,
                    color = Node.Color.Black,
                    // Removed node
                    leftChild = NodeData(
                        payload = 1000.0,
                        color = Node.Color.Black,
                    ),
                    // Sibling
                    rightChild = NodeData(
                        payload = 1750.0,
                        color = Node.Color.Black,
                    ),
                ),
                // Parent's sibling (uncle)
                rightChild = NodeData(
                    payload = 3000.0,
                    color = Node.Color.Black,
                    // Parent's close nephew
                    leftChild = NodeData(
                        payload = 2500.0,
                        color = Node.Color.Red,
                        leftChild = RedBlackTreeTestUtils.buildBalance(
                            requiredBlackDepth = 2,
                            payloadRange = 2000.0..2500.0,
                        ),
                        rightChild = RedBlackTreeTestUtils.buildBalance(
                            requiredBlackDepth = 2,
                            payloadRange = 2500.0..3000.0,
                        ),
                    ),
                    // Parent's distant nephew
                    rightChild = NodeData(
                        payload = 3500.0,
                        color = Node.Color.Red,
                        leftChild = RedBlackTreeTestUtils.buildBalance(
                            requiredBlackDepth = 2,
                            payloadRange = 3000.0..3500.0,
                        ),
                        rightChild = RedBlackTreeTestUtils.buildBalance(
                            requiredBlackDepth = 2,
                            payloadRange = 3500.0..5000.0,
                        ),
                    ),
                ),
            ),
        )

        val node = tree.getNodeTraversing(payload = 1000.0)

        val predecessorNode = tree.removeVerified(
            node = node,
        )

        assertNull(
            actual = predecessorNode,
        )
    }

    /**
     * Complex removal (black leaf)
     * Case #3: The sibling is red (parent and nephews are black)
     *
     * Leads to Case #4, then done
     */
    @Test
    fun testRemove_blackLeaf_redSibling_to_blackParentBlackNephews() {
        // Black height: 3
        val tree = RedBlackTreeTestUtils.loadVerified(
            // Grandparent
            rootData = NodeData(
                payload = 2000.0,
                color = Node.Color.Red,
                // Parent / predecessor
                leftChild = NodeData(
                    payload = 1000.0,
                    color = Node.Color.Black,
                    // Sibling
                    leftChild = NodeData(
                        payload = 500.0,
                        color = Node.Color.Red,
                        // Distant nephew
                        leftChild = NodeData(
                            payload = 250.0,
                            color = Node.Color.Black,
                        ),
                        // Close nephew
                        rightChild = NodeData(
                            payload = 750.0,
                            color = Node.Color.Black,
                        ),
                    ),
                    // Removed node
                    rightChild = NodeData(
                        payload = 1500.0,
                        color = Node.Color.Black,
                    ),
                ),
                // Parent's sibling (uncle)
                rightChild = NodeData(
                    payload = 3000.0,
                    color = Node.Color.Black,
                    // Parent's close nephew
                    leftChild = NodeData(
                        payload = 2500.0,
                        color = Node.Color.Black,
                    ),
                    // Parent's distant nephew
                    rightChild = NodeData(
                        payload = 4000.0,
                        color = Node.Color.Black,
                    ),
                ),
            ),
        )

        val node = tree.getNodeTraversing(payload = 1500.0)
        val expectedPredecessorNode = tree.getNodeTraversing(payload = 1000.0)

        val predecessorNode = tree.removeVerified(
            node = node,
        )

        assertEquals(
            expected = expectedPredecessorNode,
            actual = predecessorNode,
        )
    }

    /**
     * Complex removal (black leaf)
     * Case #3: The sibling is red (parent and nephews are black)
     *
     * Leads to Case #5, then Case #6, then done
     */
    @Test
    fun testRemove_blackLeaf_redSibling_to_redCloseNephewBlackDistantNephew() {
        // Black height: 4
        val tree = RedBlackTreeTestUtils.loadVerified(
            // Grandparent / predecessor
            rootData = NodeData(
                payload = 2000.0,
                color = Node.Color.Black,
                // Parent's sibling (uncle)
                leftChild = NodeData(
                    payload = 1000.0,
                    color = Node.Color.Black,
                    // Parent's distant nephew
                    leftChild = NodeData(
                        payload = 500.0,
                        color = Node.Color.Black,
                    ),
                    // Parent's close nephew
                    rightChild = NodeData(
                        payload = 1500.0,
                        color = Node.Color.Red,
                        leftChild = RedBlackTreeTestUtils.buildBalance(
                            requiredBlackDepth = 2,
                            payloadRange = 1000.0..1500.0,
                        ),
                        rightChild = RedBlackTreeTestUtils.buildBalance(
                            requiredBlackDepth = 2,
                            payloadRange = 1500.0..2000.0,
                        ),
                    ),
                ),
                // Parent
                rightChild = NodeData(
                    payload = 3000.0,
                    color = Node.Color.Black,
                    // Removed node
                    leftChild = NodeData(
                        payload = 2500.0,
                        color = Node.Color.Black,
                    ),
                    // Sibling
                    rightChild = NodeData(
                        payload = 4000.0,
                        color = Node.Color.Red,
                        // Close nephew
                        leftChild = RedBlackTreeTestUtils.buildBalance(
                            requiredBlackDepth = 2,
                            payloadRange = 3000.0..4000.0,
                        ),
                        // Distant nephew
                        rightChild = RedBlackTreeTestUtils.buildBalance(
                            requiredBlackDepth = 2,
                            payloadRange = 4000.0..5000.0,
                        ),
                    ),
                ),
            ),
        )

        val node = tree.getNodeTraversing(payload = 2500.0)
        val expectedPredecessorNode = tree.rootNode!!

        val predecessorNode = tree.removeVerified(
            node = node,
        )

        assertEquals(
            expected = expectedPredecessorNode,
            actual = predecessorNode,
        )
    }

    /**
     * Complex removal (black leaf)
     * Case #3: The sibling is red (parent and nephews are black)
     *
     * Leads to Case #6, then done
     */
    @Test
    fun testRemove_blackLeaf_redSibling_to_redDistantNephew() {
        // Black height: 3
        val tree = RedBlackTreeTestUtils.loadVerified(
            // Grandparent
            rootData = NodeData(
                payload = 2000.0,
                color = Node.Color.Red,
                // Parent / predecessor
                leftChild = NodeData(
                    payload = 1000.0,
                    color = Node.Color.Black,
                    // Sibling
                    leftChild = NodeData(
                        payload = 500.0,
                        color = Node.Color.Red,
                        leftChild = RedBlackTreeTestUtils.buildBalance(
                            requiredBlackDepth = 2,
                            payloadRange = 0.0..500.0,
                        ),
                        rightChild = RedBlackTreeTestUtils.buildBalance(
                            requiredBlackDepth = 2,
                            payloadRange = 500.0..1000.0,
                        ),
                    ),
                    // Removed node
                    rightChild = NodeData(
                        payload = 1500.0,
                        color = Node.Color.Black,
                    ),
                ),
                // Parent's sibling
                rightChild = NodeData(
                    payload = 3000.0,
                    color = Node.Color.Black,
                    // Parent's close nephew
                    leftChild = RedBlackTreeTestUtils.buildBalance(
                        requiredBlackDepth = 2,
                        payloadRange = 2000.0..3000.0,
                    ),
                    // Parent's distant nephew
                    rightChild = NodeData(
                        payload = 4000.0,
                        color = Node.Color.Red,
                        leftChild = RedBlackTreeTestUtils.buildBalance(
                            requiredBlackDepth = 2,
                            payloadRange = 3000.0..4000.0,
                        ),
                        rightChild = RedBlackTreeTestUtils.buildBalance(
                            requiredBlackDepth = 2,
                            payloadRange = 4000.0..5000.0,
                        ),
                    ),
                ),
            ),
        )

        val node = tree.getNodeTraversing(payload = 1500.0)
        val expectedPredecessorNode = tree.getNodeTraversing(payload = 1000.0)

        val predecessorNode = tree.removeVerified(
            node = node,
        )

        assertEquals(
            expected = expectedPredecessorNode,
            actual = predecessorNode,
        )
    }

    /**
     * Complex removal (black leaf)
     * Case #4: Parent is red, nephews are nil (effectively black) (sibling is black)
     */
    @Test
    fun testRemove_blackLeaf_blackParentBlackNephews() {
        // Black height: 3
        val tree = RedBlackTreeTestUtils.loadVerified(
            // Grandparent
            rootData = NodeData(
                payload = 1000.0,
                color = Node.Color.Black,
                // Parent
                leftChild = NodeData(
                    payload = 500.0,
                    color = Node.Color.Red,
                    // Removed node
                    leftChild = NodeData(
                        payload = 250.0,
                        color = Node.Color.Black,
                    ),
                    // Sibling
                    rightChild = NodeData(
                        payload = 750.0,
                        color = Node.Color.Black,
                        // Nephews: nil
                    ),
                ),
                rightChild = RedBlackTreeTestUtils.buildBalance(
                    requiredBlackDepth = 2,
                    payloadRange = 1000.0..2000.0,
                ),
            ),
        )

        val node = tree.getNodeTraversing(payload = 250.0)

        val predecessorNode = tree.removeVerified(
            node = node,
        )

        assertNull(
            actual = predecessorNode,
        )
    }

    /**
     * Complex removal (black leaf)
     * Case #5: Close nephew is red (sibling is black), distant nephew is nil (effectively black)
     */
    @Test
    fun testRemove_blackLeaf_redCloseNephewBlackDistantNephew() {
        // Black height: 3
        val tree = RedBlackTreeTestUtils.loadVerified(
            // Grandparent
            rootData = NodeData(
                payload = 1000.0,
                color = Node.Color.Black,
                leftChild = RedBlackTreeTestUtils.buildBalance(
                    requiredBlackDepth = 2,
                    payloadRange = 0.0..1000.0,
                ),
                // Parent / predecessor
                rightChild = NodeData(
                    payload = 3000.0,
                    color = Node.Color.Red,
                    // Sibling
                    leftChild = NodeData(
                        payload = 2000.0,
                        color = Node.Color.Black,
                        // Distant nephew: nil
                        // Close nephew
                        rightChild = NodeData(
                            payload = 2500.0,
                            color = Node.Color.Red,
                        ),
                    ),
                    // Removed node
                    rightChild = NodeData(
                        payload = 3500.0,
                        color = Node.Color.Black,
                    ),
                ),
            ),
        )

        val node = tree.getNodeTraversing(payload = 3500.0)
        val expectedPredecessorNode = tree.getNodeTraversing(payload = 3000.0)

        val predecessorNode = tree.removeVerified(
            node = node,
        )

        assertEquals(
            expected = expectedPredecessorNode,
            actual = predecessorNode,
        )
    }

    /**
     * Complex removal (black leaf)
     * Case #6: Distant nephew is red (sibling is black) [parent: black]
     */
    @Test
    fun testRemove_blackLeaf_redDistantNephew_blackParent() {
        // Black height: 4
        val tree = RedBlackTreeTestUtils.loadVerified(
            // Grandparent
            rootData = NodeData(
                payload = 1000.0,
                color = Node.Color.Black,
                // parent
                leftChild = NodeData(
                    payload = 500.0,
                    color = Node.Color.Black,
                    // Removed node
                    leftChild = NodeData(
                        payload = 250.0,
                        color = Node.Color.Black,
                    ),
                    // Sibling
                    rightChild = NodeData(
                        payload = 750.0,
                        color = Node.Color.Black,
                        // Close nephew: nil
                        // Distant nephew
                        rightChild = NodeData(
                            payload = 800.0,
                            color = Node.Color.Red,
                        ),
                    ),
                ),
                rightChild = RedBlackTreeTestUtils.buildBalance(
                    requiredBlackDepth = 3,
                    payloadRange = 1000.0..2000.0,
                ),
            ),
        )

        val node = tree.getNodeTraversing(payload = 250.0)

        val predecessorNode = tree.removeVerified(
            node = node,
        )

        assertNull(
            actual = predecessorNode,
        )
    }

    /**
     * Complex removal (black leaf)
     * Case #6: Distant nephew is red (sibling is black) [parent: red]
     */
    @Test
    fun testRemove_blackLeaf_redDistantNephew_redParent() {
        // Black height: 4
        val tree = RedBlackTreeTestUtils.loadVerified(
            // parent
            rootData = NodeData(
                payload = 500.0,
                color = Node.Color.Red,
                // Removed node
                leftChild = NodeData(
                    payload = 250.0,
                    color = Node.Color.Black,
                ),
                // Sibling
                rightChild = NodeData(
                    payload = 750.0,
                    color = Node.Color.Black,
                    // Close nephew: nil
                    // Distant nephew
                    rightChild = NodeData(
                        payload = 800.0,
                        color = Node.Color.Red,
                    ),
                ),
            ),
        )

        val node = tree.getNodeTraversing(payload = 250.0)

        val predecessorNode = tree.removeVerified(
            node = node,
        )

        assertNull(
            actual = predecessorNode,
        )
    }
}
