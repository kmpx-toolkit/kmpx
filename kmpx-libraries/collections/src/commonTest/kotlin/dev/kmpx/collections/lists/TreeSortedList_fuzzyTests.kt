package dev.kmpx.collections.lists

import dev.kmpx.collections.multi_sets.mutableMultiSetOf
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("ClassName")
class TreeSortedList_fuzzyTests {
    private data class Entry(
        // A non-unique ID
        val id: Int,
        // A key for sorting
        val key: Int,
    )

    @Suppress("ConstPropertyName")
    companion object {
        private const val maxSize = 4_000
        private const val iterationCount = 2_000
    }

    private val keyComparator = compareBy<Entry> { it.key }

    @Test
    fun test_growAndShrink() {
        val random = Random(0)

        val mutableControlMultiSet = mutableMultiSetOf<Entry>()

        val treeSortedList = TreeSortedList(
            comparator = keyComparator,
        )

        var nextId = 0

        fun addRandomEntry() {
            val newEntry = when {
                mutableControlMultiSet.isNotEmpty() && random.nextDouble() < 0.01 -> { // Produce a duplicate on purpose (1% chance)
                    mutableControlMultiSet.random()
                }

                else -> { // Produce a new entry (may still be a duplicate with a small chance)
                    // pool of keys = 10% of total size (collisions will happen)
                    val key = random.nextInt(
                        from = 0, until = maxSize / 10
                    )

                    Entry(
                        id = nextId++,
                        key = key,
                    )
                }
            }

            mutableControlMultiSet.add(newEntry)

            assertTrue(
                actual = treeSortedList.add(newEntry),
                message = "Failed to add new entry",
            )
        }

        fun removeRandomEntry() {
            val existingEntry = mutableControlMultiSet.random()

            mutableControlMultiSet.remove(existingEntry)

            assertTrue(
                actual = treeSortedList.remove(existingEntry),
                message = "Failed to remove existing entry",
            )
        }

        fun removeNonContainedEntry() {
            val existingEntry = mutableControlMultiSet.random()

            // Get a key of an existing entry
            val key = existingEntry.key

            assertFalse(
                actual = treeSortedList.remove(
                    Entry(
                        id = -1, // An invalid ID
                        key = key,
                    ),
                ),
                message = "Removed non-contained entry",
            )
        }

        while (treeSortedList.size < maxSize / 2) {
            addRandomEntry()
        }

        repeat(iterationCount) {
            val x = random.nextDouble()

            when {
                x < 0.01 -> {
                    removeNonContainedEntry()
                }

                x < 0.2 -> {
                    removeRandomEntry()
                }

                else -> {
                    addRandomEntry()
                }
            }

            treeSortedList.verifyContentSorted(
                comparator = keyComparator,
                expectedElements = mutableControlMultiSet,
            )
        }
    }
}
