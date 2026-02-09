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
class OrderedBinaryTree_leaningLookup_tests {
    private data class KeyedPayload(
        val key: Int,
        val value: String,
    )

    @Test
    fun test_findLeaningWith_emptyTree() {
        val tree = OrderedBinaryTree.create<KeyedPayload>()

        val location = tree.findLeaningWith(
            comparator = BoundComparator.compareBy(50) { it.key },
            leanSide = Side.Left,
        )

        assertIs<RootLocation>(location)
    }

    @Test
    fun test_findLeaningWith_singleNode_found() {
        val tree = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = KeyedPayload(50, "A"),
                color = Node.Color.Black,
            ),
        )

        val location = tree.findLeaningWith(
            comparator = BoundComparator.compareBy(50) { it.key },
            leanSide = Side.Left,
        )

        assertIs<RootLocation>(location)
    }

    @Test
    fun test_findLeaningWith_singleNode_notFound() {
        val tree = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = KeyedPayload(50, "A"),
                color = Node.Color.Black,
            ),
        )

        val location = tree.findLeaningWith(
            comparator = BoundComparator.compareBy(25) { it.key },
            leanSide = Side.Left,
        )

        assertIs<RelativeLocation<KeyedPayload>>(location)
        assertEquals(expected = tree.rootNode, actual = location.parentNode)
        assertEquals(expected = Side.Left, actual = location.side)
        assertNull(location.occupyingNode)
    }

    @Test
    fun test_findLeaningWith_multiNode_found_noDuplicates() {
        val tree = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = KeyedPayload(50, "A"),
                color = Node.Color.Black,
                leftChild = NodeData(
                    payload = KeyedPayload(25, "B"),
                    color = Node.Color.Red,
                ),
                rightChild = NodeData(
                    payload = KeyedPayload(75, "C"),
                    color = Node.Color.Red,
                ),
            ),
        )

        val location = tree.findLeaningWith(
            comparator = BoundComparator.compareBy(25) { it.key },
            leanSide = Side.Left,
        )

        assertIs<RelativeLocation<KeyedPayload>>(location)
        assertEquals(expected = KeyedPayload(25, "B"), actual = location.occupyingNode?.payload)
    }

    @Test
    fun test_findLeaningWith_multiNode_duplicates_leanLeft() {
        // Tree structure:
        //         50:A
        //        /    \
        //     50:B    75:C
        //     /
        //   50:D
        val tree = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = KeyedPayload(50, "A"),
                color = Node.Color.Black,
                leftChild = NodeData(
                    payload = KeyedPayload(50, "B"),
                    color = Node.Color.Red,
                    leftChild = NodeData(
                        payload = KeyedPayload(50, "D"),
                        color = Node.Color.Black,
                    ),
                ),
                rightChild = NodeData(
                    payload = KeyedPayload(75, "C"),
                    color = Node.Color.Red,
                ),
            ),
        )

        val location = tree.findLeaningWith(
            comparator = BoundComparator.compareBy(50) { it.key },
            leanSide = Side.Left,
        )

        assertIs<RelativeLocation<KeyedPayload>>(location)
        assertEquals(expected = KeyedPayload(50, "D"), actual = location.occupyingNode?.payload)
    }

    @Test
    fun test_findLeaningWith_multiNode_duplicates_leanRight() {
        // Tree structure:
        //         50:A
        //        /    \
        //     25:B    50:C
        //                \
        //               50:D
        val tree = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = KeyedPayload(50, "A"),
                color = Node.Color.Black,
                leftChild = NodeData(
                    payload = KeyedPayload(25, "B"),
                    color = Node.Color.Red,
                ),
                rightChild = NodeData(
                    payload = KeyedPayload(50, "C"),
                    color = Node.Color.Red,
                    rightChild = NodeData(
                        payload = KeyedPayload(50, "D"),
                        color = Node.Color.Black,
                    ),
                ),
            ),
        )

        val location = tree.findLeaningWith(
            comparator = BoundComparator.compareBy(50) { it.key },
            leanSide = Side.Right,
        )

        assertIs<RelativeLocation<KeyedPayload>>(location)
        assertEquals(expected = KeyedPayload(50, "D"), actual = location.occupyingNode?.payload)
    }

    @Test
    fun test_findLeaningWith_multiNode_notFound_leanLeft() {
        val tree = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = KeyedPayload(50, "A"),
                color = Node.Color.Black,
                leftChild = NodeData(
                    payload = KeyedPayload(25, "B"),
                    color = Node.Color.Red,
                ),
                rightChild = NodeData(
                    payload = KeyedPayload(75, "C"),
                    color = Node.Color.Red,
                ),
            ),
        )

        val location = tree.findLeaningWith(
            comparator = BoundComparator.compareBy(60) { it.key },
            leanSide = Side.Left,
        )

        assertIs<RelativeLocation<KeyedPayload>>(location)
        assertEquals(expected = KeyedPayload(75, "C"), actual = location.parentNode.payload)
        assertEquals(expected = Side.Left, actual = location.side)
        assertNull(location.occupyingNode)
    }

    @Test
    fun test_findLeaningWith_multiNode_notFound_leanRight() {
        val tree = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = KeyedPayload(50, "A"),
                color = Node.Color.Black,
                leftChild = NodeData(
                    payload = KeyedPayload(25, "B"),
                    color = Node.Color.Red,
                ),
                rightChild = NodeData(
                    payload = KeyedPayload(75, "C"),
                    color = Node.Color.Red,
                ),
            ),
        )

        val location = tree.findLeaningWith(
            comparator = BoundComparator.compareBy(60) { it.key },
            leanSide = Side.Right,
        )

        assertIs<RelativeLocation<KeyedPayload>>(location)
        assertEquals(expected = KeyedPayload(75, "C"), actual = location.parentNode.payload)
        assertEquals(expected = Side.Left, actual = location.side)
        assertNull(location.occupyingNode)
    }

    @Test
    fun test_findLeaningWith_allDuplicates_leanLeft() {
        // Tree structure:
        //         50:A
        //        /    \
        //     50:B    50:C
        val tree = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = KeyedPayload(50, "A"),
                color = Node.Color.Black,
                leftChild = NodeData(
                    payload = KeyedPayload(50, "B"),
                    color = Node.Color.Red,
                ),
                rightChild = NodeData(
                    payload = KeyedPayload(50, "C"),
                    color = Node.Color.Red,
                ),
            ),
        )

        val location = tree.findLeaningWith(
            comparator = BoundComparator.compareBy(50) { it.key },
            leanSide = Side.Left,
        )

        assertIs<RelativeLocation<KeyedPayload>>(location)
        assertEquals(expected = KeyedPayload(50, "B"), actual = location.occupyingNode?.payload)
    }

    @Test
    fun test_findLeaningWith_allDuplicates_leanRight() {
        // Tree structure:
        //         50:A
        //        /    \
        //     50:B    50:C
        val tree = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = KeyedPayload(50, "A"),
                color = Node.Color.Black,
                leftChild = NodeData(
                    payload = KeyedPayload(50, "B"),
                    color = Node.Color.Red,
                ),
                rightChild = NodeData(
                    payload = KeyedPayload(50, "C"),
                    color = Node.Color.Red,
                ),
            ),
        )

        val location = tree.findLeaningWith(
            comparator = BoundComparator.compareBy(50) { it.key },
            leanSide = Side.Right,
        )

        assertIs<RelativeLocation<KeyedPayload>>(location)
        assertEquals(expected = KeyedPayload(50, "C"), actual = location.occupyingNode?.payload)
    }

    @Test
    fun test_findLeaningWith_deepTree_duplicates() {
        // Tree structure:
        //              50:A
        //            /      \
        //         50:B      50:C
        //        /    \         \
        //     50:D   50:E      50:F
        //     /
        //   50:G
        val tree = OrderedBinaryTree.load(
            rootData = NodeData(
                payload = KeyedPayload(50, "A"),
                color = Node.Color.Black,
                leftChild = NodeData(
                    payload = KeyedPayload(50, "B"),
                    color = Node.Color.Red,
                    leftChild = NodeData(
                        payload = KeyedPayload(50, "D"),
                        color = Node.Color.Black,
                        leftChild = NodeData(
                            payload = KeyedPayload(50, "G"),
                            color = Node.Color.Red,
                        ),
                    ),
                    rightChild = NodeData(
                        payload = KeyedPayload(50, "E"),
                        color = Node.Color.Black,
                    ),
                ),
                rightChild = NodeData(
                    payload = KeyedPayload(50, "C"),
                    color = Node.Color.Red,
                    rightChild = NodeData(
                        payload = KeyedPayload(50, "F"),
                        color = Node.Color.Black,
                    ),
                ),
            ),
        )

        val locationLeft = tree.findLeaningWith(
            comparator = BoundComparator.compareBy(50) { it.key },
            leanSide = Side.Left,
        )

        assertIs<RelativeLocation<KeyedPayload>>(locationLeft)
        assertEquals(expected = KeyedPayload(50, "G"), actual = locationLeft.occupyingNode?.payload)

        val locationRight = tree.findLeaningWith(
            comparator = BoundComparator.compareBy(50) { it.key },
            leanSide = Side.Right,
        )

        assertIs<RelativeLocation<KeyedPayload>>(locationRight)
        assertEquals(expected = KeyedPayload(50, "F"), actual = locationRight.occupyingNode?.payload)
    }
}

