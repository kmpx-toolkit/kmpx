package dev.kmpx.collections.sets

import dev.kmpx.collections.SortedCollections.RankKind
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@Suppress("ClassName")
class TreeSet_ceiling_tests {
    @Test
    fun test_ceiling_emptySet() {
        val set = treeSetOf<Int>()

        assertNull(
            actual = set.ceiling(element = 10),
        )
    }

    @Test
    fun test_ceiling_singleElement_exactMatch() {
        val set = treeSetOf(20)

        assertEquals(
            expected = 20,
            actual = set.ceiling(element = 20),
        )
    }

    @Test
    fun test_ceiling_singleElement_smaller() {
        val set = treeSetOf(20)

        assertEquals(
            expected = 20,
            actual = set.ceiling(element = 10),
        )
    }

    @Test
    fun test_ceiling_singleElement_larger() {
        val set = treeSetOf(20)

        assertNull(
            actual = set.ceiling(element = 30),
        )
    }

    @Test
    fun test_ceiling_exactMatch_first() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        assertEquals(
            expected = 10,
            actual = set.ceiling(element = 10),
        )
    }

    @Test
    fun test_ceiling_exactMatch_middle() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        assertEquals(
            expected = 30,
            actual = set.ceiling(element = 30),
        )
    }

    @Test
    fun test_ceiling_exactMatch_last() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        assertEquals(
            expected = 50,
            actual = set.ceiling(element = 50),
        )
    }

    @Test
    fun test_ceiling_allExactMatches() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        assertEquals(
            expected = 10,
            actual = set.ceiling(element = 10),
        )

        assertEquals(
            expected = 20,
            actual = set.ceiling(element = 20),
        )

        assertEquals(
            expected = 30,
            actual = set.ceiling(element = 30),
        )

        assertEquals(
            expected = 40,
            actual = set.ceiling(element = 40),
        )

        assertEquals(
            expected = 50,
            actual = set.ceiling(element = 50),
        )
    }

    @Test
    fun test_ceiling_betweenElements() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        // Element 15 is between 10 and 20, ceiling should be 20
        assertEquals(
            expected = 20,
            actual = set.ceiling(element = 15),
        )

        // Element 25 is between 20 and 30, ceiling should be 30
        assertEquals(
            expected = 30,
            actual = set.ceiling(element = 25),
        )

        // Element 35 is between 30 and 40, ceiling should be 40
        assertEquals(
            expected = 40,
            actual = set.ceiling(element = 35),
        )

        // Element 45 is between 40 and 50, ceiling should be 50
        assertEquals(
            expected = 50,
            actual = set.ceiling(element = 45),
        )
    }

    @Test
    fun test_ceiling_smallerThanAll() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        // Element smaller than all should return the smallest element
        assertEquals(
            expected = 10,
            actual = set.ceiling(element = 5),
        )

        assertEquals(
            expected = 10,
            actual = set.ceiling(element = 0),
        )

        assertEquals(
            expected = 10,
            actual = set.ceiling(element = -100),
        )
    }

    @Test
    fun test_ceiling_largerThanAll() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        assertNull(
            actual = set.ceiling(element = 55),
        )

        assertNull(
            actual = set.ceiling(element = 100),
        )

        assertNull(
            actual = set.ceiling(element = 1000),
        )
    }

    @Test
    fun test_ceiling_twoElements() {
        val set = treeSetOf(10, 20)

        assertEquals(
            expected = 10,
            actual = set.ceiling(element = 5),
        )

        assertEquals(
            expected = 10,
            actual = set.ceiling(element = 10),
        )

        assertEquals(
            expected = 20,
            actual = set.ceiling(element = 15),
        )

        assertEquals(
            expected = 20,
            actual = set.ceiling(element = 20),
        )

        assertNull(
            actual = set.ceiling(element = 25),
        )
    }

    @Test
    fun test_ceiling_afterRemoval_ceilingRemoved() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        // Element 25's ceiling is initially 30
        assertEquals(
            expected = 30,
            actual = set.ceiling(element = 25),
        )

        // Remove 30
        set.remove(element = 30)

        // Element 25's ceiling is now 40
        assertEquals(
            expected = 40,
            actual = set.ceiling(element = 25),
        )
    }

    @Test
    fun test_ceiling_afterRemoval_elementRemoved() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        // Element 30's ceiling is itself (30)
        assertEquals(
            expected = 30,
            actual = set.ceiling(element = 30),
        )

        // Remove 30
        set.remove(element = 30)

        // Element 30's ceiling is now 40
        assertEquals(
            expected = 40,
            actual = set.ceiling(element = 30),
        )
    }

    @Test
    fun test_ceiling_afterInsertion_newCeiling() {
        val set = treeSetOf(10, 30, 40, 50)

        // Element 25's ceiling is 30
        assertEquals(
            expected = 30,
            actual = set.ceiling(element = 25),
        )

        // Add 20
        set.add(element = 20)

        // Element 25's ceiling is still 30
        assertEquals(
            expected = 30,
            actual = set.ceiling(element = 25),
        )

        // Element 15's ceiling is now 20
        assertEquals(
            expected = 20,
            actual = set.ceiling(element = 15),
        )
    }

    @Test
    fun test_ceiling_afterInsertion_betweenElementAndCeiling() {
        val set = treeSetOf(10, 50, 60)

        // Element 35's ceiling is 50
        assertEquals(
            expected = 50,
            actual = set.ceiling(element = 35),
        )

        // Add 40
        set.add(element = 40)

        // Element 35's ceiling is now 40
        assertEquals(
            expected = 40,
            actual = set.ceiling(element = 35),
        )

        // Add 30
        set.add(element = 30)

        // Element 35's ceiling is still 40
        assertEquals(
            expected = 40,
            actual = set.ceiling(element = 35),
        )
    }

    @Test
    fun test_ceiling_largeSet() {
        // Test with a larger set to verify correctness with complex tree structure
        val elements = listOf(5, 15, 25, 35, 45, 55, 65, 75, 85, 95)
        val set = treeSetOf(*elements.toTypedArray())

        // Test exact matches
        elements.forEach { element ->
            assertEquals(
                expected = element,
                actual = set.ceiling(element = element),
                message = "Ceiling of $element should be itself",
            )
        }

        // Test elements between existing ones
        assertEquals(
            expected = 15,
            actual = set.ceiling(element = 10),
        )

        assertEquals(
            expected = 35,
            actual = set.ceiling(element = 30),
        )

        assertEquals(
            expected = 65,
            actual = set.ceiling(element = 60),
        )

        assertEquals(
            expected = 95,
            actual = set.ceiling(element = 90),
        )

        // Test smaller than all
        assertEquals(
            expected = 5,
            actual = set.ceiling(element = 1),
        )

        // Test larger than all
        assertNull(
            actual = set.ceiling(element = 100),
        )
    }

    @Test
    fun test_ceiling_negativeNumbers() {
        val set = treeSetOf(-50, -30, -10, 10, 30, 50)

        assertEquals(
            expected = -30,
            actual = set.ceiling(element = -40),
        )

        assertEquals(
            expected = -10,
            actual = set.ceiling(element = -20),
        )

        assertEquals(
            expected = 10,
            actual = set.ceiling(element = 0),
        )

        assertEquals(
            expected = 30,
            actual = set.ceiling(element = 20),
        )

        assertEquals(
            expected = 50,
            actual = set.ceiling(element = 40),
        )

        assertNull(
            actual = set.ceiling(element = 100),
        )
    }

    @Test
    fun test_ceiling_consecutiveNumbers() {
        val set = treeSetOf(1, 2, 3, 4, 5)

        assertEquals(
            expected = 1,
            actual = set.ceiling(element = 0),
        )

        assertEquals(
            expected = 1,
            actual = set.ceiling(element = 1),
        )

        assertEquals(
            expected = 2,
            actual = set.ceiling(element = 2),
        )

        assertEquals(
            expected = 3,
            actual = set.ceiling(element = 3),
        )

        assertEquals(
            expected = 4,
            actual = set.ceiling(element = 4),
        )

        assertEquals(
            expected = 5,
            actual = set.ceiling(element = 5),
        )

        assertNull(
            actual = set.ceiling(element = 6),
        )
    }

    @Test
    fun test_ceiling_unorderedInsertion() {
        // Insert elements in non-sorted order and verify ceiling works correctly
        val set = treeSetOf<Int>()

        set.add(50)
        set.add(20)
        set.add(40)
        set.add(10)
        set.add(30)

        assertEquals(
            expected = 20,
            actual = set.ceiling(element = 15),
        )

        assertEquals(
            expected = 30,
            actual = set.ceiling(element = 25),
        )

        assertEquals(
            expected = 40,
            actual = set.ceiling(element = 35),
        )

        assertEquals(
            expected = 50,
            actual = set.ceiling(element = 45),
        )

        assertNull(
            actual = set.ceiling(element = 55),
        )
    }

    @Test
    fun test_ceiling_clearAndReuse() {
        val set = treeSetOf(10, 20, 30)

        assertEquals(
            expected = 30,
            actual = set.ceiling(element = 25),
        )

        // Clear the set
        set.clear()

        assertNull(
            actual = set.ceiling(element = 25),
        )

        // Add new elements
        set.add(40)
        set.add(50)
        set.add(60)

        assertEquals(
            expected = 50,
            actual = set.ceiling(element = 45),
        )

        assertEquals(
            expected = 60,
            actual = set.ceiling(element = 55),
        )

        assertNull(
            actual = set.ceiling(element = 65),
        )
    }

    @Test
    fun test_ceiling_integration_withFloor() {
        // Integration test: verify ceiling and floor are consistent
        val set = treeSetOf(10, 20, 30, 40, 50)

        // For an element in the set, floor and ceiling should be the same
        assertEquals(
            expected = set.ceiling(element = 30),
            actual = set.floor(element = 30),
        )

        // For an element not in the set, ceiling should be greater than floor
        val element = 25
        val floorValue = set.floor(element = element)
        val ceilingValue = set.ceiling(element = element)

        assertEquals(expected = 20, actual = floorValue)
        assertEquals(expected = 30, actual = ceilingValue)
    }

    @Test
    fun test_ceiling_integration_withSelect() {
        // Integration test: verify ceiling with select
        val set = treeSetOf(10, 20, 30, 40, 50)

        // Ceiling of an element smaller than all should equal select(0)
        assertEquals(
            expected = set.select(rank = 0),
            actual = set.ceiling(element = 5),
        )

        // Ceiling of elements between existing ones
        assertEquals(
            expected = set.select(rank = 1), // 20
            actual = set.ceiling(element = 15),
        )

        assertEquals(
            expected = set.select(rank = 2), // 30
            actual = set.ceiling(element = 25),
        )
    }

    @Test
    fun test_ceiling_integration_withFindRank() {
        // Integration test: verify ceiling and findRank are consistent
        val set = treeSetOf(10, 20, 30, 40, 50)

        // For an element not in the set, ceiling should equal select(findRank(element).rank)
        val element = 25
        val rankResult = set.findRank(element = element)
        val ceilingValue = set.ceiling(element = element)
        val selectedValue = set.select(rank = rankResult.rank)

        assertEquals(
            expected = RankKind.Potential,
            actual = rankResult.kind,
        )

        assertEquals(
            expected = selectedValue,
            actual = ceilingValue,
        )
    }
}
