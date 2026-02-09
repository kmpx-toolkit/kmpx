package dev.kmpx.collections.maps

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@Suppress("ClassName")
class TreeMap_tests {
    @Test
    fun test_initial() {
        val map = treeMapOf<Int, String>()

        assertEquals(
            expected = 0,
            actual = map.size,
        )

        assertNull(
            actual = map[10],
        )

        map.verifyContent(
            entries = emptyList(),
            controlKeys = setOf(10, 20, 30),
        )
    }

    @Test
    fun test_put_empty() {
        val map = treeMapOf<Int, String>()

        assertNull(
            actual = map.put(
                key = 10,
                value = "a",
            ),
        )

        assertEquals(
            expected = "a",
            actual = map[10],
        )

        assertEquals(
            expected = 1,
            actual = map.size,
        )

        map.verifyContent(
            entries = listOf(10 to "a"),
            controlKeys = setOf(20, 30),
        )
    }

    @Test
    fun test_put_overwrite() {
        val map = treeMapOf(
            10 to "a",
            20 to "b",
        )

        assertEquals(
            expected = "a",
            actual = map.put(
                key = 10,
                value = "z",
            ),
        )

        assertEquals(
            expected = "z",
            actual = map[10],
        )

        assertEquals(
            expected = "b",
            actual = map[20],
        )

        assertEquals(
            expected = 2,
            actual = map.size,
        )

        map.verifyContent(
            entries = listOf(10 to "z", 20 to "b"),
            controlKeys = setOf(30, 40),
        )
    }

    @Test
    fun test_put_new() {
        val map = treeMapOf(
            10 to "a",
            20 to "b",
        )

        assertNull(
            actual = map.put(
                key = 15,
                value = "c",
            ),
        )

        assertEquals(
            expected = "c",
            actual = map[15],
        )

        assertEquals(
            expected = 3,
            actual = map.size,
        )

        map.verifyContent(
            entries = listOf(10 to "a", 15 to "c", 20 to "b"),
            controlKeys = setOf(30, 40),
        )
    }

    @Test
    fun test_insertEntry_duplicate() {
        val map = treeMapOf(
            10 to "a",
            20 to "b",
        )

        assertNull(
            actual = map.insertEntry(
                key = 10,
                value = "x",
            ),
        )

        assertEquals(
            expected = "a",
            actual = map[10],
        )

        assertEquals(
            expected = 2,
            actual = map.size,
        )

        map.verifyContent(
            entries = listOf(10 to "a", 20 to "b"),
            controlKeys = setOf(30, 40),
        )
    }

    @Test
    fun test_insertEntry_nonDuplicate() {
        val map = treeMapOf(
            10 to "a",
            20 to "b",
        )

        val entryHandle = assertNotNull(
            actual = map.insertEntry(
                key = 15,
                value = "c",
            ),
        )

        assertEquals(
            expected = "c",
            actual = map.getValueVia(
                entryHandle = entryHandle,
            ),
        )

        assertEquals(
            expected = 3,
            actual = map.size,
        )

        map.verifyContent(
            entries = listOf(10 to "a", 15 to "c", 20 to "b"),
            controlKeys = setOf(30, 40),
        )
    }

    @Test
    fun test_setValueVia() {
        val map = treeMapOf(
            10 to "a",
            20 to "b1",
            30 to "c",
        )

        val entryHandle = map.resolve(
            key = 20,
        )!!

        assertEquals(
            expected = "b1",
            actual = map.setValueVia(
                entryHandle = entryHandle,
                newValue = "b2",
            ),
        )

        map.verifyContent(
            entries = listOf(
                10 to "a",
                20 to "b2",
                30 to "c",
            ),
            controlKeys = setOf(15, 25),
        )
    }

    @Test
    fun test_remove() {
        val map = treeMapOf(
            10 to "a",
            20 to "b",
            30 to "c",
        )

        assertEquals(
            expected = "b",
            actual = map.remove(20),
        )

        assertNull(
            actual = map[20],
        )

        assertEquals(
            expected = 2,
            actual = map.size,
        )

        map.verifyContent(
            entries = listOf(10 to "a", 30 to "c"),
            controlKeys = setOf(20, 40, 50),
        )

        assertEquals(
            expected = "a",
            actual = map.remove(10),
        )

        map.verifyContent(
            entries = listOf(30 to "c"),
            controlKeys = setOf(10, 20, 40, 50),
        )

        assertEquals(
            expected = "c",
            actual = map.remove(30),
        )

        assertEquals(
            expected = 0,
            actual = map.size,
        )

        map.verifyContent(
            entries = emptyList(),
            controlKeys = setOf(10, 20, 30, 40, 50),
        )
    }

    @Test
    fun test_remove_notContained() {
        val map = treeMapOf(
            10 to "a",
            20 to "b",
        )

        assertNull(
            actual = map.remove(99),
        )

        assertEquals(
            expected = 2,
            actual = map.size,
        )

        map.verifyContent(
            entries = listOf(10 to "a", 20 to "b"),
            controlKeys = setOf(99, 30, 40),
        )
    }

    @Test
    fun test_removeEntryVia() {
        val map = treeMapOf(
            10 to "a",
            20 to "b",
            30 to "c",
        )

        val entryHandle = assertNotNull(
            actual = map.resolve(key = 20),
        )

        val removedValue = assertNotNull(
            actual = map.removeEntryVia(entryHandle = entryHandle),
        )

        assertEquals(
            expected = "b",
            actual = removedValue,
        )

        assertNull(
            actual = map.getValueVia(entryHandle = entryHandle),
        )

        assertNull(
            actual = map.removeEntryVia(entryHandle = entryHandle),
        )

        map.verifyContent(
            entries = listOf(10 to "a", 30 to "c"),
            controlKeys = setOf(20, 40, 50),
        )
    }

    @Test
    fun test_getValueVia() {
        val map = treeMapOf(
            10 to "a",
            20 to "b",
        )

        val entryHandle = assertNotNull(
            actual = map.resolve(key = 10),
        )

        assertEquals(
            expected = "a",
            actual = map.getValueVia(entryHandle = entryHandle),
        )

        map.verifyContent(
            entries = listOf(10 to "a", 20 to "b"),
            controlKeys = setOf(30, 40),
        )
    }

    /**
     * Test that [SortedMap.floorEntry] returns the correct entry when the key is exactly present in the map (should
     * return that entry, not a different one).
     */
    @Test
    fun test_floorEntry_exactKeyPresent() {
        val map = treeMapOf(
            10 to "a",
            20 to "b",
            30 to "c",
        )

        val entry = assertNotNull(
            actual = map.floorEntry(20),
        )

        assertEquals(
            expected = 20,
            actual = entry.key,
        )

        assertEquals(
            expected = "b",
            actual = entry.value,
        )
    }

    /**
     * Test that [SortedMap.floorEntry] returns the correct entry when the key is not present in the map, but there are
     * keys lower than it (should return the entry with the greatest key that is still lower than the given key).
     */
    @Test
    fun test_floorEntry_lowerKeyPresent() {
        val map = treeMapOf(
            10 to "a",
            20 to "b",
            30 to "c",
        )

        val entry = assertNotNull(
            actual = map.floorEntry(25),
        )

        assertEquals(
            expected = 20,
            actual = entry.key,
        )

        assertEquals(
            expected = "b",
            actual = entry.value,
        )
    }

    /**
     * Test that [SortedMap.floorEntry] returns null when the key is not present in the map and there are no keys lower
     * than it (should return null since there is no floor entry).
     */
    @Test
    fun test_floorEntry_noFloorKeyPresent() {
        val map = treeMapOf(
            10 to "a",
            20 to "b",
            30 to "c",
        )

        assertNull(
            actual = map.floorEntry(5),
        )
    }

    /**
     * Test that [SortedMap.ceilingEntry] returns the correct entry when the key is exactly present in the map (should
     * return that entry, not a different one).
     */
    @Test
    fun test_ceilingEntry_exactKeyPresent() {
        val map = treeMapOf(
            10 to "a",
            20 to "b",
            30 to "c",
        )

        val entry = assertNotNull(
            actual = map.ceilingEntry(20),
        )

        assertEquals(
            expected = 20,
            actual = entry.key,
        )

        assertEquals(
            expected = "b",
            actual = entry.value,
        )
    }

    /**
     * Test that [SortedMap.ceilingEntry] returns the correct entry when the key is not present in the map, but there
     * are keys higher than it (should return the entry with the smallest key that is still higher than the given key).
     */
    @Test
    fun test_ceilingEntry_higherKeyPresent() {
        val map = treeMapOf(
            10 to "a",
            20 to "b",
            30 to "c",
        )

        val entry = assertNotNull(
            actual = map.ceilingEntry(15),
        )

        assertEquals(
            expected = 20,
            actual = entry.key,
        )

        assertEquals(
            expected = "b",
            actual = entry.value,
        )
    }

    /**
     * Test that [SortedMap.ceilingEntry] returns null when the key is not present in the map and there are no keys
     * higher than it (should return null since there is no ceiling entry).
     */
    @Test
    fun test_ceilingEntry_noCeilingKeyPresent() {
        val map = treeMapOf(
            10 to "a",
            20 to "b",
            30 to "c",
        )

        assertNull(
            actual = map.ceilingEntry(35),
        )
    }
}
