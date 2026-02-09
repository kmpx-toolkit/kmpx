package dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree.Node
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.NodeData
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.load
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@Suppress("ClassName")
class OrderedBinaryTree_strictFloorLookup_tests {

    @Test
    fun test_findFloorWith_emptyTree() {
        val tree = OrderedBinaryTree.create<Int>()

        val floorNode = tree.findFloorWith(
            comparator = BoundComparator.compareTo(50),
        )

        assertNull(floorNode)
    }

    @Test
    fun test_findFloorWith_singleNode_exactMatch() {
        val tree = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = 50,
                color = Node.Color.Black,
            ),
        )

        val floorNode = tree.findFloorWith(
            comparator = BoundComparator.compareTo(50),
        )

        assertEquals(expected = 50, actual = floorNode?.payload)
    }

    @Test
    fun test_findFloorWith_singleNode_floorExists() {
        val tree = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = 50,
                color = Node.Color.Black,
            ),
        )

        val floorNode = tree.findFloorWith(
            comparator = BoundComparator.compareTo(75),
        )

        assertEquals(expected = 50, actual = floorNode?.payload)
    }

    @Test
    fun test_findFloorWith_singleNode_noFloor() {
        val tree = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = 50,
                color = Node.Color.Black,
            ),
        )

        val floorNode = tree.findFloorWith(
            comparator = BoundComparator.compareTo(25),
        )

        assertNull(floorNode)
    }

    @Test
    fun test_findFloorWith_multiNode_exactMatch() {
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

        val floorNode = tree.findFloorWith(
            comparator = BoundComparator.compareTo(25),
        )

        assertEquals(expected = 25, actual = floorNode?.payload)
    }

    @Test
    fun test_findFloorWith_multiNode_floorExists() {
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

        val floorNode = tree.findFloorWith(
            comparator = BoundComparator.compareTo(60),
        )

        assertEquals(expected = 50, actual = floorNode?.payload)
    }

    @Test
    fun test_findFloorWith_multiNode_noFloor() {
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

        val floorNode = tree.findFloorWith(
            comparator = BoundComparator.compareTo(10),
        )

        assertNull(floorNode)
    }

    @Test
    fun test_findFloorWith_multiNode_floorIsMax() {
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

        val floorNode = tree.findFloorWith(
            comparator = BoundComparator.compareTo(100),
        )

        assertEquals(expected = 75, actual = floorNode?.payload)
    }

    @Test
    fun test_findFloorWith_multiNode_floorIsRoot() {
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

        val floorNode = tree.findFloorWith(
            comparator = BoundComparator.compareTo(60),
        )

        assertEquals(expected = 50, actual = floorNode?.payload)
    }

    @Test
    fun test_findFloorWith_deepTree_floorAtVariousDepths() {
        // Tree structure:
        //              50
        //            /    \
        //          25      75
        //         /  \    /  \
        //       10   30  60   90
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
                    leftChild = NodeData(
                        payload = 60,
                        color = Node.Color.Black,
                    ),
                    rightChild = NodeData(
                        payload = 90,
                        color = Node.Color.Black,
                    ),
                ),
            ),
        )

        // Floor at depth 2 (leaf level)
        val floorNode1 = tree.findFloorWith(
            comparator = BoundComparator.compareTo(35),
        )
        assertEquals(expected = 30, actual = floorNode1?.payload)

        // Floor at depth 1
        val floorNode2 = tree.findFloorWith(
            comparator = BoundComparator.compareTo(55),
        )
        assertEquals(expected = 50, actual = floorNode2?.payload)

        // Floor at depth 2 (right subtree)
        val floorNode3 = tree.findFloorWith(
            comparator = BoundComparator.compareTo(70),
        )
        assertEquals(expected = 60, actual = floorNode3?.payload)

        // Floor is exact match at leaf
        val floorNode4 = tree.findFloorWith(
            comparator = BoundComparator.compareTo(90),
        )
        assertEquals(expected = 90, actual = floorNode4?.payload)

        // No floor (less than minimum)
        val floorNode5 = tree.findFloorWith(
            comparator = BoundComparator.compareTo(5),
        )
        assertNull(floorNode5)
    }
}

