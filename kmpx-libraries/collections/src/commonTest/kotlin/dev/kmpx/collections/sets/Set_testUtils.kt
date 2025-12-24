package dev.kmpx.collections.sets

import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Verifies the consistency of a [Set] against expected elements and control elements.
 */
fun <E> Set<E>.verifyContent(
    /**
     * Expected elements in the set (in the iteration order).
     */
    expectedElements: Set<E>,
    /**
     * Control elements that should not be present in the set.
     */
    unexpectedElements: Set<E>,
) {
    // Verify `size`

    assertEquals(
        expected = expectedElements.size,
        actual = size,
        message = "Actual size does not match expected size: expected ${expectedElements.size}, got $size",
    )

    // Verify iterable content

    val expectedCountByElement = expectedElements.associateWith { 1 }
    val actualCountByElement = groupBy { it }.mapValues { (_, duplicates) -> duplicates.size }

    assertEquals(
        expected = expectedCountByElement,
        actual = actualCountByElement,
        message = "Actual elements do not match expected elements",
    )

    // Verify `contains`

    assertTrue(
        actual = expectedElements.all { this.contains(it) },
    )

    assertTrue(
        actual = unexpectedElements.none { actualCountByElement.contains(it) },
    )

    assertTrue(
        actual = unexpectedElements.none { contains(it) },
    )

    // Verify `equals` / `hashCode`

    assertEquals(
        expected = expectedElements,
        actual = this,
        message = "Actual set does not equal expected set",
    )

    val expectedHashCode = expectedElements.sumOf { it.hashCode() }

    assertEquals(
        expected = expectedHashCode,
        actual = this.hashCode(),
        message = "Actual set hashCode does not match expected set hashCode",
    )
}
