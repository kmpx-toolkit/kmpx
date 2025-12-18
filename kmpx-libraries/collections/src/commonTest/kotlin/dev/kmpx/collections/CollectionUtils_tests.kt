package dev.kmpx.collections

import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("ClassName")
class CollectionUtils_tests {
    @Test
    fun test_getAnswer() {
        assertEquals(
            expected = "collections",
            actual = CollectionUtils.getString(),
        )
    }
}
