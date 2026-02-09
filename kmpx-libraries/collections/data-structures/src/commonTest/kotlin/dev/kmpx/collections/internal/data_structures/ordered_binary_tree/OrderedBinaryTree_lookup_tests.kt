package dev.kmpx.collections.internal.data_structures.ordered_binary_tree

import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree.Node.Color.Black
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.OrderedBinaryTree.Node.Color.Red
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup.BoundComparator
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup.findWith
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.lookup.findWithLeaning
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.NodeData
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.getNodeTraversing
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.load
import dev.kmpx.collections.internal.data_structures.ordered_binary_tree.test_utils.verify
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("ClassName")
class OrderedBinaryTree_lookup_tests {
    private data class Entry(
        val key: Int,
        val value: String,
    )

    private val tree: OrderedBinaryTree<Entry> = OrderedBinaryTree.load(
        NodeData(
            payload = Entry(key = 15, value = "foo"),
            color = Black,
            leftChild = NodeData(
                payload = Entry(key = 10, value = "bar"),
                color = Black,
                leftChild = NodeData(
                    payload = Entry(key = 0, value = "foo"),
                    color = Black,
                ),
                rightChild = NodeData(
                    payload = Entry(key = 10, value = "foo"),
                    color = Black,
                ),
            ),
            rightChild = NodeData(
                payload = Entry(key = 20, value = "baz"),
                color = Black,
                leftChild = NodeData(
                    payload = Entry(key = 20, value = "bar"),
                    color = Black,
                ),
                rightChild = NodeData(
                    payload = Entry(key = 30, value = "foo"),
                    color = Red,
                    leftChild = NodeData(
                        payload = Entry(key = 20, value = "foo"),
                        color = Black,
                    ),
                    rightChild = NodeData(
                        payload = Entry(key = 40, value = "foo"),
                        color = Black,
                        leftChild = NodeData(
                            payload = Entry(key = 35, value = "foo"),
                            color = Red,
                        ),
                        rightChild = NodeData(
                            payload = Entry(key = 50, value = "foo"),
                            color = Red,
                        ),
                    ),
                ),
            ),
        ),
    ).apply {
        verify()
    }

    @Test
    fun test_findWith_existing_multipleInstances() {
        val expectedNode = tree.getNodeTraversing(
            payload = Entry(
                key = 20,
                value = "baz",
            ),
        )

        val expectedLocation = expectedNode.locate()

        assertEquals(
            expected = expectedLocation,
            actual = tree.findWith(
                comparator = BoundComparator.compareBy(
                    boundKey = 20,
                    keySelector = Entry::key,
                ),
            ),
        )
    }

    @Test
    fun test_findWith_existing_singleInstance() {
        val expectedNode = tree.getNodeTraversing(
            payload = Entry(
                key = 30,
                value = "foo",
            ),
        )

        val expectedLocation = expectedNode.locate()

        val actualLocation = tree.findWith(
            comparator = BoundComparator.compareBy(
                boundKey = 30,
                keySelector = Entry::key,
            ),
        )

        assertEquals(
            expected = expectedLocation,
            actual = actualLocation,
        )
    }

    @Test
    fun test_findWith_nonExisting() {
        val expectedParentNode = tree.getNodeTraversing(
            payload = Entry(
                key = 10,
                value = "foo",
            ),
        )

        val expectedLocation = OrderedBinaryTree.RelativeLocation(
            parentNode = expectedParentNode,
            side = OrderedBinaryTree.Side.Right,
        )

        assertEquals(
            expected = expectedLocation,
            actual = tree.findWith(
                comparator = BoundComparator.compareBy(
                    boundKey = 11,
                    keySelector = Entry::key,
                ),
            ),
        )
    }

    @Test
    fun test_findWithLeaning_existing_leanLeft() {
        val expectedNode = tree.getNodeTraversing(
            payload = Entry(
                key = 20,
                value = "bar",
            ),
        )

        val expectedLocation = expectedNode.locate()

        assertEquals(
            expected = expectedLocation,
            actual = tree.findWithLeaning(
                comparator = BoundComparator.compareBy(
                    boundKey = 20,
                    keySelector = Entry::key,
                ),
                leanSide = OrderedBinaryTree.Side.Left,
            ),
        )
    }

    @Test
    fun test_findWithLeaning_existing_leanRight() {
        val expectedNode = tree.getNodeTraversing(
            payload = Entry(
                key = 20,
                value = "foo",
            ),
        )

        val expectedLocation = expectedNode.locate()

        assertEquals(
            expected = expectedLocation,
            actual = tree.findWithLeaning(
                comparator = BoundComparator.compareBy(
                    boundKey = 20,
                    keySelector = Entry::key,
                ),
                leanSide = OrderedBinaryTree.Side.Right,
            ),
        )
    }

    @Test
    fun test_findWithLeaning_existing_singleInstance() {
        val expectedNode = tree.getNodeTraversing(
            payload = Entry(
                key = 30,
                value = "foo",
            ),
        )

        val expectedLocation = expectedNode.locate()

        val actualLocation = tree.findWithLeaning(
            comparator = BoundComparator.compareBy(
                boundKey = 30,
                keySelector = Entry::key,
            ),
            leanSide = OrderedBinaryTree.Side.Right,
        )

        assertEquals(
            expected = expectedLocation,
            actual = actualLocation,
        )
    }

    @Test
    fun test_findWithLeaning_nonExisting() {
        val expectedParentNode = tree.getNodeTraversing(
            payload = Entry(
                key = 10,
                value = "foo",
            ),
        )

        val expectedLocation = OrderedBinaryTree.RelativeLocation(
            parentNode = expectedParentNode,
            side = OrderedBinaryTree.Side.Right,
        )

        assertEquals(
            expected = expectedLocation,
            actual = tree.findWithLeaning(
                comparator = BoundComparator.compareBy(
                    boundKey = 11,
                    keySelector = Entry::key,
                ),
                leanSide = OrderedBinaryTree.Side.Right,
            ),
        )
    }
}
