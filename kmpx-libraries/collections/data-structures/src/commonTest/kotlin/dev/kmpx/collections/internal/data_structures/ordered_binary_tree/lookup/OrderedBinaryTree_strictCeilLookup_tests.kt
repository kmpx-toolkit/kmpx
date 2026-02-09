package dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree.Node
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.NodeData
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.load
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@Suppress("ClassName")
class OrderedBinaryTree_strictCeilLookup_tests {
    @Test
    fun test_findCeilWith_emptyTree() {
        val tree = OrderedBinaryTree.create<Int>()

        val ceilNode = tree.findCeilWith(
            comparator = BoundComparator.compareTo(50),
        )

        assertNull(ceilNode)
    }

    @Test
    fun test_findCeilWith_singleNode_exactMatch() {
        val tree = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = 50,
                color = Node.Color.Black,
            ),
        )

        val ceilNode = tree.findCeilWith(
            comparator = BoundComparator.compareTo(50),
        )

        assertEquals(expected = 50, actual = ceilNode?.payload)
    }

    @Test
    fun test_findCeilWith_singleNode_ceilExists() {
        val tree = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = 50,
                color = Node.Color.Black,
            ),
        )

        val ceilNode = tree.findCeilWith(
            comparator = BoundComparator.compareTo(25),
        )

        assertEquals(expected = 50, actual = ceilNode?.payload)
    }

    @Test
    fun test_findCeilWith_singleNode_noCeil() {
        val tree = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = 50,
                color = Node.Color.Black,
            ),
        )

        val ceilNode = tree.findCeilWith(
            comparator = BoundComparator.compareTo(75),
        )

        assertNull(ceilNode)
    }

    @Test
    fun test_findCeilWith_multiNode_exactMatch() {
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

        val ceilNode = tree.findCeilWith(
            comparator = BoundComparator.compareTo(75),
        )

        assertEquals(expected = 75, actual = ceilNode?.payload)
    }

    @Test
    fun test_findCeilWith_multiNode_ceilExists() {
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

        val ceilNode = tree.findCeilWith(
            comparator = BoundComparator.compareTo(40),
        )

        assertEquals(expected = 50, actual = ceilNode?.payload)
    }

    @Test
    fun test_findCeilWith_multiNode_noCeil() {
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

        val ceilNode = tree.findCeilWith(
            comparator = BoundComparator.compareTo(90),
        )

        assertNull(ceilNode)
    }

    @Test
    fun test_findCeilWith_multiNode_ceilIsMin() {
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

        val ceilNode = tree.findCeilWith(
            comparator = BoundComparator.compareTo(10),
        )

        assertEquals(expected = 25, actual = ceilNode?.payload)
    }

    @Test
    fun test_findCeilWith_multiNode_ceilIsRoot() {
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

        val ceilNode = tree.findCeilWith(
            comparator = BoundComparator.compareTo(40),
        )

        assertEquals(expected = 50, actual = ceilNode?.payload)
    }

    @Test
    fun test_findCeilWith_deepTree_ceilAtVariousDepths() {
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

        // Ceil at depth 2 (leaf level)
        val ceilNode1 = tree.findCeilWith(
            comparator = BoundComparator.compareTo(25),
        )
        assertEquals(expected = 25, actual = ceilNode1?.payload)

        // Ceil at depth 1
        val ceilNode2 = tree.findCeilWith(
            comparator = BoundComparator.compareTo(45),
        )
        assertEquals(expected = 50, actual = ceilNode2?.payload)

        // Ceil at depth 2 (right subtree)
        val ceilNode3 = tree.findCeilWith(
            comparator = BoundComparator.compareTo(65),
        )
        assertEquals(expected = 75, actual = ceilNode3?.payload)

        // Ceil is exact match at leaf
        val ceilNode4 = tree.findCeilWith(
            comparator = BoundComparator.compareTo(10),
        )
        assertEquals(expected = 10, actual = ceilNode4?.payload)

        // No ceil (greater than maximum)
        val ceilNode5 = tree.findCeilWith(
            comparator = BoundComparator.compareTo(95),
        )
        assertNull(ceilNode5)
    }
}


