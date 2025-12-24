package dev.kmpx.collections.lists

import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

object ListTestUtils {
    fun <E> verifyIntegrity(list: List<E>) {
        val iterator = list.iterator()
        var index = 0
        val iteratedElements = mutableListOf<E>()
        val iteratedFirstIndexByElement = mutableMapOf<E, Int>()

        while (iterator.hasNext()) {
            val iteratedElement = iterator.next()

            iteratedElements.add(iteratedElement)

            val gotElement = list[index]

            assertEquals(
                expected = iteratedElement,
                actual = gotElement,
                message = "Inconsistency between iterator and get() at index $index: iterator returned $iteratedElement, get() returned $gotElement",
            )

            val isContained = list.contains(iteratedElement)

            assertTrue(
                actual = isContained,
                message = "Inconsistency in contains() for element $iteratedElement at index $index: contains() returned false",
            )

            val iteratedFirstIndex = iteratedFirstIndexByElement.getOrPut(iteratedElement) { index }
            val foundFirstIndex = list.indexOf(iteratedElement)

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
                list[iteratedSize]
            },
            message = "Inconsistency in get() bounds checking: accessing index $iteratedSize did not throw IndexOutOfBoundsException",
        )

        assertEquals(
            expected = iteratedSize,
            actual = list.size,
            message = "Inconsistency between iterated size and reported size: iterated $iteratedSize, reported ${list.size}",
        )

        when {
            iteratedSize == 0 -> {
                assertTrue(
                    actual = list.isEmpty(),
                    message = "List is empty but isEmpty() returned false",
                )
            }

            else -> {
                assertFalse(
                    actual = list.isEmpty(),
                    message = "List is not empty but isEmpty() returned true",
                )
            }
        }

        assertEquals(
            expected = iteratedElements,
            actual = list,
            message = "Inconsistency in equals() between list and iterated elements",
        )
    }
}