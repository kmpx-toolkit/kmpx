package dev.kmpx.collections.lists

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("ClassName")
class TreeSortedList_tests {
    private class Entry(
        val uniqueTag: Any? = null,
        val key: Int,
        val value: String,
    ) {
        companion object {
            val keyComparator: Comparator<Entry> = compareBy { it.key }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as Entry

            if (key != other.key) return false
            if (value != other.value) return false

            return true
        }

        override fun hashCode(): Int {
            var result = key
            result = 31 * result + value.hashCode()
            return result
        }
    }

    @Test
    fun test_initial() {
        val treeList = TreeList<Entry>()

        treeList.verifyContent(
            expectedElements = emptyList(),
        )
    }

    @Test
    fun test_get_noDuplicatesPresent() {
        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
            Entry(key = 40, value = "a"),
            Entry(key = 0, value = "b"),
            Entry(key = 30, value = "x"),
            Entry(key = 20, value = "z"),
            Entry(key = 10, value = "a"),
        )

        assertEquals(
            expected = Entry(key = 10, value = "a"),
            actual = treeSortedList[1],
        )
    }

    @Test
    fun test_get_orderWiseDuplicatesPresent() {
        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
            Entry(key = 40, value = "a"),
            Entry(key = 10, value = "a"),
            Entry(key = 0, value = "z"),
            Entry(key = 30, value = "w"),
            Entry(key = 20, value = "a"),
            Entry(key = 10, value = "b"),
            Entry(key = 50, value = "a"),
            Entry(key = 10, value = "c"),
        )

        assertEquals(
            expected = Entry(key = 10, value = "b"),
            actual = treeSortedList[2],
        )
    }

    @Test
    fun test_resolveAt_withinBounds() {
        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
            Entry(key = 40, value = "a"),
            Entry(key = 0, value = "b"),
            Entry(key = 30, value = "x"),
            Entry(key = 20, value = "z"),
            Entry(key = 10, value = "a"),
        )

        val resolvedHandle = assertNotNull(
            actual = treeSortedList.resolveAt(2),
        )

        val resolvedEntry = treeSortedList.getVia(resolvedHandle)

        assertEquals(
            expected = Entry(key = 20, value = "z"),
            actual = resolvedEntry,
        )
    }

    @Test
    fun test_resolveAt_outOfBounds() {
        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
            Entry(key = 40, value = "a"),
            Entry(key = 0, value = "b"),
            Entry(key = 30, value = "x"),
            Entry(key = 20, value = "z"),
            Entry(key = 10, value = "a"),
        )

        assertNull(
            actual = treeSortedList.resolveAt(-1),
        )

        assertNull(
            actual = treeSortedList.resolveAt(5),
        )
    }

    @Test
    fun test_getVia() {
        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
        )

        treeSortedList.addAll(
            listOf(
                Entry(key = 40, value = "a"),
                Entry(key = 0, value = "b"),
                Entry(key = 50, value = "a"),
                Entry(key = 10, value = "a"),
            ),
        )

        val insertedEntry = Entry(key = 30, value = "z")

        val entryHandle = treeSortedList.insert(insertedEntry)

        treeSortedList.addAll(
            listOf(
                Entry(key = 60, value = "w"),
                Entry(key = 20, value = "z"),
                Entry(key = 70, value = "a"),
            ),
        )

        assertEquals(
            expected = insertedEntry,
            actual = treeSortedList.getVia(entryHandle),
        )
    }

    @Test
    fun test_indexOf_contained_noDuplicatesPresent() {
        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
            Entry(
                key = 40,
                value = "a",
            ),
            Entry(
                key = 30,
                value = "a",
            ),
            Entry(
                key = 0,
                value = "z",
            ),
            Entry(
                key = 10,
                value = "a",
            ),
            Entry(
                key = 50,
                value = "a",
            ),
            Entry(
                key = 20,
                value = "z",
            ),
            Entry(
                key = 60,
                value = "c",
            ),
        )

        assertEquals(
            expected = 3, actual = treeSortedList.indexOf(
                Entry(
                    key = 30,
                    value = "a",
                ),
            )
        )
    }

    @Test
    fun test_indexOf_contained_orderWiseDuplicatesPresent() {
        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
            Entry(
                key = 40,
                value = "a",
            ),
            Entry(
                key = 30,
                value = "a",
            ),
            Entry(
                key = 0,
                value = "z",
            ),
            Entry(
                key = 40,
                value = "b",
            ),
            Entry(
                key = 10,
                value = "a",
            ),
            Entry(
                key = 50,
                value = "a",
            ),
            Entry(
                key = 40,
                value = "c",
            ),
            Entry(
                key = 20,
                value = "z",
            ),
            Entry(
                key = 60,
                value = "c",
            ),
        )

        assertEquals(
            expected = 5, actual = treeSortedList.indexOf(
                Entry(
                    key = 40,
                    value = "b",
                ),
            )
        )
    }

    @Test
    fun test_indexOf_contained_literalDuplicatesPresent() {
        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
            Entry(
                key = 30,
                value = "a",
            ),
            Entry(
                key = 20,
                value = "a",
            ),
            Entry(
                key = 30,
                value = "a",
            ),
            Entry(
                key = 0,
                value = "z",
            ),
            Entry(
                key = 10,
                value = "b",
            ),
            Entry(
                key = 30,
                value = "a",
            ),
        )

        assertEquals(
            expected = 3,
            actual = treeSortedList.indexOf(
                Entry(
                    key = 30,
                    value = "a",
                ),
            ),
        )
    }

    @Test
    fun test_indexOf_nonContained_noDuplicatesPresent() {
        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
            Entry(
                key = 40,
                value = "a",
            ),
            Entry(
                key = 30,
                value = "a",
            ),
            Entry(
                key = 10,
                value = "z",
            ),
            Entry(
                key = 20,
                value = "b",
            ),
        )

        assertEquals(
            expected = -1, actual = treeSortedList.indexOf(
                Entry(
                    key = 11,
                    value = "??",
                ),
            )
        )
    }

    @Test
    fun test_indexOf_nonContained_orderWiseDuplicatesPresent() {
        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
            Entry(
                key = 40,
                value = "a",
            ),
            Entry(
                key = 10,
                value = "a",
            ),
            Entry(
                key = 30,
                value = "a",
            ),
            Entry(
                key = 10,
                value = "c",
            ),
            Entry(
                key = 20,
                value = "b",
            ),
        )

        assertEquals(
            expected = -1, actual = treeSortedList.indexOf(
                Entry(
                    key = 10,
                    value = "b",
                ),
            )
        )
    }

    @Test
    fun test_indexOfVia() {
        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
        )

        treeSortedList.addAll(
            listOf(
                Entry(key = 40, value = "a"),
                Entry(key = 0, value = "b"),
                Entry(key = 50, value = "a"),
                Entry(key = 10, value = "a"),
            ),
        )

        val insertedEntry = Entry(key = 30, value = "z")

        val entryHandle = treeSortedList.insert(insertedEntry)

        treeSortedList.addAll(
            listOf(
                Entry(key = 60, value = "w"),
                Entry(key = 20, value = "z"),
                Entry(key = 70, value = "a"),
            ),
        )

        assertEquals(
            expected = 3,
            actual = treeSortedList.indexOfVia(entryHandle),
        )
    }

    @Test
    fun test_resolveFirst_contained_noDuplicatesPresent() {
        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
        )

        treeSortedList.addAll(
            listOf(
                Entry(
                    key = 40,
                    value = "a",
                ),
                Entry(
                    key = 0,
                    value = "z",
                ),
                Entry(
                    key = 10,
                    value = "a",
                ),
                Entry(
                    key = 50,
                    value = "a",
                ),
            )
        )

        val entry = Entry(
            key = 30,
            value = "a",
        )

        val insertedEntryHandle = treeSortedList.insert(entry)

        treeSortedList.addAll(
            listOf(
                Entry(
                    key = 20,
                    value = "a",
                ),
                Entry(
                    key = 50,
                    value = "z",
                ),
                Entry(
                    key = 60,
                    value = "a",
                ),
            ),
        )

        val resolvedEntryHandle = assertNotNull(
            treeSortedList.resolveFirst(entry),
        )

        assertEquals(
            expected = insertedEntryHandle,
            actual = resolvedEntryHandle,
        )
    }

    @Test
    fun test_resolveFirst_contained_orderWiseDuplicatesPresent() {
        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
        )

        treeSortedList.addAll(
            listOf(
                Entry(
                    key = 40,
                    value = "a",
                ),
                Entry(
                    key = 0,
                    value = "z",
                ),
                Entry(
                    key = 30,
                    value = "b",
                ),
                Entry(
                    key = 10,
                    value = "a",
                ),
                Entry(
                    key = 50,
                    value = "a",
                ),
            )
        )

        val entry = Entry(
            key = 30,
            value = "a",
        )

        val insertedEntryHandle = treeSortedList.insert(entry)

        treeSortedList.addAll(
            listOf(
                Entry(
                    key = 20,
                    value = "a",
                ),
                Entry(
                    key = 30,
                    value = "c",
                ),
                Entry(
                    key = 50,
                    value = "z",
                ),
                Entry(
                    key = 60,
                    value = "a",
                ),
            ),
        )

        val resolvedEntryHandle = assertNotNull(
            treeSortedList.resolveFirst(entry),
        )

        assertEquals(
            expected = insertedEntryHandle,
            actual = resolvedEntryHandle,
        )
    }

    @Test
    fun test_resolveFirst_contained_literalDuplicatesPresent() {
        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
        )

        treeSortedList.addAll(
            listOf(
                Entry(
                    key = 40,
                    value = "a",
                ),
                Entry(
                    key = 0,
                    value = "z",
                ),
                Entry(
                    key = 30,
                    value = "b",
                ),
                Entry(
                    key = 10,
                    value = "a",
                ),
                Entry(
                    key = 50,
                    value = "a",
                ),
            )
        )

        val entry = Entry(
            key = 30,
            value = "a",
        )

        val insertedEntryHandle = treeSortedList.insert(entry)

        treeSortedList.addAll(
            listOf(
                Entry(
                    key = 20,
                    value = "a",
                ),
                entry,
                Entry(
                    key = 30,
                    value = "c",
                ),
                entry,
                Entry(
                    key = 50,
                    value = "z",
                ),
                Entry(
                    key = 60,
                    value = "a",
                ),
            ),
        )

        val resolvedEntryHandle = assertNotNull(
            treeSortedList.resolveFirst(entry),
        )

        assertEquals(
            expected = insertedEntryHandle,
            actual = resolvedEntryHandle,
        )
    }

    @Test
    fun test_resolveFirst_nonContained_noDuplicatesPresent() {
        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
            Entry(
                key = 40,
                value = "a",
            ),
            Entry(
                key = 30,
                value = "a",
            ),
            Entry(
                key = 10,
                value = "z",
            ),
            Entry(
                key = 20,
                value = "b",
            ),
        )

        assertNull(
            actual = treeSortedList.resolveFirst(
                Entry(
                    key = 11,
                    value = "??",
                ),
            ),
        )
    }

    @Test
    fun test_resolveFirst_nonContained_orderWiseDuplicatesPresent() {
        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
            Entry(
                key = 40,
                value = "a",
            ),
            Entry(
                key = 10,
                value = "a",
            ),
            Entry(
                key = 30,
                value = "a",
            ),
            Entry(
                key = 10,
                value = "c",
            ),
            Entry(
                key = 20,
                value = "b",
            ),
        )

        assertNull(
            actual = treeSortedList.resolveFirst(
                Entry(
                    key = 10,
                    value = "b",
                ),
            ),
        )
    }

    @Test
    fun test_predictIndexOf_empty() {
        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
        )

        val newEntry = Entry(key = 10, value = "a")

        assertEquals(
            expected = 0,
            actual = treeSortedList.predictIndexOf(newEntry),
        )
    }

    @Test
    fun test_predictIndexOf_nonEmpty_noDuplicatesPresent() {
        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
            Entry(key = 40, value = "a"),
            Entry(key = 10, value = "a"),
            Entry(key = 0, value = "z"),
            Entry(key = 30, value = "w"),
            Entry(key = 20, value = "a"),
            Entry(key = 50, value = "a"),
        )

        val newEntry = Entry(key = 25, value = "a")

        assertEquals(
            expected = 3,
            actual = treeSortedList.predictIndexOf(newEntry),
        )
    }

    @Test
    fun test_predictIndexOf_nonEmpty_orderWiseDuplicatesPresent() {
        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
            Entry(key = 40, value = "a"),
            Entry(key = 10, value = "a"),
            Entry(key = 0, value = "z"),
            Entry(key = 30, value = "w"),
            Entry(key = 20, value = "a"),
            Entry(key = 10, value = "b"),
            Entry(key = 50, value = "a"),
            Entry(key = 10, value = "c"),
        )

        val newEntry = Entry(key = 10, value = "p")

        assertEquals(
            expected = 4,
            actual = treeSortedList.predictIndexOf(newEntry),
        )
    }

    @Test
    fun test_predictIndexOf_nonEmpty_literalDuplicatesPresent() {
        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
            Entry(
                key = 40,
                value = "a",
            ),
            Entry(
                key = 10,
                value = "a",
            ),
            Entry(
                key = 20,
                value = "a",
            ),
            Entry(
                key = 10,
                value = "b",
            ),
            Entry(
                key = 0,
                value = "z",
            ),
            Entry(
                key = 10,
                value = "c",
            ),
            Entry(
                key = 20,
                value = "c",
            ),
        )

        val newEntry = Entry(
            key = 10,
            value = "a",
        )

        assertEquals(
            expected = 4,
            actual = treeSortedList.predictIndexOf(newEntry),
        )
    }

    @Test
    fun test_add_empty() {
        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
        )

        val newEntry = Entry(key = 10, value = "a")

        assertTrue(
            treeSortedList.add(newEntry),
        )

        treeSortedList.verifyContent(
            expectedElements = listOf(newEntry),
        )
    }

    @Test
    fun test_add_nonEmpty_noDuplicatesPresent() {
        val initialEntries = listOf(
            Entry(key = 40, value = "a"),
            Entry(key = 10, value = "a"),
            Entry(key = 0, value = "z"),
            Entry(key = 30, value = "w"),
            Entry(key = 20, value = "a"),
            Entry(key = 10, value = "b"),
            Entry(key = 50, value = "a"),
            Entry(key = 10, value = "c"),
        )

        val newEntry = Entry(key = 25, value = "a")

        val newEntries = initialEntries + newEntry

        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
            elements = initialEntries,
        )

        assertTrue(
            treeSortedList.add(newEntry),
        )

        treeSortedList.verifyContent(
            expectedElements = newEntries.sortedWith(Entry.keyComparator),
        )
    }

    @Test
    fun test_add_nonEmpty_orderWiseDuplicatesPresent() {
        val initialEntries = listOf(
            Entry(key = 40, value = "a"),
            Entry(key = 10, value = "a"),
            Entry(key = 0, value = "z"),
            Entry(key = 30, value = "w"),
            Entry(key = 20, value = "a"),
            Entry(key = 10, value = "b"),
            Entry(key = 50, value = "a"),
            Entry(key = 10, value = "c"),
        )

        val newEntry = Entry(key = 10, value = "p")

        val newEntries = initialEntries + newEntry

        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
            elements = initialEntries,
        )

        assertTrue(
            treeSortedList.add(newEntry),
        )

        treeSortedList.verifyContent(
            expectedElements = newEntries.sortedWith(Entry.keyComparator),
        )
    }

    @Test
    fun test_add_nonEmpty_literalDuplicatesPresent() {
        val initialEntries = listOfNotNull(
            Entry(
                key = 40,
                value = "a",
            ),
            Entry(
                uniqueTag = "#0",
                key = 10,
                value = "a",
            ),
            Entry(
                key = 20,
                value = "a",
            ),
            Entry(
                uniqueTag = "#1",
                key = 10,
                value = "a",
            ),
            Entry(
                key = 0,
                value = "z",
            ),
            Entry(
                uniqueTag = "#2",
                key = 10,
                value = "a",
            ),
            Entry(
                key = 20,
                value = "c",
            ),
        )

        val newEntry = Entry(
            uniqueTag = "#3",
            key = 10,
            value = "a",
        )

        val newEntries = initialEntries + newEntry

        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
            elements = initialEntries,
        )

        assertTrue(
            treeSortedList.add(newEntry),
        )

        treeSortedList.verifyContent(
            expectedElements = newEntries.sortedWith(Entry.keyComparator),
        )

        assertEquals(
            expected = listOf("#0", "#1", "#2", "#3"),
            actual = treeSortedList.mapNotNull { it.uniqueTag },
        )
    }

    @Test
    fun test_insert_empty() {
        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
        )

        val newEntry = Entry(key = 10, value = "a")

        val insertedEntryHandle = treeSortedList.insert(newEntry)

        treeSortedList.verifyContent(
            expectedElements = listOf(newEntry),
        )

        val resolvedEntry = treeSortedList.getVia(insertedEntryHandle)

        assertEquals(
            expected = newEntry,
            actual = resolvedEntry,
        )
    }

    @Test
    fun test_insert_nonEmpty_noDuplicatesPresent() {
        val initialEntries = listOf(
            Entry(key = 40, value = "a"),
            Entry(key = 10, value = "a"),
            Entry(key = 0, value = "z"),
            Entry(key = 30, value = "w"),
            Entry(key = 20, value = "a"),
            Entry(key = 10, value = "b"),
            Entry(key = 50, value = "a"),
            Entry(key = 10, value = "c"),
        )

        val newEntry = Entry(key = 25, value = "a")

        val newEntries = initialEntries + newEntry

        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
            elements = initialEntries,
        )

        val insertedEntryHandle = treeSortedList.insert(newEntry)

        treeSortedList.verifyContent(
            expectedElements = newEntries.sortedWith(Entry.keyComparator),
        )

        val resolvedEntry = treeSortedList.getVia(insertedEntryHandle)

        assertEquals(
            expected = newEntry,
            actual = resolvedEntry,
        )
    }

    @Test
    fun test_insert_nonEmpty_orderWiseDuplicatesPresent() {
        val initialEntries = listOf(
            Entry(key = 40, value = "a"),
            Entry(key = 10, value = "a"),
            Entry(key = 0, value = "z"),
            Entry(key = 30, value = "w"),
            Entry(key = 20, value = "a"),
            Entry(key = 10, value = "b"),
            Entry(key = 50, value = "a"),
            Entry(key = 10, value = "c"),
        )

        val newEntry = Entry(key = 10, value = "p")

        val newEntries = initialEntries + newEntry

        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
            elements = initialEntries,
        )

        val insertedEntryHandle = treeSortedList.insert(newEntry)

        treeSortedList.verifyContent(
            expectedElements = newEntries.sortedWith(Entry.keyComparator),
        )

        val resolvedEntry = treeSortedList.getVia(insertedEntryHandle)

        assertEquals(
            expected = newEntry,
            actual = resolvedEntry,
        )
    }

    @Test
    fun test_insert_nonEmpty_literalDuplicatesPresent() {
        val initialEntries = listOfNotNull(
            Entry(
                key = 40,
                value = "a",
            ),
            Entry(
                uniqueTag = "#0",
                key = 10,
                value = "a",
            ),
            Entry(
                key = 20,
                value = "a",
            ),
            Entry(
                uniqueTag = "#1",
                key = 10,
                value = "a",
            ),
            Entry(
                key = 0,
                value = "z",
            ),
            Entry(
                uniqueTag = "#2",
                key = 10,
                value = "a",
            ),
            Entry(
                key = 20,
                value = "c",
            ),
        )

        val newEntry = Entry(
            uniqueTag = "#3",
            key = 10,
            value = "a",
        )

        val newEntries = initialEntries + newEntry

        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
            elements = initialEntries,
        )

        val insertedEntryHandle = treeSortedList.insert(newEntry)

        treeSortedList.verifyContent(
            expectedElements = newEntries.sortedWith(Entry.keyComparator),
        )

        assertEquals(
            expected = listOf("#0", "#1", "#2", "#3"),
            actual = treeSortedList.mapNotNull { it.uniqueTag },
        )

        val resolvedEntry = treeSortedList.getVia(insertedEntryHandle)

        assertEquals(
            expected = newEntry,
            actual = resolvedEntry,
        )
    }

    @Test
    fun test_remove_contained_noDuplicatesPresent() {
        val removedEntry = Entry(key = 10, value = "b")

        fun buildEntries(
            shouldIncludeRemoved: Boolean,
        ): List<Entry> = listOfNotNull(
            Entry(
                key = 40,
                value = "a",
            ),
            Entry(
                key = 10,
                value = "a",
            ),
            Entry(
                key = 0,
                value = "z",
            ),
            Entry(
                key = 30,
                value = "w",
            ),
            Entry(
                key = 20,
                value = "a",
            ),
            removedEntry.takeIf { shouldIncludeRemoved },
            Entry(
                key = 50,
                value = "a",
            ),
            Entry(
                key = 10,
                value = "c",
            ),
        )

        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
            elements = buildEntries(shouldIncludeRemoved = true),
        )

        assertTrue(
            treeSortedList.remove(removedEntry),
        )

        treeSortedList.verifyContent(
            expectedElements = buildEntries(
                shouldIncludeRemoved = false,
            ).sortedWith(Entry.keyComparator),
        )
    }

    @Test
    fun test_remove_contained_orderWiseDuplicatesPresent() {
        val removedEntry = Entry(
            key = 10,
            value = "b",
        )

        fun buildEntries(
            shouldIncludeRemoved: Boolean,
        ): List<Entry> = listOfNotNull(
            Entry(
                key = 40,
                value = "a",
            ),
            Entry(
                key = 10,
                value = "a",
            ),
            Entry(
                key = 0,
                value = "z",
            ),
            removedEntry.takeIf { shouldIncludeRemoved },
            Entry(
                key = 10,
                value = "c",
            ),
            Entry(
                key = 20,
                value = "a",
            ),
            Entry(
                key = 10,
                value = "d",
            ),
            Entry(
                key = 20,
                value = "c",
            ),
        )

        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
            elements = buildEntries(shouldIncludeRemoved = true),
        )

        assertTrue(
            treeSortedList.remove(removedEntry),
        )

        treeSortedList.verifyContent(
            expectedElements = buildEntries(
                shouldIncludeRemoved = false,
            ).sortedWith(Entry.keyComparator),
        )
    }

    @Test
    fun test_remove_contained_literalDuplicatesPresent() {
        val removedEntry = Entry(
            uniqueTag = "#0",
            key = 10,
            value = "a",
        )

        fun buildEntries(
            shouldIncludeRemoved: Boolean,
        ): List<Entry> = listOfNotNull(
            Entry(
                key = 40,
                value = "a",
            ),
            removedEntry.takeIf { shouldIncludeRemoved },
            Entry(
                uniqueTag = "#1",
                key = 10,
                value = "a",
            ),
            Entry(
                key = 0,
                value = "z",
            ),
            Entry(
                uniqueTag = "#2",
                key = 10,
                value = "a",
            ),
            Entry(
                key = 20,
                value = "a",
            ),
            Entry(
                uniqueTag = "#3",
                key = 10,
                value = "a",
            ),
            Entry(
                key = 20,
                value = "c",
            ),
        )

        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
            elements = buildEntries(shouldIncludeRemoved = true),
        )

        assertTrue(
            treeSortedList.remove(removedEntry),
        )

        treeSortedList.verifyContent(
            expectedElements = buildEntries(
                shouldIncludeRemoved = false,
            ).sortedWith(Entry.keyComparator),
        )

        assertEquals(
            expected = setOf(
                "#1",
                "#2",
                "#3",
            ),
            actual = treeSortedList.mapNotNull { it.uniqueTag }.toSet(),
        )
    }

    @Test
    fun test_remove_nonContained_noDuplicatesPresent() {
        val initialEntries = listOf(
            Entry(
                key = 40,
                value = "a",
            ),
            Entry(
                key = 10,
                value = "a",
            ),
            Entry(
                key = 0,
                value = "z",
            ),
            Entry(
                key = 10,
                value = "a",
            ),
            Entry(
                key = 20,
                value = "a",
            ),
            Entry(
                key = 10,
                value = "a",
            ),
            Entry(
                key = 20,
                value = "c",
            ),
        )

        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
            elements = initialEntries,
        )

        assertFalse(
            treeSortedList.remove(
                Entry(
                    key = 11,
                    value = "??",
                ),
            ),
        )

        treeSortedList.verifyContent(
            expectedElements = initialEntries.sortedWith(Entry.keyComparator),
        )
    }

    @Test
    fun test_removeVia() {
        val initialEntries = listOf(
            Entry(key = 40, value = "a"),
            Entry(key = 10, value = "a"),
            Entry(key = 0, value = "z"),
            Entry(key = 30, value = "w"),
            Entry(key = 20, value = "a"),
            Entry(key = 10, value = "b"),
            Entry(key = 50, value = "a"),
            Entry(key = 10, value = "c"),
        )

        val temporaryEntry = Entry(key = 25, value = "a")

        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
            elements = initialEntries,
        )

        val insertedEntryHandle = treeSortedList.insert(temporaryEntry)

        val removedEntry = treeSortedList.removeVia(insertedEntryHandle)

        assertEquals(
            expected = temporaryEntry,
            actual = removedEntry,
        )

        treeSortedList.verifyContent(
            expectedElements = initialEntries.sortedWith(Entry.keyComparator),
        )
    }

    @Test
    fun test_remove_nonContained_orderWiseDuplicatesPresent() {
        val initialEntries = listOf(
            Entry(
                key = 40,
                value = "a",
            ),
            Entry(
                key = 10,
                value = "a",
            ),
            Entry(
                key = 0,
                value = "z",
            ),
            Entry(
                key = 10,
                value = "a",
            ),
            Entry(
                key = 20,
                value = "a",
            ),
            Entry(
                key = 10,
                value = "a",
            ),
            Entry(
                key = 20,
                value = "c",
            ),
        )

        val treeSortedList = treeSortedListOf(
            comparator = Entry.keyComparator,
            elements = initialEntries,
        )

        assertFalse(
            treeSortedList.remove(
                Entry(
                    key = 10,
                    value = "??",
                ),
            ),
        )

        treeSortedList.verifyContent(
            expectedElements = initialEntries.sortedWith(Entry.keyComparator),
        )
    }
}
