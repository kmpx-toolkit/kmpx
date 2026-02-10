package dev.kmpx.collections.sets

import dev.kmpx.collections.SortedCollections.RankKind
import dev.kmpx.collections.SortedCollections.RankResult
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("ClassName")
class TreeSet_findRank_tests {
    @Test
    fun test_findRank_emptySet() {
        val set = treeSetOf<Int>()

        val result = set.findRank(element = 10)

        assertEquals(
            expected = RankResult(
                rank = 0,
                kind = RankKind.Potential,
            ),
            actual = result,
        )
    }

    @Test
    fun test_findRank_existingElements() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        // Test each element has correct rank
        assertEquals(
            expected = RankResult(
                rank = 0,
                kind = RankKind.Existing,
            ),
            actual = set.findRank(element = 10),
        )

        assertEquals(
            expected = RankResult(
                rank = 1,
                kind = RankKind.Existing,
            ),
            actual = set.findRank(element = 20),
        )

        assertEquals(
            expected = RankResult(
                rank = 2,
                kind = RankKind.Existing,
            ),
            actual = set.findRank(element = 30),
        )

        assertEquals(
            expected = RankResult(
                rank = 3,
                kind = RankKind.Existing,
            ),
            actual = set.findRank(element = 40),
        )

        assertEquals(
            expected = RankResult(
                rank = 4,
                kind = RankKind.Existing,
            ),
            actual = set.findRank(element = 50),
        )
    }

    @Test
    fun test_findRank_potentialElements_beforeAll() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        val result = set.findRank(element = 5)

        assertEquals(
            expected = RankResult(
                rank = 0,
                kind = RankKind.Potential,
            ),
            actual = result,
        )
    }

    @Test
    fun test_findRank_potentialElements_betweenElements() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        // Element 15 would be inserted at rank 1 (between 10 and 20)
        assertEquals(
            expected = RankResult(
                rank = 1,
                kind = RankKind.Potential,
            ),
            actual = set.findRank(element = 15),
        )

        // Element 25 would be inserted at rank 2 (between 20 and 30)
        assertEquals(
            expected = RankResult(
                rank = 2,
                kind = RankKind.Potential,
            ),
            actual = set.findRank(element = 25),
        )

        // Element 35 would be inserted at rank 3 (between 30 and 40)
        assertEquals(
            expected = RankResult(
                rank = 3,
                kind = RankKind.Potential,
            ),
            actual = set.findRank(element = 35),
        )

        // Element 45 would be inserted at rank 4 (between 40 and 50)
        assertEquals(
            expected = RankResult(
                rank = 4,
                kind = RankKind.Potential,
            ),
            actual = set.findRank(element = 45),
        )
    }

    @Test
    fun test_findRank_potentialElements_afterAll() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        val result = set.findRank(element = 55)

        assertEquals(
            expected = RankResult(
                rank = 5,
                kind = RankKind.Potential,
            ),
            actual = result,
        )
    }

    @Test
    fun test_findRank_singleElement_existing() {
        val set = treeSetOf(20)

        val result = set.findRank(element = 20)

        assertEquals(
            expected = RankResult(
                rank = 0,
                kind = RankKind.Existing,
            ),
            actual = result,
        )
    }

    @Test
    fun test_findRank_singleElement_potential_before() {
        val set = treeSetOf(20)

        val result = set.findRank(element = 10)

        assertEquals(
            expected = RankResult(
                rank = 0,
                kind = RankKind.Potential,
            ),
            actual = result,
        )
    }

    @Test
    fun test_findRank_singleElement_potential_after() {
        val set = treeSetOf(20)

        val result = set.findRank(element = 30)

        assertEquals(
            expected = RankResult(
                rank = 1,
                kind = RankKind.Potential,
            ),
            actual = result,
        )
    }

    @Test
    fun test_findRank_afterRemoval() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        // Verify element 30 exists at rank 2
        assertEquals(
            expected = RankResult(
                rank = 2,
                kind = RankKind.Existing,
            ),
            actual = set.findRank(element = 30),
        )

        // Remove element 30
        set.remove(element = 30)

        // Verify element 30 is now potential at rank 2
        assertEquals(
            expected = RankResult(
                rank = 2,
                kind = RankKind.Potential,
            ),
            actual = set.findRank(element = 30),
        )

        // Verify other elements have shifted ranks
        assertEquals(
            expected = RankResult(
                rank = 2,
                kind = RankKind.Existing,
            ),
            actual = set.findRank(element = 40),
        )

        assertEquals(
            expected = RankResult(
                rank = 3,
                kind = RankKind.Existing,
            ),
            actual = set.findRank(element = 50),
        )
    }

    @Test
    fun test_findRank_afterInsertion() {
        val set = treeSetOf(10, 20, 40, 50)

        // Verify element 30 would be at rank 2
        assertEquals(
            expected = RankResult(
                rank = 2,
                kind = RankKind.Potential,
            ),
            actual = set.findRank(element = 30),
        )

        // Insert element 30
        set.add(element = 30)

        // Verify element 30 now exists at rank 2
        assertEquals(
            expected = RankResult(
                rank = 2,
                kind = RankKind.Existing,
            ),
            actual = set.findRank(element = 30),
        )

        // Verify other elements have shifted ranks
        assertEquals(
            expected = RankResult(
                rank = 3,
                kind = RankKind.Existing,
            ),
            actual = set.findRank(element = 40),
        )

        assertEquals(
            expected = RankResult(
                rank = 4,
                kind = RankKind.Existing,
            ),
            actual = set.findRank(element = 50),
        )
    }

    @Test
    fun test_findRank_twoElements() {
        val set = treeSetOf(10, 20)

        assertEquals(
            expected = RankResult(
                rank = 0,
                kind = RankKind.Existing,
            ),
            actual = set.findRank(element = 10),
        )

        assertEquals(
            expected = RankResult(
                rank = 1,
                kind = RankKind.Existing,
            ),
            actual = set.findRank(element = 20),
        )

        assertEquals(
            expected = RankResult(
                rank = 0,
                kind = RankKind.Potential,
            ),
            actual = set.findRank(element = 5),
        )

        assertEquals(
            expected = RankResult(
                rank = 1,
                kind = RankKind.Potential,
            ),
            actual = set.findRank(element = 15),
        )

        assertEquals(
            expected = RankResult(
                rank = 2,
                kind = RankKind.Potential,
            ),
            actual = set.findRank(element = 25),
        )
    }

    @Test
    fun test_findRank_largeSet() {
        // Test with a larger set to verify correctness with more complex tree structure
        val elements = listOf(5, 15, 25, 35, 45, 55, 65, 75, 85, 95)

        val set = treeSetOf(*elements.toTypedArray())

        // Verify all existing elements have correct ranks
        elements.forEachIndexed { index, element ->
            assertEquals(
                expected = RankResult(
                    rank = index,
                    kind = RankKind.Existing,
                ),
                actual = set.findRank(element = element),
                message = "Failed for element $element at expected rank $index",
            )
        }

        // Verify potential elements between existing ones
        assertEquals(
            expected = RankResult(
                rank = 0,
                kind = RankKind.Potential,
            ),
            actual = set.findRank(element = 1),
        )

        assertEquals(
            expected = RankResult(
                rank = 5,
                kind = RankKind.Potential,
            ),
            actual = set.findRank(element = 50),
        )

        assertEquals(
            expected = RankResult(
                rank = 10,
                kind = RankKind.Potential,
            ),
            actual = set.findRank(element = 100),
        )
    }
}
