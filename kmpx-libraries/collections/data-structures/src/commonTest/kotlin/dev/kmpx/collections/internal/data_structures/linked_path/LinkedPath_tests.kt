package dev.kmpx.collections.internal.data_structures.linked_path

import dev.kmpx.collections.internal.data_structures.linked_path.test_utils.verifyIntegrity
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@Suppress("ClassName")
class LinkedPath_tests {
    /**
     * Test initial state of a newly created [LinkedPath].
     */
    @Test
    fun test_initial() {
        val linkedPath = LinkedPath.create<Int>()

        assertNull(
            actual = linkedPath.headNode,
        )

        assertNull(
            actual = linkedPath.tailNode,
        )
    }

    /**
     * Test prepending to an empty [LinkedPath].
     */
    @Test
    fun test_prepend_empty() {
        val linkedPath = LinkedPath.create<Int>()

        val newNode = linkedPath.prepend(10)

        assertEquals(
            expected = linkedPath.headNode,
            actual = newNode,
        )

        assertEquals(
            expected = linkedPath.tailNode,
            actual = newNode,
        )

        val payloads = linkedPath.verifyIntegrity()

        assertEquals(
            expected = listOf(10),
            actual = payloads,
        )
    }

    /**
     * Test prepending to a non-empty [LinkedPath] containing a single element.
     */
    @Test
    fun test_prepend_nonEmpty_singleElement() {
        val linkedPath = LinkedPath.createFilled(20)

        val newNode = linkedPath.prepend(10)

        assertEquals(
            expected = linkedPath.headNode,
            actual = newNode,
        )

        val payloads = linkedPath.verifyIntegrity()

        assertEquals(
            expected = listOf(10, 20),
            actual = payloads,
        )
    }

    /**
     * Test prepending to a non-empty [LinkedPath] containing multiple elements.
     */
    @Test
    fun test_prepend_nonEmpty_multipleElements() {
        val linkedPath = LinkedPath.createFilled(20, 30, 40)

        val newNode = linkedPath.prepend(10)

        assertEquals(
            expected = linkedPath.headNode,
            actual = newNode,
        )

        val payloads = linkedPath.verifyIntegrity()

        assertEquals(
            expected = listOf(10, 20, 30, 40),
            actual = payloads,
        )
    }

    /**
     * Test appending to an empty [LinkedPath].
     */
    @Test
    fun test_append_empty() {
        val linkedPath = LinkedPath.create<Int>()

        val newNode = linkedPath.append(10)

        assertEquals(
            expected = linkedPath.headNode,
            actual = newNode,
        )

        assertEquals(
            expected = linkedPath.tailNode,
            actual = newNode,
        )

        val payloads = linkedPath.verifyIntegrity()

        assertEquals(
            expected = listOf(10),
            actual = payloads,
        )
    }

    /**
     * Test appending to a non-empty [LinkedPath] containing a single element.
     */
    @Test
    fun test_append_nonEmpty_singleElement() {
        val linkedPath = LinkedPath.createFilled(10)

        val newNode = linkedPath.append(20)

        assertEquals(
            expected = linkedPath.tailNode,
            actual = newNode,
        )

        val payloads = linkedPath.verifyIntegrity()

        assertEquals(
            expected = listOf(10, 20),
            actual = payloads,
        )
    }

    /**
     * Test appending to a non-empty [LinkedPath] containing multiple elements.
     */
    @Test
    fun test_append_nonEmpty_multipleElements() {
        val linkedPath = LinkedPath.createFilled(10, 20, 30)

        val newNode = linkedPath.append(40)

        assertEquals(
            expected = linkedPath.tailNode,
            actual = newNode,
        )

        val payloads = linkedPath.verifyIntegrity()

        assertEquals(
            expected = listOf(10, 20, 30, 40),
            actual = payloads,
        )
    }

    /**
     * Test inserting before the single node in a [LinkedPath].
     */
    @Test
    fun test_inertBefore_singleElement() {
        val linkedPath = LinkedPath.createFilled(20)

        linkedPath.headNode!!.let { referenceNode ->
            linkedPath.insertBefore(
                referenceNode = referenceNode,
                payload = 10,
            )
        }

        val payloads = linkedPath.verifyIntegrity()

        assertEquals(
            expected = listOf(10, 20),
            actual = payloads,
        )
    }

    /**
     * Test inserting before the head node in a [LinkedPath] containing multiple elements.
     */
    @Test
    fun test_inertBefore_multipleElements_head() {
        val linkedPath = LinkedPath.createFilled(20, 30, 40)

        linkedPath.headNode!!.let { referenceNode ->
            linkedPath.insertBefore(
                referenceNode = referenceNode,
                payload = 10,
            )
        }

        val payloads = linkedPath.verifyIntegrity()

        assertEquals(
            expected = listOf(10, 20, 30, 40),
            actual = payloads,
        )
    }

    /**
     * Test inserting before the tail node in a [LinkedPath] containing multiple elements.
     */
    @Test
    fun test_inertBefore_multipleElements_tail() {
        val linkedPath = LinkedPath.createFilled(20, 30, 40)

        linkedPath.tailNode!!.let { referenceNode ->
            linkedPath.insertBefore(
                referenceNode = referenceNode,
                payload = 35,
            )
        }

        val payloads = linkedPath.verifyIntegrity()

        assertEquals(
            expected = listOf(20, 30, 35, 40),
            actual = payloads,
        )
    }

    /**
     * Test inserting before a middle node in a [LinkedPath] containing multiple elements.
     */
    @Test
    fun test_insertBefore_multipleElements_middle() {
        val linkedPath = LinkedPath.createFilled(20, 30, 40)

        // Insert before the middle node (30)
        val middleNode = linkedPath.headNode!!.nextNode!!

        linkedPath.insertBefore(
            referenceNode = middleNode,
            payload = 25,
        )

        val payloads = linkedPath.verifyIntegrity()

        assertEquals(
            expected = listOf(20, 25, 30, 40),
            actual = payloads,
        )
    }

    /**
     * Test inserting after the single node in a [LinkedPath] containing a single element.
     */
    @Test
    fun test_insertAfter_singleElement() {
        val linkedPath = LinkedPath.createFilled(10)

        linkedPath.headNode!!.let { referenceNode ->
            linkedPath.insertAfter(
                referenceNode = referenceNode,
                payload = 20,
            )
        }

        val payloads = linkedPath.verifyIntegrity()

        assertEquals(
            expected = listOf(10, 20),
            actual = payloads,
        )
    }

    /**
     * Test inserting after the head node in a [LinkedPath] containing multiple elements.
     */
    @Test
    fun test_insertAfter_multipleElements_head() {
        val linkedPath = LinkedPath.createFilled(10, 20, 30)

        linkedPath.headNode!!.let { referenceNode ->
            linkedPath.insertAfter(
                referenceNode = referenceNode,
                payload = 15,
            )
        }

        val payloads = linkedPath.verifyIntegrity()

        assertEquals(
            expected = listOf(10, 15, 20, 30),
            actual = payloads,
        )
    }

    /**
     * Test inserting after the tail node in a [LinkedPath] containing multiple elements.
     */
    @Test
    fun test_insertAfter_multipleElements_tail() {
        val linkedPath = LinkedPath.createFilled(10, 20, 30)

        linkedPath.tailNode!!.let { referenceNode ->
            linkedPath.insertAfter(
                referenceNode = referenceNode,
                payload = 40,
            )
        }

        val payloads = linkedPath.verifyIntegrity()

        assertEquals(
            expected = listOf(10, 20, 30, 40),
            actual = payloads,
        )
    }

    /**
     * Test inserting after a middle node in a [LinkedPath] containing multiple elements.
     */
    @Test
    fun test_insertAfter_multipleElements_middle() {
        val linkedPath = LinkedPath.createFilled(10, 20, 30)

        // Insert after the middle node (20)
        val middleNode = linkedPath.headNode!!.nextNode!!

        linkedPath.insertAfter(
            referenceNode = middleNode,
            payload = 25,
        )

        val payloads = linkedPath.verifyIntegrity()

        assertEquals(
            expected = listOf(10, 20, 25, 30),
            actual = payloads,
        )
    }

    /**
     * Test cutting off the single node in a [LinkedPath].
     */
    @Test
    fun test_cutOff_singleElement() {
        val linkedPath = LinkedPath.createFilled(10)

        val nodeToCutOff = linkedPath.headNode!!

        linkedPath.cutOff(
            node = nodeToCutOff,
        )

        val payloads = linkedPath.verifyIntegrity()

        assertEquals(
            expected = emptyList(),
            actual = payloads,
        )
    }

    /**
     * Test cutting off the head node in a [LinkedPath] containing multiple elements.
     */
    @Test
    fun test_cutOff_multipleElements_head() {
        val linkedPath = LinkedPath.createFilled(10, 20, 30)

        val nodeToCutOff = linkedPath.headNode!!

        linkedPath.cutOff(
            node = nodeToCutOff,
        )

        val payloads = linkedPath.verifyIntegrity()

        assertEquals(
            expected = listOf(20, 30),
            actual = payloads,
        )
    }

    /**
     * Test cutting off the tail node in a [LinkedPath] containing multiple elements.
     */
    @Test
    fun test_cutOff_multipleElements_tail() {
        val linkedPath = LinkedPath.createFilled(10, 20, 30)

        val nodeToCutOff = linkedPath.tailNode!!

        linkedPath.cutOff(
            node = nodeToCutOff,
        )

        val payloads = linkedPath.verifyIntegrity()

        assertEquals(
            expected = listOf(10, 20),
            actual = payloads,
        )
    }

    /**
     * Test cutting off a middle node in a [LinkedPath] containing multiple elements.
     */
    @Test
    fun test_cutOff_multipleElements_middle() {
        val linkedPath = LinkedPath.createFilled(10, 20, 30)

        // Cut off the middle node (20)

        val middleNode = linkedPath.headNode!!.nextNode!!

        linkedPath.cutOff(
            node = middleNode,
        )

        val payloads = linkedPath.verifyIntegrity()

        assertEquals(
            expected = listOf(10, 30),
            actual = payloads,
        )
    }
}

private fun <PayloadT> LinkedPath.Companion.createFilled(
    vararg payloads: PayloadT,
): LinkedPath<PayloadT> {
    val linkedPath = LinkedPath.create<PayloadT>()

    for (payload in payloads) {
        linkedPath.append(payload)
    }

    return linkedPath
}
