package dev.kmpx.collections.multi_sets

import dev.kmpx.collections.sets.verifyContent
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private data class EntryImpl<out E>(
    override val element: E,
    override val count: Int,
) : AbstractMutableMultiSet.AbstractEntry<E>()

/**
 * Verifies the consistency of a [MultiSet] against expected elements and control elements.
 */
fun <E : Comparable<E>> MultiSet<E>.verifyContent(
    /**
     * Expected elements in the multi-set (in any order).
     */
    expectedCountByElement: Map<E, Int>,
    /**
     * Control elements that should not be present in the multi-set.
     */
    unexpectedElements: Set<E>,
) {
    // Verify `size`

    val expectedSize = expectedCountByElement.values.sum()

    assertEquals(
        expected = expectedSize,
        actual = size,
        message = "Actual size does not match expected size",
    )

    // Verify iterable content

    val actualCountByElementFromIterator: Map<E, Int> = this.toList().getCountByElement()

    assertEquals(
        expected = expectedCountByElement,
        actual = actualCountByElementFromIterator,
        message = "Actual count by element does not match expected count by element",
    )

    // Verify `getCount`

    expectedCountByElement.forEach { (element, expectedCount) ->
        val actualCount = this.getCount(element)

        assertEquals(
            expected = expectedCount,
            actual = actualCount,
            message = "Actual count for element '$element' does not match expected count",
        )
    }

    // Verify `contains`

    assertTrue(
        actual = expectedCountByElement.all { (element, _) -> this.contains(element) },
    )

    assertTrue(
        actual = unexpectedElements.none { actualCountByElementFromIterator.containsKey(it) },
    )

    assertTrue(
        actual = unexpectedElements.none { this.contains(it) },
    )

    // Verify `entrySet`

    entrySet.verifyContent(
        expectedElements = expectedCountByElement.map {
            EntryImpl(
                element = it.key,
                count = it.value,
            )
        }.toSet(),
        unexpectedElements = emptySet(),
    )

    // Verify `equals` / `hashCode`

    val expectedMultiSet = MultiSet.ofCounts(
        *expectedCountByElement.map {
            (it.key to it.value)
        }.toTypedArray(),
    )

    assertEquals(
        expected = expectedMultiSet,
        actual = this,
        message = "MultiSet equality check failed",
    )

    assertEquals(
        expected = expectedMultiSet.hashCode(),
        actual = this.hashCode(),
        message = "MultiSet hashCode check failed",
    )
}

private fun <E> Iterable<E>.getCountByElement(): Map<E, Int> = this.groupBy { it }.mapValues { (_, list) -> list.size }
