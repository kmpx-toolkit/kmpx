package dev.kmpx.collections.lists

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("ClassName")
class LinkedList_tests {
    private enum class Fruit {
        Apple, Raspberry, Banana, Orange, Kiwi, Mango, Pineapple, Strawberry, Watermelon, Grape,
    }

    /**
     * Test initial state of an empty list.
     */
    @Test
    fun test_initial() {
        val linkedList = linkedListOf<Fruit>()

        StableListTestUtils.verifyIntegrity(linkedList)

        assertTrue(
            linkedList.isEmpty(),
        )
    }

    /**
     * Test the [List.get] method implementation.
     */
    @Test
    fun test_get() {
        val linkedList = linkedListOf(
            Fruit.Banana,
            Fruit.Orange,
            Fruit.Kiwi,
            Fruit.Strawberry,
            Fruit.Pineapple,
        )

        assertEquals(
            expected = Fruit.Banana,
            actual = linkedList[0],
        )

        assertEquals(
            expected = Fruit.Kiwi,
            actual = linkedList[2],
        )

        assertEquals(
            expected = Fruit.Pineapple,
            actual = linkedList[4],
        )
    }

    /**
     * Test the [StableList.resolveAt] method implementation (within bounds).
     */
    @Test
    fun test_resolveAt_withinBounds() {
        val linkedList = linkedListOf(
            Fruit.Banana,
            Fruit.Orange,
            Fruit.Kiwi,
        )

        val handle = assertNotNull(
            actual = linkedList.resolveAt(1),
        )

        assertEquals(
            expected = Fruit.Orange,
            actual = linkedList.getVia(handle),
        )
    }

    /**
     * Test the [StableList.resolveAt] method implementation (outside bounds).
     */
    @Test
    fun test_resolveAt_outsideBounds() {
        val linkedList = linkedListOf(
            Fruit.Banana,
            Fruit.Orange,
            Fruit.Kiwi,
        )

        assertNull(
            actual = linkedList.resolveAt(3),
        )
    }

    /**
     * Test the [MutableList.set] method implementation.
     */
    @Test
    fun test_set() {
        val linkedList = linkedListOf(
            Fruit.Banana,
            Fruit.Orange,
            Fruit.Kiwi,
            Fruit.Strawberry,
            Fruit.Pineapple,
        )

        linkedList[1] = Fruit.Raspberry

        StableListTestUtils.verifyIntegrity(linkedList)

        assertEquals(
            expected = Fruit.Raspberry,
            actual = linkedList[1],
        )
    }

    /**
     * Test the [MutableList.add] method implementation appending to an empty list.
     */
    @Test
    fun test_add_append_empty() {
        val linkedList = linkedListOf<Fruit>()

        assertTrue(
            actual = linkedList.add(Fruit.Grape),
        )

        StableListTestUtils.verifyIntegrity(linkedList)

        assertEquals(
            expected = listOf(
                Fruit.Grape,
            ),
            actual = linkedList,
        )
    }

    /**
     * Test the [MutableList.add] method implementation appending to a non-empty list.
     */
    @Test
    fun test_add_append_nonEmpty() {
        val linkedList = linkedListOf(
            Fruit.Grape,
            Fruit.Strawberry,
        )

        assertTrue(
            actual = linkedList.add(Fruit.Orange),
        )

        StableListTestUtils.verifyIntegrity(linkedList)

        assertEquals(
            expected = listOf(
                Fruit.Grape,
                Fruit.Strawberry,
                Fruit.Orange,
            ),
            actual = linkedList,
        )
    }

    /**
     * Test the [MutableList.add] method implementation appending a duplicate element.
     */
    @Test
    fun test_add_append_duplicate() {
        val linkedList = linkedListOf(
            Fruit.Grape,
            Fruit.Strawberry,
            Fruit.Apple,
            Fruit.Mango,
        )

        assertTrue(
            actual = linkedList.add(Fruit.Apple),
        )

        StableListTestUtils.verifyIntegrity(linkedList)

        assertEquals(
            expected = listOf(
                Fruit.Grape,
                Fruit.Strawberry,
                Fruit.Apple,
                Fruit.Mango,
                Fruit.Apple,
            ),
            actual = linkedList,
        )
    }

    /**
     * Test the [MutableStableList.addEx] method implementation.
     */
    @Test
    fun test_addEx() {
        val linkedList = linkedListOf(
            Fruit.Banana,
            Fruit.Orange,
            Fruit.Pineapple,
        )

        val addedElementHandle = linkedList.addEx(Fruit.Banana)

        StableListTestUtils.verifyIntegrity(linkedList)

        assertEquals(
            expected = listOf(
                Fruit.Banana,
                Fruit.Orange,
                Fruit.Pineapple,
                Fruit.Banana,
            ),
            actual = linkedList,
        )

        assertEquals(
            expected = Fruit.Banana,
            actual = linkedList.getVia(handle = addedElementHandle),
        )
    }

    /**
     * Test the [MutableList.add] method implementation inserting at index 0.
     */
    @Test
    fun test_add_atIndex_first() {
        val linkedList = linkedListOf(
            Fruit.Grape,
            Fruit.Strawberry,
            Fruit.Orange,
        )

        linkedList.add(
            index = 0,
            element = Fruit.Apple,
        )

        StableListTestUtils.verifyIntegrity(linkedList)

        assertEquals(
            expected = listOf(
                Fruit.Apple,
                Fruit.Grape,
                Fruit.Strawberry,
                Fruit.Orange,
            ),
            actual = linkedList,
        )
    }

    /**
     * Test the [MutableList.add] method implementation inserting at a middle index.
     */
    @Test
    fun test_add_atIndex_middle() {
        val linkedList = linkedListOf(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Watermelon,
            Fruit.Orange,
        )

        linkedList.add(
            index = 2,
            element = Fruit.Apple,
        )

        StableListTestUtils.verifyIntegrity(linkedList)

        assertEquals(
            expected = listOf(
                Fruit.Strawberry,
                Fruit.Grape,
                Fruit.Apple,
                Fruit.Watermelon,
                Fruit.Orange,
            ),
            actual = linkedList,
        )
    }

    /**
     * Test the [MutableList.add] method implementation inserting an element equal to an existing one.
     */
    @Test
    fun test_add_atIndex_duplicate() {
        val linkedList = linkedListOf(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Watermelon,
            Fruit.Orange,
        )

        linkedList.add(
            index = 2,
            element = Fruit.Watermelon,
        )

        StableListTestUtils.verifyIntegrity(linkedList)

        assertEquals(
            expected = listOf(
                Fruit.Strawberry,
                Fruit.Grape,
                Fruit.Watermelon,
                Fruit.Watermelon,
                Fruit.Orange,
            ),
            actual = linkedList,
        )
    }

    /**
     * Test the [MutableList.add] method implementation inserting at the last index.
     */
    @Test
    fun test_add_atIndex_last() {
        val linkedList = linkedListOf(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Orange,
        )

        linkedList.add(
            index = 2,
            element = Fruit.Apple,
        )

        StableListTestUtils.verifyIntegrity(linkedList)

        assertEquals(
            expected = listOf(
                Fruit.Strawberry,
                Fruit.Grape,
                Fruit.Apple,
                Fruit.Orange,
            ),
            actual = linkedList,
        )
    }

    /**
     * Test the [MutableList.add] method implementation inserting one past the last index.
     */
    @Test
    fun test_add_atIndex_onePastLast() {
        val linkedList = linkedListOf(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Orange,
        )

        linkedList.add(
            index = 3,
            element = Fruit.Apple,
        )

        StableListTestUtils.verifyIntegrity(linkedList)

        assertEquals(
            expected = listOf(
                Fruit.Strawberry,
                Fruit.Grape,
                Fruit.Orange,
                Fruit.Apple,
            ),
            actual = linkedList,
        )
    }

    /**
     * Test the [MutableStableList.addAtEx] method implementation (adding a single element).
     */
    @Test
    fun test_addAtEx_single() {
        val linkedList = linkedListOf(
            Fruit.Grape,
            Fruit.Strawberry,
            Fruit.Orange,
        )

        val addedElementHandle = linkedList.addAtEx(
            index = 1,
            element = Fruit.Apple,
        )

        StableListTestUtils.verifyIntegrity(linkedList)

        assertEquals(
            expected = listOf(
                Fruit.Grape,
                Fruit.Apple,
                Fruit.Strawberry,
                Fruit.Orange,
            ),
            actual = linkedList,
        )

        assertEquals(
            expected = Fruit.Apple,
            actual = linkedList.getVia(handle = addedElementHandle),
        )
    }

    /**
     * Test the [MutableStableList.addAtEx] method implementation (adding multiple elements).
     */
    @Test
    fun test_addAtEx_multiple() {
        val linkedList = linkedListOf(
            Fruit.Strawberry,
            // +Mango
            Fruit.Kiwi,
            Fruit.Orange,
            // +Pineapple
            Fruit.Grape,
            Fruit.Raspberry,
            // +Kiwi
            Fruit.Orange,
        )

        linkedList.addAtEx(
            index = 5,
            element = Fruit.Kiwi,
        )

        StableListTestUtils.verifyIntegrity(linkedList)

        linkedList.addAtEx(
            index = 3,
            element = Fruit.Pineapple,
        )

        StableListTestUtils.verifyIntegrity(linkedList)

        linkedList.addAtEx(
            index = 1,
            element = Fruit.Mango,
        )
        StableListTestUtils.verifyIntegrity(linkedList)

        assertEquals(
            expected = listOf(
                Fruit.Strawberry,
                Fruit.Mango,
                Fruit.Kiwi,
                Fruit.Orange,
                Fruit.Pineapple,
                Fruit.Grape,
                Fruit.Raspberry,
                Fruit.Kiwi,
                Fruit.Orange,
            ),
            actual = linkedList,
        )
    }

    /**
     * Test the [MutableList.addAll] method implementation inserting at the last index.
     */
    @Test
    fun test_addAllAt() {
        val linkedList = linkedListOf(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Orange,
        )

        linkedList.addAllAt(
            index = 2,
            elements = listOf(
                Fruit.Pineapple,
                Fruit.Apple,
                Fruit.Raspberry,
            ),
        )

        StableListTestUtils.verifyIntegrity(linkedList)

        assertEquals(
            expected = listOf(
                Fruit.Strawberry,
                Fruit.Grape,
                Fruit.Pineapple,
                Fruit.Apple,
                Fruit.Raspberry,
                Fruit.Orange,
            ),
            actual = linkedList,
        )
    }

    /**
     * Test the [MutableList.removeAt] method implementation removing from the first index.
     */
    @Test
    fun test_removeAt_first() {
        val linkedList = linkedListOf(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Orange,
        )

        linkedList.removeAt(0)

        StableListTestUtils.verifyIntegrity(linkedList)

        assertEquals(
            expected = listOf(
                Fruit.Grape,
                Fruit.Orange,
            ),
            actual = linkedList,
        )
    }

    /**
     * Test the [MutableList.removeAt] method implementation removing from a middle index.
     */
    @Test
    fun test_removeAt_middle() {
        val linkedList = linkedListOf(
            Fruit.Strawberry,
            Fruit.Kiwi,
            Fruit.Orange,
        )

        linkedList.removeAt(1)

        StableListTestUtils.verifyIntegrity(linkedList)

        assertEquals(
            expected = listOf(
                Fruit.Strawberry,
                Fruit.Orange,
            ),
            actual = linkedList,
        )
    }

    /**
     * Test the [MutableList.removeAt] method implementation removing from the last index.
     */
    @Test
    fun test_removeAt_last() {
        val linkedList = linkedListOf(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Orange,
        )

        linkedList.removeAt(2)

        StableListTestUtils.verifyIntegrity(linkedList)

        assertEquals(
            expected = listOf(
                Fruit.Strawberry,
                Fruit.Grape,
            ),
            actual = linkedList,
        )
    }

    /**
     * Test the [MutableList.removeAt] method implementation removing from an index past the last index.
     */
    @Test
    fun test_removeAt_pastLast() {
        val linkedList = linkedListOf(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Orange,
        )

        assertIs<IndexOutOfBoundsException>(
            assertFails {
                linkedList.removeAt(3)
            },
        )

        StableListTestUtils.verifyIntegrity(linkedList)

        assertEquals(
            expected = listOf(
                Fruit.Strawberry,
                Fruit.Grape,
                Fruit.Orange,
            ),
            actual = linkedList,
        )
    }

    /**
     * Test the [MutableList.remove] method implementation removing a contained element.
     */
    @Test
    fun test_remove_contained() {
        val linkedList = linkedListOf(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Orange,
        )

        assertTrue(
            actual = linkedList.remove(
                Fruit.Strawberry,
            ),
        )

        StableListTestUtils.verifyIntegrity(linkedList)

        assertEquals(
            expected = listOf(
                Fruit.Grape,
                Fruit.Orange,
            ),
            actual = linkedList,
        )
    }

    /**
     * Test the [MutableList.remove] method implementation removing a non-contained element.
     */
    @Test
    fun test_remove_nonContained() {
        val linkedList = linkedListOf(
            Fruit.Strawberry,
            Fruit.Kiwi,
            Fruit.Orange,
        )

        assertFalse(
            actual = linkedList.remove(Fruit.Apple),
        )

        StableListTestUtils.verifyIntegrity(linkedList)

        assertEquals(
            expected = listOf(
                Fruit.Strawberry,
                Fruit.Kiwi,
                Fruit.Orange,
            ),
            actual = linkedList,
        )
    }

    /**
     * Test the [MutableStableList.removeVia] method implementation.
     */
    @Test
    fun test_removeVia() {
        val linkedList = linkedListOf(
            Fruit.Strawberry,
            Fruit.Kiwi,
            Fruit.Orange,
        )

        val addedElementHandle = linkedList.addAtEx(
            index = 2,
            element = Fruit.Mango,
        )

        assertEquals(
            expected = Fruit.Mango,
            actual = linkedList.removeVia(
                handle = addedElementHandle,
            ),
        )

        StableListTestUtils.verifyIntegrity(linkedList)

        assertEquals(
            expected = listOf(
                Fruit.Strawberry,
                Fruit.Kiwi,
                Fruit.Orange,
            ),
            actual = linkedList,
        )
    }

    /**
     * Test the [MutableStableList.indexOfVia] method implementation.
     */
    @Test
    fun test_indexOfVia() {
        val linkedList = linkedListOf(
            Fruit.Strawberry,
            Fruit.Kiwi,
            Fruit.Orange,
            Fruit.Grape,
        )

        val elementHandle = linkedList.findEx(Fruit.Orange)!!

        assertEquals(
            expected = 2,
            actual = linkedList.indexOfVia(
                handle = elementHandle,
            ),
        )
    }

    /**
     * Test the [StableList.findEx] method implementation.
     */
    @Test
    fun test_findEx() {
        val linkedList = linkedListOf(
            Fruit.Strawberry,
            Fruit.Kiwi,
            Fruit.Orange,
            Fruit.Kiwi,
        )

        val handle = assertNotNull(
            linkedList.findEx(Fruit.Kiwi),
        )

        assertEquals(
            expected = Fruit.Kiwi,
            actual = linkedList.getVia(handle = handle),
        )

        assertEquals(
            expected = 1,
            actual = linkedList.indexOfVia(handle = handle),
        )
    }
}
