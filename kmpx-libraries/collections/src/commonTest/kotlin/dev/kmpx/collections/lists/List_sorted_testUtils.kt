package dev.kmpx.collections.lists

import dev.kmpx.collections.multi_sets.toMultiSet
import kotlin.test.assertEquals
import kotlin.test.fail

fun <E> List<E>.verifyContentSorted(
    comparator: Comparator<E>,
    expectedElements: Collection<E>,
) {
    // Verify that the list is indeed sorted

    zipWithNext().forEach { (previousElement, nextElement) ->
        if (comparator.compare(previousElement, nextElement) > 0) {
            fail("List is not properly sorted: element $previousElement is greater than $nextElement")
        }
    }

    // Verify that the list has the expected size

    assertEquals(
        expected = expectedElements.size,
        actual = size,
        message = "Size does not match expected size",
    )

    // Verify that the list contains exactly the expected elements

    val actualElementsMultiSet = this.toMultiSet()
    val expectedElementsMultiSet = expectedElements.toMultiSet()

    assertEquals(
        expected = expectedElementsMultiSet,
        actual = actualElementsMultiSet,
        message = "Actual elements multi-set does not match expected elements multi-set",
    )

    // Verify the consistency between indexed access and iteration

    val iterator = this.iterator()
    var iterationCount = 0

    while (iterator.hasNext()) {
        val index = iterationCount++
        val iteratedElement = iterator.next()

        val gotElement = this[index]

        assertEquals(
            expected = gotElement,
            actual = iteratedElement,
            message = "Element retrieved at index $index does not match iterated element",
        )
    }

    assertEquals(
        expected = expectedElements.size,
        actual = iterationCount,
        message = "Number of iterated elements does not match expected size",
    )
}
