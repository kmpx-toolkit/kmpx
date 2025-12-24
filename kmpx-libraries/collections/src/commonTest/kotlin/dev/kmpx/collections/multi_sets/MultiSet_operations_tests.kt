package dev.kmpx.collections.multi_sets

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("ClassName")
class MultiSet_operations_tests {
    @Test
    fun test_plus() {
        val multiSet1 = multiSetOf(1, 1, 1, 3, 5, 5, 7)
        val multiSet2 = multiSetOf(2, 2, 3, 4, 6, 7, 7, 8)

        val sumMultiSet = multiSet1 + multiSet2

        assertEquals(
            expected = MultiSet.ofCounts(
                1 to 3,
                2 to 2,
                3 to 2,
                4 to 1,
                5 to 2,
                6 to 1,
                7 to 3,
                8 to 1,
            ),
            actual = sumMultiSet,
        )
    }

    @Test
    fun test_minus_nonCoerced() {
        val minuendMultiSet = MultiSet.ofCounts(
            'A' to 5,
            'B' to 3,
            'C' to 6,
            'D' to 1,
        )

        val subtrahendMultiSet = MultiSet.ofCounts(
            'A' to 3,
            'B' to 2,
            'C' to 2,
            'D' to 1,
        )

        val differenceMultiSet = minuendMultiSet - subtrahendMultiSet

        assertEquals(
            expected = MultiSet.ofCounts(
                'A' to 2,
                'B' to 1,
                'C' to 4,
            ),
            actual = differenceMultiSet,
        )
    }

    @Test
    fun test_minus_coerced() {
        val minuendMultiSet = MultiSet.ofCounts(
            'A' to 5,
            'B' to 3,
            'C' to 6,
            'D' to 1,
        )

        val subtrahendMultiSet = MultiSet.ofCounts(
            'A' to 6,
            'B' to 2,
            'C' to 2,
            'D' to 3,
        )

        val differenceMultiSet = minuendMultiSet - subtrahendMultiSet

        assertEquals(
            expected = MultiSet.ofCounts(
                'B' to 1,
                'C' to 4,
            ),
            actual = differenceMultiSet,
        )
    }

    @Test
    fun test_containsWhole_superSet() {
        val superMultiSet = MultiSet.ofCounts(
            1 to 4,
            2 to 2,
            3 to 5,
        )

        val subMultiSet = MultiSet.ofCounts(
            1 to 3,
            2 to 2,
            3 to 1,
        )

        assertTrue(
            superMultiSet.containsWhole(subMultiSet)
        )

        assertFalse(
            subMultiSet.containsWhole(superMultiSet),
        )
    }

    @Test
    fun test_containsWhole_same() {
        val multiSet = MultiSet.ofCounts(
            1 to 4,
            2 to 2,
            3 to 5,
        )

        assertTrue(
            multiSet.containsWhole(multiSet),
        )

        assertTrue(
            multiSet.containsWhole(multiSet)
        )
    }
}
