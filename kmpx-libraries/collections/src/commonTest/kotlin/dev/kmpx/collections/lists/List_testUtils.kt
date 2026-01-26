package dev.kmpx.collections.lists

import kotlin.test.assertEquals

fun <E> List<E>.verifyContent(
    expectedElements: List<E>,
) {
    // Verify that the list has the expected size

    assertEquals(
        expected = expectedElements.size,
        actual = size,
        message = "Size does not match expected size",
    )

    // Verify that the list contains exactly the expected elements

    assertEquals(
        expected = expectedElements,
        actual = this,
        message = "Actual list elements does not match expected list elements",
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
