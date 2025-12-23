package dev.kmpx.collections.sets

import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertIs

@Suppress("ClassName")
class TreeSet_incorrectIteration_tests {
    @Test
    fun test_iterator_next_noNextElement() {
        val elements = arrayOf(30, 20, 10, 40)

        val mutableSet = treeSetOf(*elements)

        val iterator = mutableSet.iterator()

        iterator.next()
        iterator.next()
        iterator.next()
        iterator.next()

        assertIs<NoSuchElementException>(
            value = assertFails {
                iterator.next()
            },
        )

        mutableSet.verifyContent(
            expectedElements = elements.toSet(),
            unexpectedElements = setOf(-10),
        )
    }

    @Test
    fun test_iterator_remove_beforeNext() {
        val elements = arrayOf(30, 20, 10, 40)

        val mutableSet = treeSetOf(*elements)

        val iterator = mutableSet.iterator()

        assertIs<IllegalStateException>(
            value = assertFails {
                iterator.remove()
            },
        )

        mutableSet.verifyContent(
            expectedElements = elements.toSet(),
            unexpectedElements = setOf(-10),
        )
    }

    @Test
    fun test_iterator_remove_alreadyCalled() {
        val elements = arrayOf(30, 20, 10, 40)

        val mutableSet = treeSetOf(*elements)

        val iterator = mutableSet.iterator()

        iterator.next()
        iterator.next()

        // First removal (element: 20)
        iterator.remove()

        mutableSet.verifyContent(
            expectedElements = setOf(10, 30, 40),
            unexpectedElements = setOf(20, -10),
        )
    }
}
