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
class TreeList_tests {
    private enum class Fruit {
        Apple, Raspberry, Banana, Orange, Kiwi, Mango, Pineapple, Strawberry, Watermelon, Grape,
    }

    /**
     * Test initial state of an empty list.
     */
    @Test
    fun test_initial() {
        val treeList = treeListOf<Fruit>()

        StableListTestUtils.verifyIntegrity(treeList)

        assertTrue(
            treeList.isEmpty(),
        )
    }

    /**
     * Test the [List.get] method implementation.
     */
    @Test
    fun test_get() {
        val linkedList = treeListOf(
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
     * Test the [MutableList.set] method implementation.
     */
    @Test
    fun test_set() {
        val treeList = treeListOf(
            Fruit.Banana,
            Fruit.Orange,
            Fruit.Kiwi,
            Fruit.Strawberry,
            Fruit.Pineapple,
        )

        treeList[1] = Fruit.Raspberry

        StableListTestUtils.verifyIntegrity(treeList)

        assertEquals(
            expected = Fruit.Raspberry,
            actual = treeList[1],
        )
    }
    
    /**
     * Test the [MutableList.add] method implementation appending to an empty list.
     */
    @Test
    fun test_add_append_empty() {
        val treeList = treeListOf<Fruit>()

        assertTrue(
            actual = treeList.add(Fruit.Grape),
        )

        StableListTestUtils.verifyIntegrity(treeList)

        assertEquals(
            expected = listOf(
                Fruit.Grape,
            ),
            actual = treeList,
        )
    }

    /**
     * Test the [MutableList.add] method implementation appending to a non-empty list.
     */
    @Test
    fun test_add_append_nonEmpty() {
        val treeList = treeListOf(
            Fruit.Grape,
            Fruit.Strawberry,
        )

        assertTrue(
            actual = treeList.add(Fruit.Orange),
        )

        StableListTestUtils.verifyIntegrity(treeList)

        assertEquals(
            expected = listOf(
                Fruit.Grape,
                Fruit.Strawberry,
                Fruit.Orange,
            ),
            actual = treeList,
        )
    }

    /**
     * Test the [MutableList.add] method implementation appending a duplicate element.
     */
    @Test
    fun test_add_append_duplicate() {
        val treeList = treeListOf(
            Fruit.Grape,
            Fruit.Strawberry,
            Fruit.Apple,
            Fruit.Mango,
        )

        assertTrue(
            actual = treeList.add(Fruit.Apple),
        )

        StableListTestUtils.verifyIntegrity(treeList)

        assertEquals(
            expected = listOf(
                Fruit.Grape,
                Fruit.Strawberry,
                Fruit.Apple,
                Fruit.Mango,
                Fruit.Apple,
            ),
            actual = treeList,
        )
    }

    /**
     * Test the [MutableStableList.addEx] method implementation.
     */
    @Test
    fun test_addEx() {
        val treeList = treeListOf(
            Fruit.Banana,
            Fruit.Orange,
            Fruit.Pineapple,
        )

        val addedElementHandle = treeList.addEx(Fruit.Banana)

        StableListTestUtils.verifyIntegrity(treeList)

        assertEquals(
            expected = listOf(
                Fruit.Banana,
                Fruit.Orange,
                Fruit.Pineapple,
                Fruit.Banana,
            ),
            actual = treeList,
        )

        assertEquals(
            expected = Fruit.Banana,
            actual = treeList.getVia(handle = addedElementHandle),
        )
    }

    /**
     * Test the [MutableList.add] method implementation inserting at index 0.
     */
    @Test
    fun test_add_atIndex_first() {
        val treeList = treeListOf(
            Fruit.Grape,
            Fruit.Strawberry,
            Fruit.Orange,
        )

        treeList.add(
            index = 0,
            element = Fruit.Apple,
        )

        StableListTestUtils.verifyIntegrity(treeList)

        assertEquals(
            expected = listOf(
                Fruit.Apple,
                Fruit.Grape,
                Fruit.Strawberry,
                Fruit.Orange,
            ),
            actual = treeList,
        )
    }

    /**
     * Test the [MutableList.add] method implementation inserting at a middle index.
     */
    @Test
    fun test_add_atIndex_middle() {
        val treeList = treeListOf(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Watermelon,
            Fruit.Orange,
        )

        treeList.add(
            index = 2,
            element = Fruit.Apple,
        )

        StableListTestUtils.verifyIntegrity(treeList)

        assertEquals(
            expected = listOf(
                Fruit.Strawberry,
                Fruit.Grape,
                Fruit.Apple,
                Fruit.Watermelon,
                Fruit.Orange,
            ),
            actual = treeList,
        )
    }

    /**
     * Test the [MutableList.add] method implementation inserting an element equal to an existing one.
     */
    @Test
    fun test_add_atIndex_duplicate() {
        val treeList = treeListOf(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Watermelon,
            Fruit.Orange,
        )

        treeList.add(
            index = 2,
            element = Fruit.Watermelon,
        )

        StableListTestUtils.verifyIntegrity(treeList)

        assertEquals(
            expected = listOf(
                Fruit.Strawberry,
                Fruit.Grape,
                Fruit.Watermelon,
                Fruit.Watermelon,
                Fruit.Orange,
            ),
            actual = treeList,
        )
    }

    /**
     * Test the [MutableList.add] method implementation inserting at the last index.
     */
    @Test
    fun test_add_atIndex_last() {
        val treeList = treeListOf(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Orange,
        )

        treeList.add(
            index = 2,
            element = Fruit.Apple,
        )

        StableListTestUtils.verifyIntegrity(treeList)

        assertEquals(
            expected = listOf(
                Fruit.Strawberry,
                Fruit.Grape,
                Fruit.Apple,
                Fruit.Orange,
            ),
            actual = treeList,
        )
    }

    /**
     * Test the [MutableList.add] method implementation inserting one past the last index.
     */
    @Test
    fun test_add_atIndex_onePastLast() {
        val treeList = treeListOf(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Orange,
        )

        treeList.add(
            index = 3,
            element = Fruit.Apple,
        )

        StableListTestUtils.verifyIntegrity(treeList)

        assertEquals(
            expected = listOf(
                Fruit.Strawberry,
                Fruit.Grape,
                Fruit.Orange,
                Fruit.Apple,
            ),
            actual = treeList,
        )
    }

    /**
     * Test the [MutableStableList.addAtEx] method implementation.
     */
    @Test
    fun test_addAtEx() {
        val treeList = treeListOf(
            Fruit.Grape,
            Fruit.Strawberry,
            Fruit.Orange,
        )

        val addedElementHandle = treeList.addAtEx(
            index = 1,
            element = Fruit.Apple,
        )

        StableListTestUtils.verifyIntegrity(treeList)

        assertEquals(
            expected = listOf(
                Fruit.Grape,
                Fruit.Apple,
                Fruit.Strawberry,
                Fruit.Orange,
            ),
            actual = treeList,
        )

        assertEquals(
            expected = Fruit.Apple,
            actual = treeList.getVia(handle = addedElementHandle),
        )
    }

    /**
     * Test the [MutableList.addAll] method implementation inserting at the last index.
     */
    @Test
    fun test_addAllAt() {
        val treeList = treeListOf(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Orange,
        )

        treeList.addAllAt(
            index = 2,
            elements = listOf(
                Fruit.Pineapple,
                Fruit.Apple,
                Fruit.Raspberry,
            ),
        )

        StableListTestUtils.verifyIntegrity(treeList)

        assertEquals(
            expected = listOf(
                Fruit.Strawberry,
                Fruit.Grape,
                Fruit.Pineapple,
                Fruit.Apple,
                Fruit.Raspberry,
                Fruit.Orange,
            ),
            actual = treeList,
        )
    }

    /**
     * Test the [MutableList.removeAt] method implementation removing from the first index.
     */
    @Test
    fun test_removeAt_first() {
        val treeList = treeListOf(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Orange,
        )

        treeList.removeAt(0)

        StableListTestUtils.verifyIntegrity(treeList)

        assertEquals(
            expected = listOf(
                Fruit.Grape,
                Fruit.Orange,
            ),
            actual = treeList,
        )
    }

    /**
     * Test the [MutableList.removeAt] method implementation removing from a middle index.
     */
    @Test
    fun test_removeAt_middle() {
        val treeList = treeListOf(
            Fruit.Strawberry,
            Fruit.Kiwi,
            Fruit.Orange,
        )

        treeList.removeAt(1)

        StableListTestUtils.verifyIntegrity(treeList)

        assertEquals(
            expected = listOf(
                Fruit.Strawberry,
                Fruit.Orange,
            ),
            actual = treeList,
        )
    }

    /**
     * Test the [MutableList.removeAt] method implementation removing from the last index.
     */
    @Test
    fun test_removeAt_last() {
        val treeList = treeListOf(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Orange,
        )

        treeList.removeAt(2)

        StableListTestUtils.verifyIntegrity(treeList)

        assertEquals(
            expected = listOf(
                Fruit.Strawberry,
                Fruit.Grape,
            ),
            actual = treeList,
        )
    }

    /**
     * Test the [MutableList.removeAt] method implementation removing from an index past the last index.
     */
    @Test
    fun test_removeAt_pastLast() {
        val treeList = treeListOf(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Orange,
        )

        assertIs<IndexOutOfBoundsException>(
            assertFails {
                treeList.removeAt(3)
            },
        )

        StableListTestUtils.verifyIntegrity(treeList)

        assertEquals(
            expected = listOf(
                Fruit.Strawberry,
                Fruit.Grape,
                Fruit.Orange,
            ),
            actual = treeList,
        )
    }

    /**
     * Test the [MutableList.remove] method implementation removing a contained element.
     */
    @Test
    fun test_remove_contained() {
        val treeList = treeListOf(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Orange,
        )

        assertTrue(
            actual = treeList.remove(
                Fruit.Strawberry,
            ),
        )

        StableListTestUtils.verifyIntegrity(treeList)

        assertEquals(
            expected = listOf(
                Fruit.Grape,
                Fruit.Orange,
            ),
            actual = treeList,
        )
    }

    /**
     * Test the [MutableList.remove] method implementation removing a non-contained element.
     */
    @Test
    fun test_remove_nonContained() {
        val treeList = treeListOf(
            Fruit.Strawberry,
            Fruit.Kiwi,
            Fruit.Orange,
        )

        assertFalse(
            actual = treeList.remove(Fruit.Apple),
        )

        StableListTestUtils.verifyIntegrity(treeList)

        assertEquals(
            expected = listOf(
                Fruit.Strawberry,
                Fruit.Kiwi,
                Fruit.Orange,
            ),
            actual = treeList,
        )
    }

    /**
     * Test the [MutableStableList.removeVia] method implementation.
     */
    @Test
    fun test_removeVia() {
        val treeList = treeListOf(
            Fruit.Strawberry,
            Fruit.Kiwi,
            Fruit.Orange,
        )

        val addedElementHandle = treeList.addAtEx(
            index = 2,
            element = Fruit.Mango,
        )

        assertEquals(
            expected = Fruit.Mango,
            actual = treeList.removeVia(
                handle = addedElementHandle,
            ),
        )

        StableListTestUtils.verifyIntegrity(treeList)

        assertNull(
            actual = treeList.getVia(handle = addedElementHandle),
        )

        assertNull(
            actual = treeList.removeVia(handle = addedElementHandle),
        )

        assertNull(
            actual = treeList.indexOfVia(handle = addedElementHandle),
        )

        assertEquals(
            expected = listOf(
                Fruit.Strawberry,
                Fruit.Kiwi,
                Fruit.Orange,
            ),
            actual = treeList,
        )
    }

    /**
     * Test the [MutableStableList.indexOfVia] method implementation.
     */
    @Test
    fun test_indexOfVia() {
        val treeList = treeListOf(
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

        val kiwiHandle = treeList.addAtEx(
            index = 5,
            element = Fruit.Kiwi,
        )

        assertEquals(
            expected = 5,
            actual = treeList.indexOfVia(
                handle = kiwiHandle,
            ),
        )

        val pineappleHandle = treeList.addAtEx(
            index = 3,
            element = Fruit.Pineapple,
        )

        assertEquals(
            expected = 3,
            actual = treeList.indexOfVia(
                handle = pineappleHandle,
            ),
        )

        val mangoHandle = treeList.addAtEx(
            index = 1,
            element = Fruit.Mango,
        )

        assertEquals(
            expected = 1,
            actual = treeList.indexOfVia(
                handle = mangoHandle,
            ),
        )

        assertEquals(
            expected = 4,
            actual = treeList.indexOfVia(
                handle = pineappleHandle,
            ),
        )

        assertEquals(
            expected = 7,
            actual = treeList.indexOfVia(
                handle = kiwiHandle,
            ),
        )
    }

    /**
     * Test the [StableList.findEx] method implementation.
     */
    @Test
    fun test_findEx() {
        val treeList = treeListOf(
            Fruit.Strawberry,
            Fruit.Kiwi,
            Fruit.Orange,
            Fruit.Kiwi,
        )

        val handle = assertNotNull(
            treeList.findEx(Fruit.Kiwi),
        )

        assertEquals(
            expected = Fruit.Kiwi,
            actual = treeList.getVia(handle = handle),
        )

        assertEquals(
            expected = 1,
            actual = treeList.indexOfVia(handle = handle),
        )
    }
}
