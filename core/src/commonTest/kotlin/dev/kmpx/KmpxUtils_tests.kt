package dev.kmpx

import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("ClassName")
class KmpxUtils_tests {
    @Test
    fun test_getAnswer() {
        assertEquals(
            expected = 42,
            actual = KmpxUtils.getAnswer(),
        )
    }
}
