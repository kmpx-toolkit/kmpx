package dev.kmpx.collections.multi_sets

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

@Suppress("ClassName")
class HashMultiSet_tests {
    @Test
    fun test_initial_empty() {
        val multiSet = hashMultiSetOf<Int>()

        multiSet.verifyContent(
            expectedCountByElement = emptyMap(),
            unexpectedElements = setOf(10, 20, 30),
        )
    }

    @Test
    fun test_initial_withoutDuplicates() {
        val multiSet = hashMultiSetOf(10, 20, 30, 40)

        multiSet.verifyContent(
            expectedCountByElement = mapOf(
                10 to 1,
                20 to 1,
                30 to 1,
                40 to 1,
            ),
            unexpectedElements = setOf(-10, -20, -30),
        )
    }

    @Test
    fun test_initial_withDuplicates() {
        val multiSet = hashMultiSetOf(10, 10, 20, 30, 30, 30, 40, 40)

        multiSet.verifyContent(
            expectedCountByElement = mapOf(
                10 to 2,
                20 to 1,
                30 to 3,
                40 to 2,
            ),
            unexpectedElements = setOf(-10, -20, -30),
        )
    }

    @Test
    fun test_addOne_empty() {
        val multiSet = hashMultiSetOf<Int>()

        assertTrue(
            actual = multiSet.add(10),
        )

        multiSet.verifyContent(
            expectedCountByElement = mapOf(
                10 to 1,
            ),
            unexpectedElements = setOf(20, 30),
        )
    }

    @Test
    fun test_addOne_new() {
        val multiSet = hashMultiSetOf(10, 20, 30)

        assertTrue(
            actual = multiSet.add(40),
        )

        multiSet.verifyContent(
            expectedCountByElement = mapOf(
                10 to 1,
                20 to 1,
                30 to 1,
                40 to 1,
            ),
            unexpectedElements = setOf(-10, 50),
        )
    }

    @Test
    fun test_addOne_duplicate() {
        val multiSet = hashMultiSetOf(10, 20, 30)

        assertTrue(
            actual = multiSet.add(20),
        )

        multiSet.verifyContent(
            expectedCountByElement = mapOf(
                10 to 1,
                20 to 2,
                30 to 1,
            ),
            unexpectedElements = setOf(-10, 40, 50),
        )
    }

    @Test
    fun test_addMany_new() {
        val multiSet = hashMultiSetOf(10, 20, 30)

        val oldCount = multiSet.add(
            element = 40,
            count = 3,
        )

        assertEquals(
            expected = 0,
            actual = oldCount,
        )

        multiSet.verifyContent(
            expectedCountByElement = mapOf(
                10 to 1,
                20 to 1,
                30 to 1,
                40 to 3,
            ),
            unexpectedElements = setOf(-10, 50),
        )
    }

    @Test
    fun test_addMany_duplicate() {
        val multiSet = hashMultiSetOf(10, 20, 30)

        val oldCount = multiSet.add(
            element = 20,
            count = 3,
        )

        assertEquals(
            expected = 1,
            actual = oldCount,
        )

        multiSet.verifyContent(
            expectedCountByElement = mapOf(
                10 to 1,
                20 to 4,
                30 to 1,
            ),
            unexpectedElements = setOf(-10, 50),
        )
    }

    @Test
    fun test_addZero_new() {
        val multiSet = hashMultiSetOf(10, 20, 30)

        val oldCount = multiSet.add(
            element = 40,
            count = 0,
        )

        assertEquals(
            expected = 0,
            actual = oldCount,
        )

        multiSet.verifyContent(
            expectedCountByElement = mapOf(
                10 to 1,
                20 to 1,
                30 to 1,
            ),
            unexpectedElements = setOf(-10, 50),
        )
    }

    @Test
    fun test_addZero_duplicate() {
        val multiSet = hashMultiSetOf(10, 20, 30)

        val oldCount = multiSet.add(
            element = 20,
            count = 0,
        )

        assertEquals(
            expected = 1,
            actual = oldCount,
        )

        multiSet.verifyContent(
            expectedCountByElement = mapOf(
                10 to 1,
                20 to 1,
                30 to 1,
            ),
            unexpectedElements = setOf(-10, 50),
        )
    }

    @Test
    fun test_remove_nonLast() {
        val multiSet = hashMultiSetOf(10, 10, 20, 20, 20, 30, 40)

        assertTrue(
            actual = multiSet.remove(20),
        )

        multiSet.verifyContent(
            expectedCountByElement = mapOf(
                10 to 2,
                20 to 2,
                30 to 1,
                40 to 1,
            ),
            unexpectedElements = setOf(-10, 50),
        )
    }

    @Test
    fun test_remove_last() {
        val multiSet = hashMultiSetOf(10, 10, 20, 20, 20, 30, 40)

        assertTrue(
            actual = multiSet.remove(30),
        )

        multiSet.verifyContent(
            expectedCountByElement = mapOf(
                10 to 2,
                20 to 3,
                40 to 1,
            ),
            unexpectedElements = setOf(-10, 30, 50),
        )
    }

    @Test
    fun test_remove_notContained() {
        val multiSet = hashMultiSetOf(10, 10, 20, 20, 30, 40)

        assertFalse(
            actual = multiSet.remove(-10),
        )

        multiSet.verifyContent(
            expectedCountByElement = mapOf(
                10 to 2,
                20 to 2,
                30 to 1,
                40 to 1,
            ),
            unexpectedElements = setOf(-20, 50),
        )
    }

    @Test
    fun test_hashCode_unique() {
        val multiSet1 = hashMultiSetOf(1, 2, 3)
        val multiSet2 = hashMultiSetOf(4, 5, 6)

        assertNotEquals(
            illegal = multiSet1.hashCode(),
            actual = multiSet2.hashCode(),
            message = "Hash codes for different multi-sets should not be equal"
        )
    }
}
