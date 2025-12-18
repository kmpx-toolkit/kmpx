package dev.kmpx.collections.internal.data_structures.ordered_binary_tree

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree.Node
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree.RotationDirection
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.NodeData
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.attachVerified
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.collapseVerified
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.cutOffVerified
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.dump
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.rotateVerified
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.swapVerified
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.verifyIntegrity
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertIs
import kotlin.test.assertNull

@Suppress("ClassName")
class OrderedBinaryTree_tests {
    @Test
    fun test_initial() {
        val tree = OrderedBinaryTree.create<Int>()

        tree.verifyIntegrity()

        assertNull(
            actual = tree.dump(),
        )
    }

    @Test
    fun test_attach_root() {
        val tree = OrderedBinaryTree.create<Int>()

        val node100 = tree.attachVerified(
            location = OrderedBinaryTree.RootLocation.cast(),
            payload = 100,
            color = Node.Color.Red,
        )

        assertEquals(
            expected = NodeData(
                payload = 100,
                color = Node.Color.Red,
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

    @Test
    fun test_attach_ordinaryLeaf() {
        val tree = OrderedBinaryTree.create<Int>()

        val node100 = tree.attachVerified(
            location = OrderedBinaryTree.RootLocation.cast(),
            payload = 100,
            color = Node.Color.Red,
        )

        val node90 = tree.attachVerified(
            location = node100.getLeftChildLocation(),
            payload = 90,
            color = Node.Color.Black,
        )

        assertEquals(
            expected = node100,
            actual = node90.parent,
        )

        val node110 = tree.attachVerified(
            location = node100.getRightChildLocation(),
            payload = 110,
            color = Node.Color.Red,
        )

        assertEquals(
            expected = node100,
            actual = node110.parent,
        )

        assertEquals(
            expected = OrderedBinaryTree.RelativeLocation(
                parentNode = node100,
                side = OrderedBinaryTree.Side.Right,
            ),
            actual = node110.locate(),
        )

        assertEquals(
            expected = NodeData(
                payload = 100,
                color = Node.Color.Red,
                leftChild = NodeData(
                    payload = 90,
                    color = Node.Color.Black,
                ),
                rightChild = NodeData(
                    payload = 110,
                    color = Node.Color.Red,
                ),
            ),
            actual = tree.dump(),
        )
    }

    @Test
    fun test_cutOffLeaf_root() {
        val tree = OrderedBinaryTree.create<Int>()

        val node100 = tree.attachVerified(
            location = OrderedBinaryTree.RootLocation.cast(),
            payload = 100,
            color = Node.Color.Red,
        )

        val emptiedLocation = tree.cutOffVerified(leafNode = node100)

        assertEquals(
            expected = null,
            actual = tree.dump(),
        )

        assertEquals(
            expected = OrderedBinaryTree.RootLocation.cast(),
            actual = emptiedLocation,
        )
    }

    @Test
    fun test_cutOffLeaf_ordinaryLeaf_extremal() {
        val tree = OrderedBinaryTree.create<Int>()

        val node100 = tree.attachVerified(
            location = OrderedBinaryTree.RootLocation.cast(),
            payload = 100,
            color = Node.Color.Red,
        )

        tree.attachVerified(
            location = node100.getLeftChildLocation(),
            payload = 90,
            color = Node.Color.Black,
        )

        val node110 = tree.attachVerified(
            location = node100.getRightChildLocation(),
            payload = 110,
            color = Node.Color.Red,
        )

        val emptiedLocation = tree.cutOffVerified(leafNode = node110)

        assertEquals(
            expected = NodeData(
                payload = 100,
                color = Node.Color.Red,
                leftChild = NodeData(
                    payload = 90,
                    color = Node.Color.Black,
                ),
            ),
            actual = tree.dump(),
        )

        assertEquals(
            expected = OrderedBinaryTree.RelativeLocation(
                parentNode = node100,
                side = OrderedBinaryTree.Side.Right,
            ),
            actual = emptiedLocation,
        )
    }

    @Test
    fun test_cutOffLeaf_ordinaryLeaf_nonExtremal() {
        val tree = OrderedBinaryTree.create<Int>()

        val node100 = tree.attachVerified(
            location = OrderedBinaryTree.RootLocation.cast(),
            payload = 100,
            color = Node.Color.Red,
        )

        val node50 = tree.attachVerified(
            location = node100.getLeftChildLocation(),
            payload = 50,
            color = Node.Color.Black,
        )

        val node75 = tree.attachVerified(
            location = node50.getRightChildLocation(),
            payload = 75,
            color = Node.Color.Red,
        )

        tree.attachVerified(
            location = node100.getRightChildLocation(),
            payload = 110,
            color = Node.Color.Black,
        )

        val emptiedLocation = tree.cutOffVerified(leafNode = node75)

        assertEquals(
            expected = NodeData(
                payload = 100,
                color = Node.Color.Red,
                leftChild = NodeData(
                    payload = 50,
                    color = Node.Color.Black,
                ),
                rightChild = NodeData(
                    payload = 110,
                    color = Node.Color.Black,
                ),
            ),
            actual = tree.dump(),
        )

        assertEquals(
            expected = OrderedBinaryTree.RelativeLocation(
                parentNode = node50,
                side = OrderedBinaryTree.Side.Right,
            ),
            actual = emptiedLocation,
        )
    }

    @Test
    fun test_cutOffLeaf_nonLeaf() {
        val tree = OrderedBinaryTree.create<Int>()

        val node100 = tree.attachVerified(
            location = OrderedBinaryTree.RootLocation.cast(),
            payload = 100,
            color = Node.Color.Black,
        )

        tree.attachVerified(
            location = node100.getLeftChildLocation(),
            payload = 90,
            color = Node.Color.Red,
        )

        val node110 = tree.attachVerified(
            location = node100.getRightChildLocation(),
            payload = 110,
            color = Node.Color.Red,
        )

        tree.attachVerified(
            location = node110.getLeftChildLocation(),
            payload = 105,
            color = Node.Color.Red,
        )

        tree.attachVerified(
            location = node110.getRightChildLocation(),
            payload = 120,
            color = Node.Color.Red,
        )

        assertIs<IllegalArgumentException>(
            assertFails {
                tree.cutOffLeaf(leafNode = node110)
            },
        )
    }

    @Test
    fun test_collapse_root() {
        val tree = OrderedBinaryTree.create<Int>()

        val node100 = tree.attachVerified(
            location = OrderedBinaryTree.RootLocation.cast(),
            payload = 100,
            color = Node.Color.Red,
        )

        val node90 = tree.attachVerified(
            location = node100.getLeftChildLocation(),
            payload = 90,
            color = Node.Color.Black,
        )

        val node95 = tree.attachVerified(
            location = node90.getRightChildLocation(),
            payload = 95,
            color = Node.Color.Red,
        )

        assertEquals(
            expected = node95,
            actual = tree.collapseVerified(
                node = node100,
                side = OrderedBinaryTree.Side.Left,
            ),
        )

        assertEquals(
            expected = NodeData(
                payload = 90,
                color = Node.Color.Black,
                rightChild = NodeData(
                    payload = 95,
                    color = Node.Color.Red,
                ),
            ),
            actual = tree.dump(),
        )
    }

    @Test
    fun test_collapse_rootChild() {
        val tree = OrderedBinaryTree.create<Int>()

        val node100 = tree.attachVerified(
            location = OrderedBinaryTree.RootLocation.cast(),
            payload = 100,
            color = Node.Color.Red,
        )

        val node90 = tree.attachVerified(
            location = node100.getLeftChildLocation(),
            payload = 90,
            color = Node.Color.Red,
        )

        val node95 = tree.attachVerified(
            location = node90.getRightChildLocation(),
            payload = 95,
            color = Node.Color.Black,
        )

        assertEquals(
            expected = node95,
            actual = tree.collapseVerified(
                node = node90,
                side = OrderedBinaryTree.Side.Right,
            ),
        )

        assertEquals(
            expected = NodeData(
                payload = 100,
                color = Node.Color.Red,
                leftChild = NodeData(
                    payload = 95,
                    color = Node.Color.Black,
                ),
            ),
            actual = tree.dump(),
        )
    }

    /**
     * - Collapsed non-root node having one child (on the right side)
     * - Collapsed node is not an extremal (minimal) value on the child's opposite side (left)
     */
    @Test
    fun test_collapse_ordinarySingleChild_nonExtremal() {
        val tree = OrderedBinaryTree.create<Int>()

        val node100 = tree.attachVerified(
            location = OrderedBinaryTree.RootLocation.cast(),
            payload = 100,
            color = Node.Color.Black,
        )

        val node200 = tree.attachVerified(
            location = node100.getRightChildLocation(),
            payload = 200,
            color = Node.Color.Red,
        )

        val node300 = tree.attachVerified(
            location = node200.getRightChildLocation(),
            payload = 300,
            color = Node.Color.Red,
        )

        assertEquals(
            expected = node300,
            actual = tree.collapseVerified(
                node = node200,
                side = OrderedBinaryTree.Side.Right,
            ),
        )

        assertEquals(
            expected = NodeData(
                payload = 100,
                color = Node.Color.Black,
                rightChild = NodeData(
                    payload = 300,
                    color = Node.Color.Red,
                ),
            ),
            actual = tree.dump(),
        )
    }

    /**
     * - Collapsed non-root node having one child (on the left side)
     * - Collapsed node is an extremal (maximal) value on the child's opposite side (right)
     */
    @Test
    fun test_collapse_ordinarySingleChild_extremal() {
        val tree = OrderedBinaryTree.create<Int>()

        val node100 = tree.attachVerified(
            location = OrderedBinaryTree.RootLocation.cast(),
            payload = 100,
            color = Node.Color.Black,
        )

        tree.attachVerified(
            location = node100.getLeftChildLocation(),
            payload = 90,
            color = Node.Color.Red,
        )

        val node110 = tree.attachVerified(
            location = node100.getRightChildLocation(),
            payload = 110,
            color = Node.Color.Red,
        )

        val node105 = tree.attachVerified(
            location = node110.getLeftChildLocation(),
            payload = 105,
            color = Node.Color.Red,
        )

        assertEquals(
            expected = node105,
            actual = tree.collapseVerified(
                node = node110,
                side = OrderedBinaryTree.Side.Left,
            ),
        )

        assertEquals(
            expected = NodeData(
                payload = 100,
                color = Node.Color.Black,
                leftChild = NodeData(
                    payload = 90,
                    color = Node.Color.Red,
                ),
                rightChild = NodeData(
                    payload = 105,
                    color = Node.Color.Red,
                ),
            ),
            actual = tree.dump(),
        )
    }

    @Test
    fun test_collapse_leaf() {
        val tree = OrderedBinaryTree.create<Int>()

        val node100 = tree.attachVerified(
            location = OrderedBinaryTree.RootLocation.cast(),
            payload = 100,
            color = Node.Color.Red,
        )

        tree.attachVerified(
            location = node100.getLeftChildLocation(),
            payload = 90,
            color = Node.Color.Red,
        )

        val node110 = tree.attachVerified(
            location = node100.getRightChildLocation(),
            payload = 110,
            color = Node.Color.Red,
        )

        val node105 = tree.attachVerified(
            location = node110.getLeftChildLocation(),
            payload = 105,
            color = Node.Color.Red,
        )

        tree.attachVerified(
            location = node110.getRightChildLocation(),
            payload = 115,
            color = Node.Color.Red,
        )

        assertIs<IllegalArgumentException>(
            assertFails {
                tree.collapseVerified(
                    node = node105,
                    side = OrderedBinaryTree.Side.Left,
                )
            },
        )

        assertIs<IllegalArgumentException>(
            assertFails {
                tree.collapseVerified(
                    node = node105,
                    side = OrderedBinaryTree.Side.Right,
                )
            },
        )
    }

    @Test
    fun test_collapse_twoChildren() {
        val tree = OrderedBinaryTree.create<Int>()

        val node100 = tree.attachVerified(
            location = OrderedBinaryTree.RootLocation.cast(),
            payload = 100,
            color = Node.Color.Red,
        )

        tree.attachVerified(
            location = node100.getLeftChildLocation(),
            payload = 90,
            color = Node.Color.Red,
        )

        val node110 = tree.attachVerified(
            location = node100.getRightChildLocation(),
            payload = 110,
            color = Node.Color.Red,
        )

        tree.attachVerified(
            location = node110.getLeftChildLocation(),
            payload = 105,
            color = Node.Color.Red,
        )

        tree.attachVerified(
            location = node110.getRightChildLocation(),
            payload = 115,
            color = Node.Color.Red,
        )

        assertIs<IllegalArgumentException>(
            assertFails {
                tree.collapseVerified(
                    node = node110,
                    side = OrderedBinaryTree.Side.Left,
                )
            },
        )

        assertIs<IllegalArgumentException>(
            assertFails {
                tree.collapseVerified(
                    node = node110,
                    side = OrderedBinaryTree.Side.Right,
                )
            },
        )
    }

    /**
     * - The swapped node is root
     * - Primary side: right (neighbour = successor)
     * - The swapped node doesn't have a child on the opposite side (left)
     *   - As it's root, it must be an extremal (minimal) value (link = null)
     * - The neighbour (successor) is separated by more than one node
     * - The neighbour (successor) doesn't have a child on the primary side (right)
     */
    @Test
    fun test_swap_root_noOppositeChild_successor_distant_neighbourNoOppositeChild() {
        val tree = OrderedBinaryTree.create<Int>()

        // The swapped node (root)
        val node200 = tree.attachVerified(
            location = OrderedBinaryTree.RootLocation.cast(),
            payload = 200,
            color = Node.Color.Black,
        )

        val node300 = tree.attachVerified(
            location = node200.getRightChildLocation(),
            payload = 300,
            color = Node.Color.Red,
        )

        val node260 = tree.attachVerified(
            location = node300.getLeftChildLocation(),
            payload = 260,
            color = Node.Color.Black,
        )

        // The successor
        val node240 = tree.attachVerified(
            location = node260.getLeftChildLocation(),
            payload = 240,
            color = Node.Color.Red,
        )

        tree.swapVerified(
            node = node200,
            side = OrderedBinaryTree.Side.Right,
        )

        // Assert that swapped nodes preserved their payloads but inherited the
        // other node's color

        assertEquals(
            expected = 200,
            actual = node200.payload,
        )

        assertEquals(
            expected = Node.Color.Red,
            actual = node200.color,
        )

        assertEquals(
            expected = 240,
            actual = node240.payload,
        )

        assertEquals(
            expected = Node.Color.Black,
            actual = node240.color,
        )

        assertEquals(
            expected = NodeData(
                payload = 240,
                color = Node.Color.Black,
                rightChild = NodeData(
                    payload = 300,
                    color = Node.Color.Red,
                    leftChild = NodeData(
                        payload = 260,
                        color = Node.Color.Black,
                        leftChild = NodeData(
                            payload = 200,
                            color = Node.Color.Red,
                        ),
                    ),
                ),
            ),
            actual = tree.dump(),
        )
    }

    /**
     * - The swapped node is non-root
     * - Primary side: left (neighbour = predecessor)
     * - The swapped node doesn't have a child on the opposite side (right)
     *   - It is an extremal (maximal) value (link = null)
     * - The neighbour (predecessor) is separated by one node
     * - The neighbour (predecessor) has a child on the primary side (left)
     */
    @Test
    fun test_swap_nonRoot_noOppositeChild_extremal_predecessor_separated_neighbourHasOppositeChild() {
        val tree = OrderedBinaryTree.create<Int>()

        val node100 = tree.attachVerified(
            location = OrderedBinaryTree.RootLocation.cast(),
            payload = 100,
            color = Node.Color.Red,
        )

        tree.attachVerified(
            location = node100.getLeftChildLocation(),
            payload = 90,
            color = Node.Color.Black,
        )

        // The swapped node
        val node200 = tree.attachVerified(
            location = node100.getRightChildLocation(),
            payload = 200,
            color = Node.Color.Red,
        )

        val node150 = tree.attachVerified(
            location = node200.getLeftChildLocation(),
            payload = 150,
            color = Node.Color.Red,
        )

        // The predecessor
        val node160 = tree.attachVerified(
            location = node150.getRightChildLocation(),
            payload = 160,
            color = Node.Color.Black,
        )

        // The predecessor's child on the primary side
        tree.attachVerified(
            location = node160.getLeftChildLocation(),
            payload = 155,
            color = Node.Color.Black,
        )

        tree.swapVerified(
            node = node200,
            side = OrderedBinaryTree.Side.Left,
        )

        // Assert that swapped nodes preserved their payloads but inherited the
        // other node's color

        assertEquals(
            expected = 200,
            actual = node200.payload,
        )

        assertEquals(
            expected = Node.Color.Black,
            actual = node200.color,
        )

        assertEquals(
            expected = 160,
            actual = node160.payload,
        )

        assertEquals(
            expected = Node.Color.Red,
            actual = node160.color,
        )

        assertEquals(
            expected = NodeData(
                payload = 100,
                color = Node.Color.Red,
                leftChild = NodeData(
                    payload = 90,
                    color = Node.Color.Black,
                ),
                rightChild = NodeData(
                    payload = 160,
                    color = Node.Color.Red,
                    leftChild = NodeData(
                        payload = 150,
                        color = Node.Color.Red,
                        rightChild = NodeData(
                            payload = 200,
                            color = Node.Color.Black,
                            leftChild = NodeData(
                                payload = 155,
                                color = Node.Color.Black,
                            ),
                        ),
                    ),
                ),
            ),
            actual = tree.dump(),
        )
    }

    /**
     * - The swapped node is non-root
     * - Primary side: right (neighbour = successor)
     * - The swapped node doesn't have a child on the opposite side (left)
     *   - It is not an extremal (minimal) value (link = predecessor)
     * - The neighbour (successor) is separated by one node
     * - The neighbour (successor) doesn't have a child on the primary side (right)
     */
    @Test
    fun test_swap_nonRoot_noOppositeChild_nonExtremal_successor_separated_neighbourNoOppositeChild() {
        val tree = OrderedBinaryTree.create<Int>()

        val node50 = tree.attachVerified(
            location = OrderedBinaryTree.RootLocation.cast(),
            payload = 50,
            color = Node.Color.Red,
        )

        tree.attachVerified(
            location = node50.getLeftChildLocation(),
            payload = 25,
            color = Node.Color.Black,
        )

        // The swapped node
        val node100 = tree.attachVerified(
            location = node50.getRightChildLocation(),
            payload = 100,
            color = Node.Color.Red,
        )

        val node200 = tree.attachVerified(
            location = node100.getRightChildLocation(),
            payload = 200,
            color = Node.Color.Red,
        )

        // The successor
        val node150 = tree.attachVerified(
            location = node200.getLeftChildLocation(),
            payload = 150,
            color = Node.Color.Black,
        )

        tree.swapVerified(
            node = node100,
            side = OrderedBinaryTree.Side.Right,
        )

        // Assert that swapped nodes preserved their payloads but inherited the
        // other node's color

        assertEquals(
            expected = 100,
            actual = node100.payload,
        )

        assertEquals(
            expected = Node.Color.Black,
            actual = node100.color,
        )

        assertEquals(
            expected = 150,
            actual = node150.payload,
        )

        assertEquals(
            expected = Node.Color.Red,
            actual = node150.color,
        )

        assertEquals(
            expected = NodeData(
                payload = 50,
                color = Node.Color.Red,
                leftChild = NodeData(
                    payload = 25,
                    color = Node.Color.Black,
                ),
                rightChild = NodeData(
                    payload = 150,
                    color = Node.Color.Red,
                    rightChild = NodeData(
                        payload = 200,
                        color = Node.Color.Red,
                        leftChild = NodeData(
                            payload = 100,
                            color = Node.Color.Black,
                        ),
                    ),
                ),
            ),
            actual = tree.dump(),
        )
    }

    /**
     * - The swapped node is non-root
     * - Primary side: right (neighbour = successor)
     * - The swapped node has a child on the opposite side (left) (link = child)
     * - The neighbour (successor) is the direct child
     * - The neighbour (successor) has a child on the primary side (right)
     */
    @Test
    fun test_swap_nonRoot_hasOppositeChild_successor_directChild_neighbourHasOppositeChild() {
        val tree = OrderedBinaryTree.create<Int>()

        val node100 = tree.attachVerified(
            location = OrderedBinaryTree.RootLocation.cast(),
            payload = 100,
            color = Node.Color.Red,
        )

        tree.attachVerified(
            location = node100.getLeftChildLocation(),
            payload = 90,
            color = Node.Color.Black,
        )

        // The swapped node
        val node200 = tree.attachVerified(
            location = node100.getRightChildLocation(),
            payload = 200,
            color = Node.Color.Red,
        )

        tree.attachVerified(
            location = node200.getLeftChildLocation(),
            payload = 150,
            color = Node.Color.Red,
        )

        // The successor
        val node300 = tree.attachVerified(
            location = node200.getRightChildLocation(),
            payload = 300,
            color = Node.Color.Black,
        )

        // The successor's child on the primary side
        tree.attachVerified(
            location = node300.getRightChildLocation(),
            payload = 400,
            color = Node.Color.Red,
        )

        tree.swapVerified(
            node = node200,
            side = OrderedBinaryTree.Side.Right,
        )

        // Assert that swapped nodes preserved their payloads but inherited the
        // other node's color

        assertEquals(
            expected = 200,
            actual = node200.payload,
        )

        assertEquals(
            expected = Node.Color.Black,
            actual = node200.color,
        )

        assertEquals(
            expected = 300,
            actual = node300.payload,
        )

        assertEquals(
            expected = Node.Color.Red,
            actual = node300.color,
        )

        assertEquals(
            expected = NodeData(
                payload = 100,
                color = Node.Color.Red,
                leftChild = NodeData(
                    payload = 90,
                    color = Node.Color.Black,
                ),
                rightChild = NodeData(
                    payload = 300,
                    color = Node.Color.Red,
                    leftChild = NodeData(
                        payload = 150,
                        color = Node.Color.Red,
                    ),
                    rightChild = NodeData(
                        payload = 200,
                        color = Node.Color.Black,
                        rightChild = NodeData(
                            payload = 400,
                            color = Node.Color.Red,
                        ),
                    ),
                ),
            ),
            actual = tree.dump(),
        )
    }

    /**
     * - The swapped node is non-root
     * - Primary side: right (neighbour = successor)
     * - The swapped node doesn't have a child on the opposite side (left) (link = predecessor)
     * - The neighbour (successor) is separated by one node
     * - The neighbour (successor) has a child on the primary side (right)
     */
    @Test
    fun test_swap_nonRoot_hasOppositeChild_successor_separated_neighbourHasOppositeChild() {
        val tree = OrderedBinaryTree.create<Int>()

        val node100 = tree.attachVerified(
            location = OrderedBinaryTree.RootLocation.cast(),
            payload = 100,
            color = Node.Color.Red,
        )

        // The swapped node
        val node150 = tree.attachVerified(
            location = node100.getRightChildLocation(),
            payload = 150,
            color = Node.Color.Red,
        )

        val node180 = tree.attachVerified(
            location = node150.getRightChildLocation(),
            payload = 180,
            color = Node.Color.Red,
        )

        // The successor
        val node170 = tree.attachVerified(
            location = node180.getLeftChildLocation(),
            payload = 170,
            color = Node.Color.Black,
        )

        // The successor's child on the primary side
        val node172 = tree.attachVerified(
            location = node170.getRightChildLocation(),
            payload = 172,
            color = Node.Color.Red,
        )

        // The successor's own successor
        tree.attachVerified(
            location = node172.getLeftChildLocation(),
            payload = 171,
            color = Node.Color.Black,
        )

        tree.swapVerified(
            node = node150,
            side = OrderedBinaryTree.Side.Right,
        )

        // Assert that swapped nodes preserved their payloads but inherited the
        // other node's color

        assertEquals(
            expected = 150,
            actual = node150.payload,
        )

        assertEquals(
            expected = Node.Color.Black,
            actual = node150.color,
        )

        assertEquals(
            expected = 170,
            actual = node170.payload,
        )

        assertEquals(
            expected = Node.Color.Red,
            actual = node170.color,
        )

        assertEquals(
            expected = NodeData(
                payload = 100,
                color = Node.Color.Red,
                rightChild = NodeData(
                    payload = 170,
                    color = Node.Color.Red,
                    rightChild = NodeData(
                        payload = 180,
                        color = Node.Color.Red,
                        leftChild = NodeData(
                            payload = 150,
                            color = Node.Color.Black,
                            rightChild = NodeData(
                                payload = 172,
                                color = Node.Color.Red,
                                leftChild = NodeData(
                                    payload = 171,
                                    color = Node.Color.Black,
                                ),
                            ),
                        ),
                    ),
                ),
            ),
            actual = tree.dump(),
        )
    }

    /**
     * - Pivot is non-root
     * - Rotation direction: counter-clockwise
     * - The ascending node has a child on the rotation direction's end side (left)
     *
     * Before rotation:
     *
     *  100
     *            150
     *       125        175
     *    110  130   160    180
     *
     *
     * After rotation:
     *
     *  100
     *               175
     *          150        180
     *       125  160
     *    110  130
     */
    @Test
    fun test_rotate_ccw_ascendingHasEndChild() {
        val tree = OrderedBinaryTree.create<Int>()

        val node100 = tree.attachVerified(
            location = OrderedBinaryTree.RootLocation.cast(),
            payload = 100,
            color = Node.Color.Red,
        )

        val node150 = tree.attachVerified(
            location = node100.getRightChildLocation(),
            payload = 150,
            color = Node.Color.Red,
        )

        val node125 = tree.attachVerified(
            location = node150.getLeftChildLocation(),
            payload = 125,
            color = Node.Color.Black,
        )

        val node175 = tree.attachVerified(
            location = node150.getRightChildLocation(),
            payload = 175,
            color = Node.Color.Black,
        )

        tree.attachVerified(
            location = node125.getLeftChildLocation(),
            payload = 110,
            color = Node.Color.Red,
        )

        tree.attachVerified(
            location = node125.getRightChildLocation(),
            payload = 130,
            color = Node.Color.Black,
        )

        tree.attachVerified(
            location = node175.getLeftChildLocation(),
            payload = 160,
            color = Node.Color.Black,
        )

        tree.attachVerified(
            location = node175.getRightChildLocation(),
            payload = 180,
            color = Node.Color.Red,
        )

        tree.rotateVerified(
            pivotNode = node150,
            direction = RotationDirection.CounterClockwise,
        )

        assertEquals(
            expected = NodeData(
                payload = 100,
                color = Node.Color.Red,
                rightChild = NodeData(
                    payload = 175,
                    color = Node.Color.Black,
                    leftChild = NodeData(
                        payload = 150,
                        color = Node.Color.Red,
                        leftChild = NodeData(
                            payload = 125,
                            color = Node.Color.Black,
                            leftChild = NodeData(
                                payload = 110,
                                color = Node.Color.Red,
                            ),
                            rightChild = NodeData(
                                payload = 130,
                                color = Node.Color.Black,
                            ),
                        ),
                        rightChild = NodeData(
                            payload = 160,
                            color = Node.Color.Black,
                        ),
                    ),
                    rightChild = NodeData(
                        payload = 180,
                        color = Node.Color.Red,
                    ),
                ),
            ),
            actual = tree.dump(),
        )
    }

    /**
     * - Pivot is root
     * - Rotation direction: clockwise
     * - The ascending node doesn't have a child on the rotation direction's end side (right)
     *
     * Before rotation:
     *
     *            150
     *       125        175
     *    110
     *
     *
     * After rotation:
     *
     *       125
     *    110    150
     *               175
     */
    @Test
    fun test_rotate_cw_ascendingNoEndChild() {
        val tree = OrderedBinaryTree.create<Int>()

        val node150 = tree.attachVerified(
            location = OrderedBinaryTree.RootLocation.cast(),
            payload = 150,
            color = Node.Color.Red,
        )

        val node125 = tree.attachVerified(
            location = node150.getLeftChildLocation(),
            payload = 125,
            color = Node.Color.Black,
        )

        tree.attachVerified(
            location = node150.getRightChildLocation(),
            payload = 175,
            color = Node.Color.Black,
        )

        tree.attachVerified(
            location = node125.getLeftChildLocation(),
            payload = 110,
            color = Node.Color.Red,
        )

        tree.rotateVerified(
            pivotNode = node150,
            direction = RotationDirection.Clockwise,
        )

        assertEquals(
            expected = NodeData(
                payload = 125,
                color = Node.Color.Black,
                leftChild = NodeData(
                    payload = 110,
                    color = Node.Color.Red,
                ),
                rightChild = NodeData(
                    payload = 150,
                    color = Node.Color.Red,
                    rightChild = NodeData(
                        payload = 175,
                        color = Node.Color.Black,
                    ),
                ),
            ),
            actual = tree.dump(),
        )
    }
}
