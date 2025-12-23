package dev.kmpx.collections.sets

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("ClassName")
class TreeSet_mutatingIteration_tests {
    @Test
    fun test_iterator_remove_some() {
        val evenElements = arrayOf(10, 20, 30, 34, 40)
        val oddElements = arrayOf(37, 13, 25, 41)

        val mutableSet = treeSetOf(*evenElements, *oddElements)

        val iterator = mutableSet.iterator()

        listOf(*evenElements, *oddElements).sorted().forEach { element ->
            assertTrue(
                actual = iterator.hasNext(),
            )

            assertEquals(
                expected = element,
                actual = iterator.next(),
            )

            // Remove odd elements
            if (element % 2 == 1) {
                iterator.remove()
            }
        }

        assertFalse(
            actual = iterator.hasNext(),
        )

        mutableSet.verifyContent(
            expectedElements = setOf(*evenElements),
            unexpectedElements = setOf(*oddElements, -10),
        )
    }

    @Test
    fun test_iterator_remove_all() {
        val elements = arrayOf(30, 20, 10, 40)

        val mutableSet = treeSetOf(*elements)

        val iterator = mutableSet.iterator()

        elements.sorted().forEach { element ->
            assertTrue(
                actual = iterator.hasNext(),
            )

            assertEquals(
                expected = element,
                actual = iterator.next(),
            )

            iterator.remove()
        }

        assertFalse(
            actual = iterator.hasNext(),
        )

        mutableSet.verifyContent(
            expectedElements = emptySet(),
            unexpectedElements = setOf(*elements, -10),
        )
    }
}
