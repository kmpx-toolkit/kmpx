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

    @Test
    fun test_initial() {
        val treeList = TreeList<Fruit>()

        treeList.verifyContent()
    }

    @Test
    fun test_set() {
        val mutableTreeList = mutableTreeListOf(
            Fruit.Banana,
            Fruit.Orange,
            Fruit.Kiwi,
            Fruit.Strawberry,
            Fruit.Pineapple,
        )

        mutableTreeList[1] = Fruit.Raspberry

        mutableTreeList.verifyContent(
            Fruit.Banana,
            Fruit.Raspberry,
            Fruit.Kiwi,
            Fruit.Strawberry,
            Fruit.Pineapple,
        )
    }

    @Test
    fun test_addEx() {
        val mutableTreeList = mutableTreeListOf(
            Fruit.Banana,
            Fruit.Orange,
            Fruit.Pineapple,
        )

        val bananaHandle = mutableTreeList.addEx(Fruit.Banana)

        mutableTreeList.verifyContent(
            Fruit.Banana,
            Fruit.Orange,
            Fruit.Pineapple,
            Fruit.Banana,
        )

        assertEquals(
            expected = Fruit.Banana,
            actual = mutableTreeList.getVia(handle = bananaHandle),
        )
    }

    @Test
    fun test_set_duplicate() {
        val mutableTreeList = mutableTreeListOf(
            Fruit.Banana,
            Fruit.Orange,
            Fruit.Kiwi,
            Fruit.Strawberry,
            Fruit.Watermelon,
        )

        mutableTreeList[3] = Fruit.Orange

        mutableTreeList.verifyContent(
            Fruit.Banana,
            Fruit.Orange,
            Fruit.Kiwi,
            Fruit.Orange,
            Fruit.Watermelon,
        )
    }

    @Test
    fun test_add_append_empty() {
        val treeList = TreeList<Fruit>()

        assertTrue(
            actual = treeList.add(
                Fruit.Grape,
            )
        )

        treeList.verifyContent(
            Fruit.Grape,
        )
    }

    @Test
    fun test_add_append_nonEmpty() {
        val mutableTreeList = mutableTreeListOf(
            Fruit.Grape,
            Fruit.Strawberry,
        )

        assertTrue(
            actual = mutableTreeList.add(
                Fruit.Orange,
            ),
        )

        mutableTreeList.verifyContent(
            Fruit.Grape,
            Fruit.Strawberry,
            Fruit.Orange,
        )
    }

    @Test
    fun test_add_append_duplicate() {
        val mutableTreeList = mutableTreeListOf(
            Fruit.Grape,
            Fruit.Strawberry,
            Fruit.Apple,
            Fruit.Mango,
        )

        assertTrue(
            actual = mutableTreeList.add(
                Fruit.Apple,
            ),
        )

        mutableTreeList.verifyContent(
            Fruit.Grape,
            Fruit.Strawberry,
            Fruit.Apple,
            Fruit.Mango,
            Fruit.Apple,
        )
    }

    @Test
    fun test_add_atIndex_first() {
        val mutableTreeList = mutableTreeListOf(
            Fruit.Grape,
            Fruit.Strawberry,
            Fruit.Orange,
        )

        mutableTreeList.add(
            index = 0,
            element = Fruit.Apple,
        )

        mutableTreeList.verifyContent(
            Fruit.Apple,
            Fruit.Grape,
            Fruit.Strawberry,
            Fruit.Orange,
        )
    }

    @Test
    fun test_addEx_atIndex() {
        val mutableTreeList = mutableTreeListOf(
            Fruit.Grape,
            Fruit.Strawberry,
            Fruit.Orange,
        )

        val appleHandle = mutableTreeList.addAtEx(
            index = 1,
            element = Fruit.Apple,
        )

        mutableTreeList.verifyContent(
            Fruit.Grape,
            Fruit.Apple,
            Fruit.Strawberry,
            Fruit.Orange,
        )

        assertEquals(
            expected = Fruit.Apple,
            actual = mutableTreeList.getVia(handle = appleHandle),
        )
    }

    @Test
    fun test_add_atIndex_middle() {
        val mutableTreeList = mutableTreeListOf(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Watermelon,
            Fruit.Orange,
        )

        mutableTreeList.add(
            index = 2,
            element = Fruit.Apple,
        )

        mutableTreeList.verifyContent(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Apple,
            Fruit.Watermelon,
            Fruit.Orange,
        )
    }

    @Test
    fun test_add_atIndex_duplicate() {
        val mutableTreeList = mutableTreeListOf(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Watermelon,
            Fruit.Orange,
        )

        mutableTreeList.add(
            index = 2,
            element = Fruit.Watermelon,
        )

        mutableTreeList.verifyContent(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Watermelon,
            Fruit.Watermelon,
            Fruit.Orange,
        )
    }

    @Test
    fun test_add_atIndex_last() {
        val mutableTreeList = mutableTreeListOf(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Orange,
        )

        mutableTreeList.add(
            index = 2,
            element = Fruit.Apple,
        )

        mutableTreeList.verifyContent(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Apple,
            Fruit.Orange,
        )
    }

    @Test
    fun test_add_atIndex_onePastLast() {
        val mutableTreeList = mutableTreeListOf(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Orange,
        )

        mutableTreeList.add(
            index = 3,
            element = Fruit.Apple,
        )

        mutableTreeList.verifyContent(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Orange,
            Fruit.Apple,
        )
    }

    @Test
    fun test_addAllAt() {
        val mutableTreeList = mutableTreeListOf(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Orange,
        )

        mutableTreeList.addAllAt(
            index = 2,
            elements = listOf(
                Fruit.Pineapple,
                Fruit.Apple,
                Fruit.Raspberry,
            ),
        )

        mutableTreeList.verifyContent(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Pineapple,
            Fruit.Apple,
            Fruit.Raspberry,
            Fruit.Orange,
        )
    }

    @Test
    fun test_removeAt_first() {
        val mutableTreeList = mutableTreeListOf(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Orange,
        )

        mutableTreeList.removeAt(0)

        mutableTreeList.verifyContent(
            Fruit.Grape,
            Fruit.Orange,
        )
    }

    @Test
    fun test_removeAt_middle() {
        val mutableTreeList = mutableTreeListOf(
            Fruit.Strawberry,
            Fruit.Kiwi,
            Fruit.Orange,
        )

        mutableTreeList.removeAt(1)

        mutableTreeList.verifyContent(
            Fruit.Strawberry,
            Fruit.Orange,
        )
    }

    @Test
    fun test_removeAt_last() {
        val mutableTreeList = mutableTreeListOf(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Orange,
        )

        mutableTreeList.removeAt(2)

        mutableTreeList.verifyContent(
            Fruit.Strawberry,
            Fruit.Grape,
        )
    }

    @Test
    fun test_removeAt_pastLast() {
        val mutableTreeList = mutableTreeListOf(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Orange,
        )

        assertIs<IndexOutOfBoundsException>(
            assertFails {
                mutableTreeList.removeAt(3)
            },
        )
    }

    @Test
    fun test_remove_contained() {
        val mutableTreeList = mutableTreeListOf(
            Fruit.Strawberry,
            Fruit.Grape,
            Fruit.Orange,
        )

        assertTrue(
            actual = mutableTreeList.remove(
                Fruit.Strawberry,
            ),
        )

        mutableTreeList.verifyContent(
            Fruit.Grape,
            Fruit.Orange,
        )
    }

    @Test
    fun test_remove_nonContained() {
        val mutableTreeList = mutableTreeListOf(
            Fruit.Strawberry,
            Fruit.Kiwi,
            Fruit.Orange,
        )

        assertFalse(
            actual = mutableTreeList.remove(
                Fruit.Apple,
            ),
        )

        mutableTreeList.verifyContent(
            Fruit.Strawberry,
            Fruit.Kiwi,
            Fruit.Orange,
        )
    }

    @Test
    fun test_removeVia() {
        val mutableTreeList = mutableTreeListOf(
            Fruit.Strawberry,
            Fruit.Kiwi,
            Fruit.Orange,
        )

        val mangoHandle = mutableTreeList.addAtEx(
            index = 2,
            element = Fruit.Mango,
        )

        assertEquals(
            expected = Fruit.Mango,
            actual = mutableTreeList.removeVia(
                handle = mangoHandle,
            ),
        )

        assertNull(
            actual = mutableTreeList.getVia(handle = mangoHandle),
        )

        assertNull(
            actual = mutableTreeList.removeVia(handle = mangoHandle),
        )

        assertNull(
            actual = mutableTreeList.indexOfVia(handle = mangoHandle),
        )

        mutableTreeList.verifyContent(
            Fruit.Strawberry,
            Fruit.Kiwi,
            Fruit.Orange,
        )
    }

    @Test
    fun test_indexOfVia() {
        val mutableTreeList = mutableTreeListOf(
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

        val kiwiHandle = mutableTreeList.addAtEx(
            index = 5,
            element = Fruit.Kiwi,
        )

        assertEquals(
            expected = 5,
            actual = mutableTreeList.indexOfVia(
                handle = kiwiHandle,
            ),
        )

        val pineappleHandle = mutableTreeList.addAtEx(
            index = 3,
            element = Fruit.Pineapple,
        )

        assertEquals(
            expected = 3,
            actual = mutableTreeList.indexOfVia(
                handle = pineappleHandle,
            ),
        )

        val mangoHandle = mutableTreeList.addAtEx(
            index = 1,
            element = Fruit.Mango,
        )

        assertEquals(
            expected = 1,
            actual = mutableTreeList.indexOfVia(
                handle = mangoHandle,
            ),
        )

        assertEquals(
            expected = 4,
            actual = mutableTreeList.indexOfVia(
                handle = pineappleHandle,
            ),
        )

        assertEquals(
            expected = 7,
            actual = mutableTreeList.indexOfVia(
                handle = kiwiHandle,
            ),
        )
    }

    @Test
    fun test_findEx() {
        val mutableTreeList = mutableTreeListOf(
            Fruit.Strawberry,
            Fruit.Kiwi,
            Fruit.Orange,
            Fruit.Kiwi,
        )

        val handle = assertNotNull(
            mutableTreeList.findEx(Fruit.Kiwi),
        )

        assertEquals(
            expected = Fruit.Kiwi,
            actual = mutableTreeList.getVia(handle = handle),
        )

        assertEquals(
            expected = 1,
            actual = mutableTreeList.indexOfVia(handle = handle),
        )
    }
}
