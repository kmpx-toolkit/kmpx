package dev.kmpx.collections.lists

import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertIs
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
    verifyIntegrity()

    assertEquals(
        expected = elements,
        actual = this,
    )
}

fun <E> List<E>.verifyIntegrity() {
    val iterator = this.iterator()
    var index = 0
    val iteratedElements = mutableListOf<E>()
    val iteratedFirstIndexByElement = mutableMapOf<E, Int>()

    while (iterator.hasNext()) {
        val iteratedElement = iterator.next()

        iteratedElements.add(iteratedElement)

        val gotElement = this[index]

        assertEquals(
            expected = iteratedElement,
            actual = gotElement,
            message = "Inconsistency between iterator and get() at index $index: iterator returned $iteratedElement, get() returned $gotElement",
        )

        val isContained = this.contains(iteratedElement)

        assertTrue(
            actual = isContained,
            message = "Inconsistency in contains() for element $iteratedElement at index $index: contains() returned false",
        )

        val iteratedFirstIndex = iteratedFirstIndexByElement.getOrPut(iteratedElement) { index }
        val foundFirstIndex = this.indexOf(iteratedElement)

        assertEquals(
            expected = iteratedFirstIndex,
            actual = foundFirstIndex,
            message = "Inconsistency in indexOf() for element $iteratedElement at index $index: expected first index $iteratedFirstIndex, got $foundFirstIndex",
        )

        ++index
    }

    val iteratedSize = index

    assertIs<IndexOutOfBoundsException>(
        value = assertFails {
            this[iteratedSize]
        },
        message = "Inconsistency in get() bounds checking: accessing index $iteratedSize did not throw IndexOutOfBoundsException",
    )

    assertEquals(
        expected = iteratedSize,
        actual = size,
        message = "Inconsistency between iterated size and reported size: iterated $iteratedSize, reported $size",
    )

    when {
        iteratedSize == 0 -> {
            assertTrue(
                actual = isEmpty(),
                message = "List is empty but isEmpty() returned false",
            )
        }

        else -> {
            assertFalse(
                actual = isEmpty(),
                message = "List is not empty but isEmpty() returned true",
            )
        }
    }

    assertEquals(
        expected = iteratedElements,
        actual = this,
        message = "Inconsistency in equals() between list and iterated elements",
    )
}
