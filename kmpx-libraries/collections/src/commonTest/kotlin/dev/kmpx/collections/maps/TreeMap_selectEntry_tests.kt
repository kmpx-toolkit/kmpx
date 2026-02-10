package dev.kmpx.collections.maps

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@Suppress("ClassName")
class TreeMap_selectEntry_tests {
    @Test
    fun test_selectEntry_emptyMap() {
        val map = treeMapOf<Int, String>()

        assertNull(
            actual = map.selectEntry(entryRank = 0),
        )
    }

    @Test
    fun test_selectEntry_emptyMap_negativeRank() {
        val map = treeMapOf<Int, String>()

        assertNull(
            actual = map.selectEntry(entryRank = -1),
        )
    }

    @Test
    fun test_selectEntry_singleEntry() {
        val map = treeMapOf(20 to "b")

        val entry = map.selectEntry(entryRank = 0)

        assertNotNull(entry)
        assertEquals(
            expected = 20,
            actual = entry.key,
        )
        assertEquals(
            expected = "b",
            actual = entry.value,
        )
    }

    @Test
    fun test_selectEntry_singleEntry_outOfBounds() {
        val map = treeMapOf(20 to "b")

        assertNull(
            actual = map.selectEntry(entryRank = 1),
        )
    }

    @Test
    fun test_selectEntry_singleEntry_negativeRank() {
        val map = treeMapOf(20 to "b")

        assertNull(
            actual = map.selectEntry(entryRank = -1),
        )
    }

    @Test
    fun test_selectEntry_multipleEntries_firstRank() {
        val map = treeMapOf(10 to "a", 20 to "b", 30 to "c", 40 to "d", 50 to "e")

        val entry = map.selectEntry(entryRank = 0)

        assertNotNull(entry)
        assertEquals(
            expected = 10,
            actual = entry.key,
        )
        assertEquals(
            expected = "a",
            actual = entry.value,
        )
    }

    @Test
    fun test_selectEntry_multipleEntries_middleRanks() {
        val map = treeMapOf(10 to "a", 20 to "b", 30 to "c", 40 to "d", 50 to "e")

        val entry1 = map.selectEntry(entryRank = 1)
        assertNotNull(entry1)
        assertEquals(expected = 20, actual = entry1.key)
        assertEquals(expected = "b", actual = entry1.value)

        val entry2 = map.selectEntry(entryRank = 2)
        assertNotNull(entry2)
        assertEquals(expected = 30, actual = entry2.key)
        assertEquals(expected = "c", actual = entry2.value)

        val entry3 = map.selectEntry(entryRank = 3)
        assertNotNull(entry3)
        assertEquals(expected = 40, actual = entry3.key)
        assertEquals(expected = "d", actual = entry3.value)
    }

    @Test
    fun test_selectEntry_multipleEntries_lastRank() {
        val map = treeMapOf(10 to "a", 20 to "b", 30 to "c", 40 to "d", 50 to "e")

        val entry = map.selectEntry(entryRank = 4)

        assertNotNull(entry)
        assertEquals(
            expected = 50,
            actual = entry.key,
        )
        assertEquals(
            expected = "e",
            actual = entry.value,
        )
    }

    @Test
    fun test_selectEntry_multipleEntries_allRanks() {
        val map = treeMapOf(10 to "a", 20 to "b", 30 to "c", 40 to "d", 50 to "e")

        // Test all valid ranks in order
        val entry0 = map.selectEntry(entryRank = 0)
        assertNotNull(entry0)
        assertEquals(expected = 10, actual = entry0.key)
        assertEquals(expected = "a", actual = entry0.value)

        val entry1 = map.selectEntry(entryRank = 1)
        assertNotNull(entry1)
        assertEquals(expected = 20, actual = entry1.key)
        assertEquals(expected = "b", actual = entry1.value)

        val entry2 = map.selectEntry(entryRank = 2)
        assertNotNull(entry2)
        assertEquals(expected = 30, actual = entry2.key)
        assertEquals(expected = "c", actual = entry2.value)

        val entry3 = map.selectEntry(entryRank = 3)
        assertNotNull(entry3)
        assertEquals(expected = 40, actual = entry3.key)
        assertEquals(expected = "d", actual = entry3.value)

        val entry4 = map.selectEntry(entryRank = 4)
        assertNotNull(entry4)
        assertEquals(expected = 50, actual = entry4.key)
        assertEquals(expected = "e", actual = entry4.value)
    }

    @Test
    fun test_selectEntry_outOfBounds_positive() {
        val map = treeMapOf(10 to "a", 20 to "b", 30 to "c", 40 to "d", 50 to "e")

        assertNull(
            actual = map.selectEntry(entryRank = 5),
        )

        assertNull(
            actual = map.selectEntry(entryRank = 100),
        )
    }

    @Test
    fun test_selectEntry_outOfBounds_negative() {
        val map = treeMapOf(10 to "a", 20 to "b", 30 to "c", 40 to "d", 50 to "e")

        assertNull(
            actual = map.selectEntry(entryRank = -1),
        )

        assertNull(
            actual = map.selectEntry(entryRank = -100),
        )
    }

    @Test
    fun test_selectEntry_twoEntries() {
        val map = treeMapOf(10 to "a", 20 to "b")

        val entry0 = map.selectEntry(entryRank = 0)
        assertNotNull(entry0)
        assertEquals(expected = 10, actual = entry0.key)
        assertEquals(expected = "a", actual = entry0.value)

        val entry1 = map.selectEntry(entryRank = 1)
        assertNotNull(entry1)
        assertEquals(expected = 20, actual = entry1.key)
        assertEquals(expected = "b", actual = entry1.value)

        assertNull(
            actual = map.selectEntry(entryRank = 2),
        )

        assertNull(
            actual = map.selectEntry(entryRank = -1),
        )
    }

    @Test
    fun test_selectEntry_afterRemoval() {
        val map = treeMapOf(10 to "a", 20 to "b", 30 to "c", 40 to "d", 50 to "e")

        // Remove middle entry
        map.remove(key = 30)

        // Verify ranks shift correctly
        val entry0 = map.selectEntry(entryRank = 0)
        assertNotNull(entry0)
        assertEquals(expected = 10, actual = entry0.key)
        assertEquals(expected = "a", actual = entry0.value)

        val entry1 = map.selectEntry(entryRank = 1)
        assertNotNull(entry1)
        assertEquals(expected = 20, actual = entry1.key)
        assertEquals(expected = "b", actual = entry1.value)

        val entry2 = map.selectEntry(entryRank = 2)
        assertNotNull(entry2)
        assertEquals(expected = 40, actual = entry2.key)
        assertEquals(expected = "d", actual = entry2.value)

        val entry3 = map.selectEntry(entryRank = 3)
        assertNotNull(entry3)
        assertEquals(expected = 50, actual = entry3.key)
        assertEquals(expected = "e", actual = entry3.value)

        assertNull(
            actual = map.selectEntry(entryRank = 4),
        )
    }

    @Test
    fun test_selectEntry_afterInsertion() {
        val map = treeMapOf(10 to "a", 20 to "b", 40 to "d", 50 to "e")

        // Insert entry in the middle
        map[30] = "c"

        // Verify all ranks including the new entry
        val entry0 = map.selectEntry(entryRank = 0)
        assertNotNull(entry0)
        assertEquals(expected = 10, actual = entry0.key)
        assertEquals(expected = "a", actual = entry0.value)

        val entry1 = map.selectEntry(entryRank = 1)
        assertNotNull(entry1)
        assertEquals(expected = 20, actual = entry1.key)
        assertEquals(expected = "b", actual = entry1.value)

        val entry2 = map.selectEntry(entryRank = 2)
        assertNotNull(entry2)
        assertEquals(expected = 30, actual = entry2.key)
        assertEquals(expected = "c", actual = entry2.value)

        val entry3 = map.selectEntry(entryRank = 3)
        assertNotNull(entry3)
        assertEquals(expected = 40, actual = entry3.key)
        assertEquals(expected = "d", actual = entry3.value)

        val entry4 = map.selectEntry(entryRank = 4)
        assertNotNull(entry4)
        assertEquals(expected = 50, actual = entry4.key)
        assertEquals(expected = "e", actual = entry4.value)

        assertNull(
            actual = map.selectEntry(entryRank = 5),
        )
    }

    @Test
    fun test_selectEntry_afterValueUpdate() {
        val map = treeMapOf(10 to "a", 20 to "b", 30 to "c")

        // Update value for key 20
        map[20] = "updated"

        // Verify rank positions stay the same but value changes
        val entry0 = map.selectEntry(entryRank = 0)
        assertNotNull(entry0)
        assertEquals(expected = 10, actual = entry0.key)
        assertEquals(expected = "a", actual = entry0.value)

        val entry1 = map.selectEntry(entryRank = 1)
        assertNotNull(entry1)
        assertEquals(expected = 20, actual = entry1.key)
        assertEquals(expected = "updated", actual = entry1.value)

        val entry2 = map.selectEntry(entryRank = 2)
        assertNotNull(entry2)
        assertEquals(expected = 30, actual = entry2.key)
        assertEquals(expected = "c", actual = entry2.value)
    }

    @Test
    fun test_selectEntry_unsortedInsertionOrder() {
        val map = treeMapOf<Int, String>()

        // Insert in non-sorted order
        map[30] = "c"
        map[10] = "a"
        map[50] = "e"
        map[20] = "b"
        map[40] = "d"

        // Verify entries are selected in sorted order by key
        val entry0 = map.selectEntry(entryRank = 0)
        assertNotNull(entry0)
        assertEquals(expected = 10, actual = entry0.key)

        val entry1 = map.selectEntry(entryRank = 1)
        assertNotNull(entry1)
        assertEquals(expected = 20, actual = entry1.key)

        val entry2 = map.selectEntry(entryRank = 2)
        assertNotNull(entry2)
        assertEquals(expected = 30, actual = entry2.key)

        val entry3 = map.selectEntry(entryRank = 3)
        assertNotNull(entry3)
        assertEquals(expected = 40, actual = entry3.key)

        val entry4 = map.selectEntry(entryRank = 4)
        assertNotNull(entry4)
        assertEquals(expected = 50, actual = entry4.key)
    }

    @Test
    fun test_selectEntry_largeMap() {
        // Test with a larger map to verify correctness with more complex tree structure
        val entries = listOf(
            5 to "e5", 15 to "e15", 25 to "e25", 35 to "e35", 45 to "e45",
            55 to "e55", 65 to "e65", 75 to "e75", 85 to "e85", 95 to "e95"
        )

        val map = treeMapOf(*entries.toTypedArray())

        // Verify all entries can be selected by rank
        entries.forEachIndexed { index, (key, value) ->
            val entry = map.selectEntry(entryRank = index)
            assertNotNull(
                actual = entry,
                message = "Failed to select entry at rank $index",
            )
            assertEquals(
                expected = key,
                actual = entry.key,
                message = "Wrong key at rank $index",
            )
            assertEquals(
                expected = value,
                actual = entry.value,
                message = "Wrong value at rank $index",
            )
        }

        // Verify out of bounds
        assertNull(
            actual = map.selectEntry(entryRank = entries.size),
        )
    }

    @Test
    fun test_selectEntry_multipleRemovals() {
        val map = treeMapOf(10 to "a", 20 to "b", 30 to "c", 40 to "d", 50 to "e")

        // Remove multiple entries
        map.remove(key = 20)
        map.remove(key = 40)

        // Verify remaining entries
        assertEquals(expected = 3, actual = map.size)

        val entry0 = map.selectEntry(entryRank = 0)
        assertNotNull(entry0)
        assertEquals(expected = 10, actual = entry0.key)

        val entry1 = map.selectEntry(entryRank = 1)
        assertNotNull(entry1)
        assertEquals(expected = 30, actual = entry1.key)

        val entry2 = map.selectEntry(entryRank = 2)
        assertNotNull(entry2)
        assertEquals(expected = 50, actual = entry2.key)

        assertNull(
            actual = map.selectEntry(entryRank = 3),
        )
    }
}
