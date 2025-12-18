package dev.kmpx.collections.internal.data_structures

import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("ClassName")
class DataStructuresUtils_tests {
    @Test
    fun test_getAnswer() {
        assertEquals(
            expected = "data_structures",
            actual = DataStructuresUtils.getString(),
        )
    }
}
