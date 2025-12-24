package dev.kmpx.collections.lists

import kotlin.test.assertEquals
import kotlin.test.assertTrue

fun <E> List<E>.verifyContent(
    vararg elements: E,
) {
    verifyContent(
        elements = elements.toList(),
    )
}

fun <E> List<E>.verifyContent(
    elements: List<E>,
) {
    assertEquals(
        expected = elements.size,
        actual = size,
        message = "Actual size does not match expected size: expected ${elements.size}, got $size",
    )

    elements.forEachIndexed { index, element ->
        val actualElement = this[index]

        assertEquals(
            expected = element,
            actual = actualElement,
            message = "Actual element at index $index does not match expected element: expected $element, got $actualElement",
        )

        assertTrue(
            actual = contains(element),
            message = "List does not contain expected element: $element",
        )
    }
}
