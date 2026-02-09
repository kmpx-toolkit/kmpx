package dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree.Node
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree.RootLocation
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree.RelativeLocation
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree.Side
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.NodeData
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.load
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

@Suppress("ClassName")
class OrderedBinaryTree_strictLookup_tests {
    @Test
    fun test_findExactWith_emptyTree() {
        val tree = OrderedBinaryTree.create<Int>()

        val location = tree.findExactWith(
            comparator = BoundComparator.compareTo(50),
        )

        assertIs<RootLocation>(location)
    }

    @Test
    fun test_findExactWith_singleNode_found() {
        val tree = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = 50,
                color = Node.Color.Black,
            ),
        )

        val location = tree.findExactWith(
            comparator = BoundComparator.compareTo(50),
        )

        assertIs<RootLocation>(location)
    }

    @Test
    fun test_findExactWith_singleNode_notFound_less() {
        val tree = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = 50,
                color = Node.Color.Black,
            ),
        )

        val location = tree.findExactWith(
            comparator = BoundComparator.compareTo(25),
        )

        assertIs<RelativeLocation<Int>>(location)
        assertEquals(expected = tree.rootNode, actual = location.parentNode)
        assertEquals(expected = Side.Left, actual = location.side)
        assertNull(location.occupyingNode)
    }

    @Test
    fun test_findExactWith_singleNode_notFound_greater() {
        val tree = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = 50,
                color = Node.Color.Black,
            ),
        )

        val location = tree.findExactWith(
            comparator = BoundComparator.compareTo(75),
        )

        assertIs<RelativeLocation<Int>>(location)
        assertEquals(expected = tree.rootNode, actual = location.parentNode)
        assertEquals(expected = Side.Right, actual = location.side)
        assertNull(location.occupyingNode)
    }

    @Test
    fun test_findExactWith_multiNode_foundAtRoot() {
        val tree = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = 50,
                color = Node.Color.Black,
                leftChild = NodeData(
                    payload = 25,
                    color = Node.Color.Red,
                ),
                rightChild = NodeData(
                    payload = 75,
                    color = Node.Color.Red,
                ),
            ),
        )

        val location = tree.findExactWith(
            comparator = BoundComparator.compareTo(50),
        )

        assertIs<RootLocation>(location)
    }

    @Test
    fun test_findExactWith_multiNode_foundInLeftSubtree() {
        val tree = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = 50,
                color = Node.Color.Black,
                leftChild = NodeData(
                    payload = 25,
                    color = Node.Color.Red,
                ),
                rightChild = NodeData(
                    payload = 75,
                    color = Node.Color.Red,
                ),
            ),
        )

        val location = tree.findExactWith(
            comparator = BoundComparator.compareTo(25),
        )

        assertIs<RelativeLocation<Int>>(location)
        assertEquals(expected = tree.rootNode, actual = location.parentNode)
        assertEquals(expected = Side.Left, actual = location.side)
        assertEquals(expected = 25, actual = location.occupyingNode?.payload)
    }

    @Test
    fun test_findExactWith_multiNode_foundInRightSubtree() {
        val tree = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = 50,
                color = Node.Color.Black,
                leftChild = NodeData(
                    payload = 25,
                    color = Node.Color.Red,
                ),
                rightChild = NodeData(
                    payload = 75,
                    color = Node.Color.Red,
                ),
            ),
        )

        val location = tree.findExactWith(
            comparator = BoundComparator.compareTo(75),
        )

        assertIs<RelativeLocation<Int>>(location)
        assertEquals(expected = tree.rootNode, actual = location.parentNode)
        assertEquals(expected = Side.Right, actual = location.side)
        assertEquals(expected = 75, actual = location.occupyingNode?.payload)
    }

    @Test
    fun test_findExactWith_multiNode_foundDeep() {
        val tree = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = 50,
                color = Node.Color.Black,
                leftChild = NodeData(
                    payload = 25,
                    color = Node.Color.Red,
                    leftChild = NodeData(
                        payload = 10,
                        color = Node.Color.Black,
                    ),
                    rightChild = NodeData(
                        payload = 30,
                        color = Node.Color.Black,
                    ),
                ),
                rightChild = NodeData(
                    payload = 75,
                    color = Node.Color.Red,
                ),
            ),
        )

        val location = tree.findExactWith(
            comparator = BoundComparator.compareTo(30),
        )

        assertIs<RelativeLocation<Int>>(location)
        assertEquals(expected = 25, actual = location.parentNode.payload)
        assertEquals(expected = Side.Right, actual = location.side)
        assertEquals(expected = 30, actual = location.occupyingNode?.payload)
    }

    @Test
    fun test_findExactWith_multiNode_notFound_between() {
        val tree = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = 50,
                color = Node.Color.Black,
                leftChild = NodeData(
                    payload = 25,
                    color = Node.Color.Red,
                ),
                rightChild = NodeData(
                    payload = 75,
                    color = Node.Color.Red,
                ),
            ),
        )

        val location = tree.findExactWith(
            comparator = BoundComparator.compareTo(60),
        )

        assertIs<RelativeLocation<Int>>(location)
        assertEquals(expected = 75, actual = location.parentNode.payload)
        assertEquals(expected = Side.Left, actual = location.side)
        assertNull(location.occupyingNode)
    }

    @Test
    fun test_findExactWith_multiNode_notFound_lessThanMin() {
        val tree = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = 50,
                color = Node.Color.Black,
                leftChild = NodeData(
                    payload = 25,
                    color = Node.Color.Red,
                ),
                rightChild = NodeData(
                    payload = 75,
                    color = Node.Color.Red,
                ),
            ),
        )

        val location = tree.findExactWith(
            comparator = BoundComparator.compareTo(10),
        )

        assertIs<RelativeLocation<Int>>(location)
        assertEquals(expected = 25, actual = location.parentNode.payload)
        assertEquals(expected = Side.Left, actual = location.side)
        assertNull(location.occupyingNode)
    }

    @Test
    fun test_findExactWith_multiNode_notFound_greaterThanMax() {
        val tree = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = 50,
                color = Node.Color.Black,
                leftChild = NodeData(
                    payload = 25,
                    color = Node.Color.Red,
                ),
                rightChild = NodeData(
                    payload = 75,
                    color = Node.Color.Red,
                ),
            ),
        )

        val location = tree.findExactWith(
            comparator = BoundComparator.compareTo(100),
        )

        assertIs<RelativeLocation<Int>>(location)
        assertEquals(expected = 75, actual = location.parentNode.payload)
        assertEquals(expected = Side.Right, actual = location.side)
        assertNull(location.occupyingNode)
    }
}

