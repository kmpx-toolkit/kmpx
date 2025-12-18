package dev.kmpx.collections.internal.data_structures.ordered_binary_tree

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree.Node
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup.find
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.NodeData
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.getNodeTraversing
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.load
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.verifyOrder
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("ClassName")
class OrderedBinaryTree_directNaturalLookup_tests {
    @Test
    fun testFind() {
        val tree: OrderedBinaryTree<Int> = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = 100,
                color = Node.Color.Red,
                leftChild = NodeData(
                    payload = 50,
                    color = Node.Color.Black,
                    leftChild = NodeData(
                        payload = 25,
                        color = Node.Color.Black,
                    ),
                    rightChild = NodeData(
                        payload = 75,
                        color = Node.Color.Black,
                    ),
                ),
                rightChild = NodeData(
                    payload = 150,
                    color = Node.Color.Black,
                    leftChild = NodeData(
                        payload = 125,
                        color = Node.Color.Red,
                        leftChild = NodeData(
                            payload = 115,
                            color = Node.Color.Red,
                            leftChild = NodeData(
                                payload = 110,
                                color = Node.Color.Red,
                            ),
                        ),
                    ),
                    rightChild = NodeData(
                        payload = 175,
                        color = Node.Color.Black,
                    ),
                ),
            ),
        )

        tree.verifyOrder()

        val node115 = tree.getNodeTraversing(payload = 115)
        val location115 = node115.locate()

        assertEquals(
            expected = location115, actual = tree.find(115)
        )

        val node175 = tree.getNodeTraversing(payload = 175)
        val location180 = node175.getRightChildLocation()

        assertEquals(
            expected = location180,
            actual = tree.find(180),
        )
    }
}
