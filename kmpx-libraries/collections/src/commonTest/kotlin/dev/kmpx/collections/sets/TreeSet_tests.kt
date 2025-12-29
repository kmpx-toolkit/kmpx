package dev.kmpx.collections.sets

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("ClassName")
class TreeSet_tests {
    @Test
    fun test_initial() {
        val set = treeSetOf<Int>()

        set.verifyContent(
            expectedElements = emptySet(),
            unexpectedElements = setOf(10, 20, 30),
        )
    }

    @Test
    fun test_lookup() {
        val set = treeSetOf(
            10, 20, 30, 40,
        )

        val handle20 = assertNotNull(
            actual = set.lookup(20),
        )

        assertEquals(
            expected = 20,
            actual = set.getVia(handle = handle20),
        )
    }

    @Test
    fun test_add_empty() {
        val set = treeSetOf<Int>()

        assertTrue(
            actual = set.add(10),
        )

        set.verifyContent(
            expectedElements = setOf(10),
            unexpectedElements = setOf(20, 30),
        )
    }

    @Test
    fun test_add_nonEmpty() {
        val set = treeSetOf<Int>()

        set.addAll(
            listOf(
                10,
                20,
                30,
            ),
        )

        assertTrue(
            actual = set.add(15),
        )

        set.verifyContent(
            expectedElements = setOf(10, 15, 20, 30),
            unexpectedElements = setOf(-10, 40, 50),
        )
    }

    @Test
    fun test_insert_duplicate() {
        val set = treeSetOf(
            10, 15, 20, 30,
        )

        assertNull(
            actual = set.insert(
                element = 20,
            ),
        )

        set.verifyContent(
            expectedElements = setOf(10, 15, 20, 30),
            unexpectedElements = setOf(-10, 40, 50),
        )
    }

    @Test
    fun test_insert_nonDuplicate() {
        val set = treeSetOf(
            10, 15, 20, 30,
        )

        val handle = assertNotNull(
            actual = set.insert(
                element = 25,
            ),
        )

        assertEquals(
            expected = 25,
            actual = set.getVia(handle = handle),
        )

        set.verifyContent(
            expectedElements = setOf(10, 15, 20, 25, 30),
            unexpectedElements = setOf(-10, 40, 50),
        )
    }

    @Test
    fun test_remove() {
        val set = treeSetOf<Int>()

        set.addAll(
            listOf(
                10,
                20,
                30,
            ),
        )

        assertTrue(
            actual = set.remove(20),
        )

        set.verifyContent(
            expectedElements = setOf(10, 30),
            unexpectedElements = setOf(20, 40, 50),
        )

        assertTrue(
            actual = set.remove(10),
        )

        set.verifyContent(
            expectedElements = setOf(30),
            unexpectedElements = setOf(10, 20, 40, 50),
        )

        assertTrue(
            actual = set.remove(30),
        )

        set.verifyContent(
            expectedElements = emptySet(),
            unexpectedElements = setOf(10, 20, 30, 40, 50),
        )
    }

    @Test
    fun test_removeVia() {
        val set = treeSetOf(
            10, 15, 20, 30,
        )

        val handle15 = assertNotNull(
            actual = set.lookup(element = 15),
        )

        assertEquals(
            expected = 15,
            actual = set.removeVia(handle = handle15),
        )

        assertNull(
            actual = set.getVia(handle = handle15),
        )

        assertNull(
            actual = set.removeVia(handle = handle15),
        )

        set.verifyContent(
            expectedElements = setOf(10, 20, 30),
            unexpectedElements = setOf(15, -20),
        )
    }

    @Test
    fun test_remove_notContained() {
        val set = treeSetOf<Int>()

        set.addAll(
            listOf(
                10,
                20,
                30,
            ),
        )

        assertFalse(
            actual = set.remove(40),
        )

        set.verifyContent(
            expectedElements = setOf(10, 20, 30),
            unexpectedElements = setOf(40, 50),
        )
    }
}
