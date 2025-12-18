package dev.kmpx.collections.internal.data_structures.ordered_binary_tree

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree.Node
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.NodeData
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.RedBlackTreeTestUtils
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.getNodeTraversing
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("ClassName")
class OrderedBinaryTree_balancedMutationUtils_tests {
    @Test
    fun testInsertAll() {
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
                    color = Node.Color.Black,
                ),
            ),
        )

        val node500 = tree.getNodeTraversing(payload = 500.0)

        tree.insertAll(
            location = node500.getRightChildLocation(),
            payloads = listOf(600.0, 700.0, 800.0),
        )

        assertEquals(
            expected = listOf(
                500.0,
                600.0,
                700.0,
                800.0,
                1000.0,
                2000.0,
            ),
            actual = tree.traverse().map {
                it.payload
            }.toList(),
        )
    }
}
