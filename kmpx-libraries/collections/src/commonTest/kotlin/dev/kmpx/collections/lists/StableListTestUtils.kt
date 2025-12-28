package dev.kmpx.collections.lists

import dev.kmpx.collections.StableCollection.Handle
import kotlin.test.assertEquals
import kotlin.test.assertNull

object StableListTestUtils {
    fun <E> verifyIntegrity(list: StableList<E>) {
        ListTestUtils.verifyIntegrity(list = list)

        var index = 0

        for ((iteratedHandle: Handle<E>, iteratedElement: E) in list.handles.zip(list.asSequence())) {
            val resolvedElement: E? = list.getVia(iteratedHandle)

            assertEquals(
                expected = iteratedElement,
                actual = resolvedElement,
                message = "Inconsistency in handle resolution for element $iteratedElement: handle resolved to $resolvedElement",
            )

            val gotHandle: Handle<E>? = list.getEx(index)

            assertEquals(
                expected = iteratedHandle,
                actual = gotHandle,
                message = "Inconsistency in getEx() for element $iteratedElement at index $index: expected handle $iteratedHandle, got $gotHandle",
            )

            val foundIndex: Int? = list.indexOfVia(iteratedHandle)

            assertEquals(
                expected = index,
                actual = foundIndex,
                message = "Inconsistency in indexOfVia() for element $iteratedElement at index $index: expected index $index, got $foundIndex",
            )

            ++index
        }

        val iteratedSize = index

        assertNull(
            actual = list.getEx(iteratedSize),
            message = "Inconsistency in getEx() bounds checking: accessing index $iteratedSize did not return null",
        )

        assertEquals(
            expected = iteratedSize,
            actual = list.size,
            message = "Inconsistency between iterated size and reported size: iterated $iteratedSize, reported ${list.size}",
        )
    }
}
