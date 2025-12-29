package dev.kmpx.collections.internal.data_structures.ordered_binary_tree

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree.Node
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup.findBy
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.NodeData
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.getNodeTraversing
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.load
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.verifyOrderBy
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("ClassName")
class OrderedBinaryTree_indirectNaturalLookup_tests {
    @Test
    fun test_findBy() {
        fun <K, V> selectKey(pair: Pair<K, V>): K = pair.first

        val tree: OrderedBinaryTree<Pair<Int, String>> = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = 100 to "A",
                color = Node.Color.Red,
                leftChild = NodeData(
                    payload = 50 to "B",
                    color = Node.Color.Black,
                    leftChild = NodeData(
                        payload = 25 to "C",
                        color = Node.Color.Black,
                    ),
                    rightChild = NodeData(
                        payload = 75 to "D",
                        color = Node.Color.Black,
                    ),
                ),
                rightChild = NodeData(
                    payload = 150 to "E",
                    color = Node.Color.Black,
                    leftChild = NodeData(
                        payload = 125 to "F",
                        color = Node.Color.Black,
                        leftChild = NodeData(
                            payload = 115 to "G",
                            color = Node.Color.Black,
                            leftChild = NodeData(
                                payload = 110 to "H",
                                color = Node.Color.Black,
                            ),
                        ),
                    ),
                    rightChild = NodeData(
                        payload = 175 to "I",
                        color = Node.Color.Black,
                    ),
                ),
            ),
        )

        tree.verifyOrderBy(selector = ::selectKey)

        val node115 = tree.getNodeTraversing(payload = 115 to "G")
        val location115 = node115.locate()

        assertEquals(
            expected = location115,
            actual = tree.findBy(
                key = 115,
                selector = ::selectKey,
            ),
        )

        val node175 = tree.getNodeTraversing(payload = 175 to "I")
        val location180 = node175.getRightChildLocation()

        assertEquals(
            expected = location180,
            actual = tree.findBy(
                key = 180,
                selector = ::selectKey,
            ),
        )
    }
}
