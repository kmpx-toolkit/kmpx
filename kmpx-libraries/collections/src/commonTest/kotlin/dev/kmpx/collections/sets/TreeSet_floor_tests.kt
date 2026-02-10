package dev.kmpx.collections.sets

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@Suppress("ClassName")
class TreeSet_floor_tests {
    @Test
    fun test_floor_emptySet() {
        val set = treeSetOf<Int>()

        assertNull(
            actual = set.floor(element = 10),
        )
    }

    @Test
    fun test_floor_singleElement_exactMatch() {
        val set = treeSetOf(20)

        assertEquals(
            expected = 20,
            actual = set.floor(element = 20),
        )
    }

    @Test
    fun test_floor_singleElement_larger() {
        val set = treeSetOf(20)

        assertEquals(
            expected = 20,
            actual = set.floor(element = 30),
        )
    }

    @Test
    fun test_floor_singleElement_smaller() {
        val set = treeSetOf(20)

        assertNull(
            actual = set.floor(element = 10),
        )
    }

    @Test
    fun test_floor_exactMatch_first() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        assertEquals(
            expected = 10,
            actual = set.floor(element = 10),
        )
    }

    @Test
    fun test_floor_exactMatch_middle() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        assertEquals(
            expected = 30,
            actual = set.floor(element = 30),
        )
    }

    @Test
    fun test_floor_exactMatch_last() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        assertEquals(
            expected = 50,
            actual = set.floor(element = 50),
        )
    }

    @Test
    fun test_floor_allExactMatches() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        assertEquals(
            expected = 10,
            actual = set.floor(element = 10),
        )

        assertEquals(
            expected = 20,
            actual = set.floor(element = 20),
        )

        assertEquals(
            expected = 30,
            actual = set.floor(element = 30),
        )

        assertEquals(
            expected = 40,
            actual = set.floor(element = 40),
        )

        assertEquals(
            expected = 50,
            actual = set.floor(element = 50),
        )
    }

    @Test
    fun test_floor_betweenElements() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        // Element 15 is between 10 and 20, floor should be 10
        assertEquals(
            expected = 10,
            actual = set.floor(element = 15),
        )

        // Element 25 is between 20 and 30, floor should be 20
        assertEquals(
            expected = 20,
            actual = set.floor(element = 25),
        )

        // Element 35 is between 30 and 40, floor should be 30
        assertEquals(
            expected = 30,
            actual = set.floor(element = 35),
        )

        // Element 45 is between 40 and 50, floor should be 40
        assertEquals(
            expected = 40,
            actual = set.floor(element = 45),
        )
    }

    @Test
    fun test_floor_smallerThanAll() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        assertNull(
            actual = set.floor(element = 5),
        )

        assertNull(
            actual = set.floor(element = 0),
        )

        assertNull(
            actual = set.floor(element = -100),
        )
    }

    @Test
    fun test_floor_largerThanAll() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        // Element larger than all should return the largest element
        assertEquals(
            expected = 50,
            actual = set.floor(element = 55),
        )

        assertEquals(
            expected = 50,
            actual = set.floor(element = 100),
        )

        assertEquals(
            expected = 50,
            actual = set.floor(element = 1000),
        )
    }

    @Test
    fun test_floor_twoElements() {
        val set = treeSetOf(10, 20)

        assertNull(
            actual = set.floor(element = 5),
        )

        assertEquals(
            expected = 10,
            actual = set.floor(element = 10),
        )

        assertEquals(
            expected = 10,
            actual = set.floor(element = 15),
        )

        assertEquals(
            expected = 20,
            actual = set.floor(element = 20),
        )

        assertEquals(
            expected = 20,
            actual = set.floor(element = 25),
        )
    }

    @Test
    fun test_floor_afterRemoval_floorRemoved() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        // Element 25's floor is initially 20
        assertEquals(
            expected = 20,
            actual = set.floor(element = 25),
        )

        // Remove 20
        set.remove(element = 20)

        // Element 25's floor is now 10
        assertEquals(
            expected = 10,
            actual = set.floor(element = 25),
        )
    }

    @Test
    fun test_floor_afterRemoval_elementRemoved() {
        val set = treeSetOf(10, 20, 30, 40, 50)

        // Element 30's floor is itself (30)
        assertEquals(
            expected = 30,
            actual = set.floor(element = 30),
        )

        // Remove 30
        set.remove(element = 30)

        // Element 30's floor is now 20
        assertEquals(
            expected = 20,
            actual = set.floor(element = 30),
        )
    }

    @Test
    fun test_floor_afterInsertion_newFloor() {
        val set = treeSetOf(10, 30, 40, 50)

        // Element 25's floor is 10
        assertEquals(
            expected = 10,
            actual = set.floor(element = 25),
        )

        // Add 20
        set.add(element = 20)

        // Element 25's floor is now 20
        assertEquals(
            expected = 20,
            actual = set.floor(element = 25),
        )
    }

    @Test
    fun test_floor_afterInsertion_betweenElementAndFloor() {
        val set = treeSetOf(10, 40, 50)

        // Element 35's floor is 10
        assertEquals(
            expected = 10,
            actual = set.floor(element = 35),
        )

        // Add 30
        set.add(element = 30)

        // Element 35's floor is now 30
        assertEquals(
            expected = 30,
            actual = set.floor(element = 35),
        )

        // Add 20
        set.add(element = 20)

        // Element 35's floor is still 30
        assertEquals(
            expected = 30,
            actual = set.floor(element = 35),
        )
    }

    @Test
    fun test_floor_largeSet() {
        // Test with a larger set to verify correctness with complex tree structure
        val elements = listOf(5, 15, 25, 35, 45, 55, 65, 75, 85, 95)
        val set = treeSetOf(*elements.toTypedArray())

        // Test exact matches
        elements.forEach { element ->
            assertEquals(
                expected = element,
                actual = set.floor(element = element),
                message = "Floor of $element should be itself",
            )
        }

        // Test elements between existing ones
        assertEquals(
            expected = 5,
            actual = set.floor(element = 10),
        )

        assertEquals(
            expected = 25,
            actual = set.floor(element = 30),
        )

        assertEquals(
            expected = 55,
            actual = set.floor(element = 60),
        )

        assertEquals(
            expected = 85,
            actual = set.floor(element = 90),
        )

        // Test smaller than all
        assertNull(
            actual = set.floor(element = 1),
        )

        // Test larger than all
        assertEquals(
            expected = 95,
            actual = set.floor(element = 100),
        )
    }

    @Test
    fun test_floor_negativeNumbers() {
        val set = treeSetOf(-50, -30, -10, 10, 30, 50)

        assertEquals(
            expected = -50,
            actual = set.floor(element = -40),
        )

        assertEquals(
            expected = -30,
            actual = set.floor(element = -20),
        )

        assertEquals(
            expected = -10,
            actual = set.floor(element = 0),
        )

        assertEquals(
            expected = 10,
            actual = set.floor(element = 20),
        )

        assertEquals(
            expected = 30,
            actual = set.floor(element = 40),
        )

        assertNull(
            actual = set.floor(element = -100),
        )
    }

    @Test
    fun test_floor_consecutiveNumbers() {
        val set = treeSetOf(1, 2, 3, 4, 5)

        assertEquals(
            expected = 1,
            actual = set.floor(element = 1),
        )

        assertEquals(
            expected = 2,
            actual = set.floor(element = 2),
        )

        assertEquals(
            expected = 3,
            actual = set.floor(element = 3),
        )

        assertEquals(
            expected = 4,
            actual = set.floor(element = 4),
        )

        assertEquals(
            expected = 5,
            actual = set.floor(element = 5),
        )

        assertNull(
            actual = set.floor(element = 0),
        )

        assertEquals(
            expected = 5,
            actual = set.floor(element = 6),
        )
    }

    @Test
    fun test_floor_unorderedInsertion() {
        // Insert elements in non-sorted order and verify floor works correctly
        val set = treeSetOf<Int>()

        set.add(50)
        set.add(20)
        set.add(40)
        set.add(10)
        set.add(30)

        assertEquals(
            expected = 10,
            actual = set.floor(element = 15),
        )

        assertEquals(
            expected = 20,
            actual = set.floor(element = 25),
        )

        assertEquals(
            expected = 30,
            actual = set.floor(element = 35),
        )

        assertEquals(
            expected = 40,
            actual = set.floor(element = 45),
        )

        assertEquals(
            expected = 50,
            actual = set.floor(element = 55),
        )
    }

    @Test
    fun test_floor_clearAndReuse() {
        val set = treeSetOf(10, 20, 30)

        assertEquals(
            expected = 20,
            actual = set.floor(element = 25),
        )

        // Clear the set
        set.clear()

        assertNull(
            actual = set.floor(element = 25),
        )

        // Add new elements
        set.add(40)
        set.add(50)
        set.add(60)

        assertEquals(
            expected = 40,
            actual = set.floor(element = 45),
        )

        assertEquals(
            expected = 50,
            actual = set.floor(element = 55),
        )

        assertNull(
            actual = set.floor(element = 35),
        )
    }

    @Test
    fun test_floor_integration_withCeiling() {
        // Integration test: verify floor and ceiling are consistent
        val set = treeSetOf(10, 20, 30, 40, 50)

        // For an element in the set, floor and ceiling should be the same
        assertEquals(
            expected = set.floor(element = 30),
            actual = set.ceiling(element = 30),
        )

        // For an element not in the set, floor should be less than ceiling
        val element = 25
        val floorValue = set.floor(element = element)
        val ceilingValue = set.ceiling(element = element)

        assertEquals(expected = 20, actual = floorValue)
        assertEquals(expected = 30, actual = ceilingValue)
    }
}
