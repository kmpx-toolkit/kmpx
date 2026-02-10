package dev.kmpx.collections.sets

import dev.kmpx.collections.SortedCollections.RankKind
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@Suppress("ClassName")
class TreeSet_select_tests {
    @Test
    fun test_select_emptySet() {
        val set = treeSetOf<Int>()

        assertNull(
            actual = set.select(rank = 0),
        )
    }

    @Test
    fun test_select_emptySet_negativeRank() {
        val set = treeSetOf<Int>()

        assertNull(
            actual = set.select(rank = -1),
        )
    }

    @Test
    fun test_select_singleElement() {
        val set = treeSetOf(20)

        assertEquals(
            expected = 20,
            actual = set.select(rank = 0),
        )
    }

    @Test
    fun test_select_singleElement_outOfBounds() {
        val set = treeSetOf(20)

        assertNull(
            actual = set.select(rank = 1),
        )
    }

    @Test
    fun test_select_singleElement_negativeRank() {
        val set = treeSetOf(20)

        assertNull(
            actual = set.select(rank = -1),
        )
    }

    @Test
    fun test_select_multipleElements_firstRank() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        assertEquals(
            expected = 10,
            actual = set.select(rank = 0),
        )
    }

    @Test
    fun test_select_multipleElements_middleRanks() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        assertEquals(
            expected = 20,
            actual = set.select(rank = 1),
        )

        assertEquals(
            expected = 30,
            actual = set.select(rank = 2),
        )

        assertEquals(
            expected = 40,
            actual = set.select(rank = 3),
        )
    }

    @Test
    fun test_select_multipleElements_lastRank() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        assertEquals(
            expected = 50,
            actual = set.select(rank = 4),
        )
    }

    @Test
    fun test_select_multipleElements_allRanks() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        // Test all valid ranks in order
        assertEquals(
            expected = 10,
            actual = set.select(rank = 0),
        )

        assertEquals(
            expected = 20,
            actual = set.select(rank = 1),
        )

        assertEquals(
            expected = 30,
            actual = set.select(rank = 2),
        )

        assertEquals(
            expected = 40,
            actual = set.select(rank = 3),
        )

        assertEquals(
            expected = 50,
            actual = set.select(rank = 4),
        )
    }

    @Test
    fun test_select_outOfBounds_positive() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        assertNull(
            actual = set.select(rank = 5),
        )

        assertNull(
            actual = set.select(rank = 100),
        )
    }

    @Test
    fun test_select_outOfBounds_negative() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        assertNull(
            actual = set.select(rank = -1),
        )

        assertNull(
            actual = set.select(rank = -100),
        )
    }

    @Test
    fun test_select_twoElements() {
        val set = treeSetOf(10, 20)

        assertEquals(
            expected = 10,
            actual = set.select(rank = 0),
        )

        assertEquals(
            expected = 20,
            actual = set.select(rank = 1),
        )

        assertNull(
            actual = set.select(rank = 2),
        )

        assertNull(
            actual = set.select(rank = -1),
        )
    }

    @Test
    fun test_select_afterRemoval() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        // Remove middle element
        set.remove(element = 30)

        // Verify ranks shift correctly
        assertEquals(
            expected = 10,
            actual = set.select(rank = 0),
        )

        assertEquals(
            expected = 20,
            actual = set.select(rank = 1),
        )

        assertEquals(
            expected = 40,
            actual = set.select(rank = 2),
        )

        assertEquals(
            expected = 50,
            actual = set.select(rank = 3),
        )

        assertNull(
            actual = set.select(rank = 4),
        )
    }

    @Test
    fun test_select_afterInsertion() {
        val set = treeSetOf(10, 20, 40, 50)

        // Insert element in the middle
        set.add(element = 30)

        // Verify all ranks
        assertEquals(
            expected = 10,
            actual = set.select(rank = 0),
        )

        assertEquals(
            expected = 20,
            actual = set.select(rank = 1),
        )

        assertEquals(
            expected = 30,
            actual = set.select(rank = 2),
        )

        assertEquals(
            expected = 40,
            actual = set.select(rank = 3),
        )

        assertEquals(
            expected = 50,
            actual = set.select(rank = 4),
        )

        assertNull(
            actual = set.select(rank = 5),
        )
    }

    @Test
    fun test_select_largeSet() {
        // Test with a larger set to verify correctness with complex tree structure
        val elements = listOf(5, 15, 25, 35, 45, 55, 65, 75, 85, 95)
        val set = treeSetOf(*elements.toTypedArray())

        // Verify all ranks return correct elements
        elements.forEachIndexed { index, expectedElement ->
            assertEquals(
                expected = expectedElement,
                actual = set.select(rank = index),
                message = "Failed for rank $index, expected element $expectedElement",
            )
        }

        // Verify out of bounds
        assertNull(
            actual = set.select(rank = elements.size),
        )

        assertNull(
            actual = set.select(rank = -1),
        )
    }

    @Test
    fun test_select_roundTrip_withFindRank() {
        // Integration test: verify that select and findRank are inverse operations
        val set = treeSetOf(10, 20, 30, 40, 50)

        for (rank in 0 until 5) {
            val element = set.select(rank = rank)!!
            val rankResult = set.findRank(element = element)

            assertEquals(
                expected = rank,
                actual = rankResult.rank,
                message = "Round trip failed for rank $rank",
            )

            assertEquals(
                expected = RankKind.Existing,
                actual = rankResult.kind,
                message = "Element at rank $rank should be Existing",
            )
        }
    }

    @Test
    fun test_select_unorderedInsertion() {
        // Insert elements in non-sorted order and verify select returns them in sorted order
        val set = treeSetOf<Int>()

        set.add(50)
        set.add(20)
        set.add(40)
        set.add(10)
        set.add(30)

        assertEquals(
            expected = 10,
            actual = set.select(rank = 0),
        )

        assertEquals(
            expected = 20,
            actual = set.select(rank = 1),
        )

        assertEquals(
            expected = 30,
            actual = set.select(rank = 2),
        )

        assertEquals(
            expected = 40,
            actual = set.select(rank = 3),
        )

        assertEquals(
            expected = 50,
            actual = set.select(rank = 4),
        )
    }

    @Test
    fun test_select_clearAndReuse() {
        val set = treeSetOf(10, 20, 30)

        assertEquals(
            expected = 20,
            actual = set.select(rank = 1),
        )

        // Clear the set
        set.clear()

        assertNull(
            actual = set.select(rank = 0),
        )

        // Add new elements
        set.add(40)
        set.add(50)

        assertEquals(
            expected = 40,
            actual = set.select(rank = 0),
        )

        assertEquals(
            expected = 50,
            actual = set.select(rank = 1),
        )

        assertNull(
            actual = set.select(rank = 2),
        )
    }
}
