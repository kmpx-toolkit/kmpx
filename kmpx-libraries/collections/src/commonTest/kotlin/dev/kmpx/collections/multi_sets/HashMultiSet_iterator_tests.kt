package dev.kmpx.collections.multi_sets

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@Suppress("ClassName")
class HashMultiSet_iterator_tests {
    @Test
    fun test_empty() {
        val multiSet = hashMultiSetOf<Int>()

        assertIs<NoSuchElementException>(
            assertFails {
                multiSet.iterator().next()
            },
        )
    }

    @Test
    fun test_nonEmpty() {
        val multiSet = hashMultiSetOf(10, 10, 20, 20, 20, 30)

        val iterator = multiSet.iterator()

        val nextElements = List(size = 6) {
            assertTrue(
                actual = iterator.hasNext()
            )

            iterator.next()
        }

        assertEquals(
            expected = listOf(10, 10, 20, 20, 20, 30),
            actual = nextElements.sorted(),
        )

        assertFalse(
            iterator.hasNext(),
        )

        assertIs<NoSuchElementException>(
            assertFails {
                iterator.next()
            },
        )
    }

    @Test
    fun test_remove_oneInstance() {
        val multiSet = hashMultiSetOf(10, 10, 20, 30)

        val iterator = multiSet.iterator()

        while (iterator.hasNext()) {
            val nextElement = iterator.next()

            if (nextElement == 10) {
                iterator.remove()
                break
            }
        }

        multiSet.verifyContent(
            expectedCountByElement = mapOf(
                10 to 1,
                20 to 1,
                30 to 1,
            ),
            unexpectedElements = setOf(-10),
        )
    }

    @Test
    fun test_remove_onlyInstance() {
        val multiSet = hashMultiSetOf(10, 10, 20, 30)

        val iterator = multiSet.iterator()

        while (iterator.hasNext()) {
            val nextElement = iterator.next()

            if (nextElement == 20) {
                iterator.remove()
                break
            }
        }

        multiSet.verifyContent(
            expectedCountByElement = mapOf(
                10 to 2,
                30 to 1,
            ),
            unexpectedElements = setOf(-10),
        )
    }

    @Test
    fun test_remove_allInstances() {
        val multiSet = hashMultiSetOf(10, 10, 10, 20, 20, 30)

        val iterator = multiSet.iterator()

        while (iterator.hasNext()) {
            val nextElement = iterator.next()

            if (nextElement == 10) {
                iterator.remove()
            }
        }

        multiSet.verifyContent(
            expectedCountByElement = mapOf(
                20 to 2,
                30 to 1,
            ),
            unexpectedElements = setOf(10),
        )
    }

    @Test
    fun test_remove_allElements() {
        val multiSet = hashMultiSetOf(10, 10, 10, 20, 20, 30)

        val iterator = multiSet.iterator()

        while (iterator.hasNext()) {
            iterator.next()
            iterator.remove()
        }

        multiSet.verifyContent(
            expectedCountByElement = emptyMap(),
            unexpectedElements = setOf(10),
        )
    }

    @Test
    fun test_next_concurrentRemove() {
        val multiSet = hashMultiSetOf(10, 10, 20, 20, 30)

        val iterator = multiSet.iterator()

        while (iterator.hasNext()) {
            val nextElement = iterator.next()

            if (nextElement == 20) {
                break
            }
        }

        multiSet.remove(20)
        multiSet.remove(20)

        assertIs<ConcurrentModificationException>(
            assertFails {
                iterator.next()
            },
        )

        multiSet.verifyContent(
            expectedCountByElement = mapOf(
                10 to 2,
                30 to 1,
            ),
            unexpectedElements = setOf(-10),
        )
    }

    @Test
    fun test_remove_concurrentRemove() {
        val multiSet = hashMultiSetOf(10, 10, 20, 30)

        val iterator = multiSet.iterator()

        while (iterator.hasNext()) {
            val nextElement = iterator.next()

            if (nextElement == 20) {
                break
            }
        }

        multiSet.remove(20)

        assertIs<ConcurrentModificationException>(
            assertFails {
                iterator.remove()
            },
        )

        multiSet.verifyContent(
            expectedCountByElement = mapOf(
                10 to 2,
                30 to 1,
            ),
            unexpectedElements = setOf(-10),
        )
    }
}
