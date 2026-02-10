package dev.kmpx.collections.maps

import kotlin.test.Test
import kotlin.test.assertEquals
import dev.kmpx.collections.SortedCollections.RankResult
import dev.kmpx.collections.SortedCollections.RankKind

@Suppress("ClassName")
class TreeMap_findKeyRank_tests {
    @Test
    fun test_findKeyRank_emptyMap() {
        val map = treeMapOf<Int, String>()

        val result = map.findKeyRank(key = 10)

        assertEquals(
            expected = RankResult(
                rank = 0,
                kind = RankKind.Potential,
            ),
            actual = result,
        )
    }

    @Test
    fun test_findKeyRank_existingKeys() {
        val map = treeMapOf(10 to "a", 20 to "b", 30 to "c", 40 to "d", 50 to "e")

        // Test each key has correct rank
        assertEquals(
            expected = RankResult(
                rank = 0,
                kind = RankKind.Existing,
            ),
            actual = map.findKeyRank(key = 10),
        )

        assertEquals(
            expected = RankResult(
                rank = 1,
                kind = RankKind.Existing,
            ),
            actual = map.findKeyRank(key = 20),
        )

        assertEquals(
            expected = RankResult(
                rank = 2,
                kind = RankKind.Existing,
            ),
            actual = map.findKeyRank(key = 30),
        )

        assertEquals(
            expected = RankResult(
                rank = 3,
                kind = RankKind.Existing,
            ),
            actual = map.findKeyRank(key = 40),
        )

        assertEquals(
            expected = RankResult(
                rank = 4,
                kind = RankKind.Existing,
            ),
            actual = map.findKeyRank(key = 50),
        )
    }

    @Test
    fun test_findKeyRank_potentialKeys_beforeAll() {
        val map = treeMapOf(10 to "a", 20 to "b", 30 to "c", 40 to "d", 50 to "e")

        val result = map.findKeyRank(key = 5)

        assertEquals(
            expected = RankResult(
                rank = 0,
                kind = RankKind.Potential,
            ),
            actual = result,
        )
    }

    @Test
    fun test_findKeyRank_potentialKeys_betweenKeys() {
        val map = treeMapOf(10 to "a", 20 to "b", 30 to "c", 40 to "d", 50 to "e")

        // Key 15 would be inserted at rank 1 (between 10 and 20)
        assertEquals(
            expected = RankResult(
                rank = 1,
                kind = RankKind.Potential,
            ),
            actual = map.findKeyRank(key = 15),
        )

        // Key 25 would be inserted at rank 2 (between 20 and 30)
        assertEquals(
            expected = RankResult(
                rank = 2,
                kind = RankKind.Potential,
            ),
            actual = map.findKeyRank(key = 25),
        )

        // Key 35 would be inserted at rank 3 (between 30 and 40)
        assertEquals(
            expected = RankResult(
                rank = 3,
                kind = RankKind.Potential,
            ),
            actual = map.findKeyRank(key = 35),
        )

        // Key 45 would be inserted at rank 4 (between 40 and 50)
        assertEquals(
            expected = RankResult(
                rank = 4,
                kind = RankKind.Potential,
            ),
            actual = map.findKeyRank(key = 45),
        )
    }

    @Test
    fun test_findKeyRank_potentialKeys_afterAll() {
        val map = treeMapOf(10 to "a", 20 to "b", 30 to "c", 40 to "d", 50 to "e")

        val result = map.findKeyRank(key = 55)

        assertEquals(
            expected = RankResult(
                rank = 5,
                kind = RankKind.Potential,
            ),
            actual = result,
        )
    }

    @Test
    fun test_findKeyRank_singleEntry_existing() {
        val map = treeMapOf(20 to "b")

        val result = map.findKeyRank(key = 20)

        assertEquals(
            expected = RankResult(
                rank = 0,
                kind = RankKind.Existing,
            ),
            actual = result,
        )
    }

    @Test
    fun test_findKeyRank_singleEntry_potential_before() {
        val map = treeMapOf(20 to "b")

        val result = map.findKeyRank(key = 10)

        assertEquals(
            expected = RankResult(
                rank = 0,
                kind = RankKind.Potential,
            ),
            actual = result,
        )
    }

    @Test
    fun test_findKeyRank_singleEntry_potential_after() {
        val map = treeMapOf(20 to "b")

        val result = map.findKeyRank(key = 30)

        assertEquals(
            expected = RankResult(
                rank = 1,
                kind = RankKind.Potential,
            ),
            actual = result,
        )
    }

    @Test
    fun test_findKeyRank_afterRemoval() {
        val map = treeMapOf(10 to "a", 20 to "b", 30 to "c", 40 to "d", 50 to "e")

        // Verify key 30 exists at rank 2
        assertEquals(
            expected = RankResult(
                rank = 2,
                kind = RankKind.Existing,
            ),
            actual = map.findKeyRank(key = 30),
        )

        // Remove entry with key 30
        map.remove(key = 30)

        // Verify key 30 is now potential at rank 2
        assertEquals(
            expected = RankResult(
                rank = 2,
                kind = RankKind.Potential,
            ),
            actual = map.findKeyRank(key = 30),
        )

        // Verify other keys have shifted ranks
        assertEquals(
            expected = RankResult(
                rank = 2,
                kind = RankKind.Existing,
            ),
            actual = map.findKeyRank(key = 40),
        )

        assertEquals(
            expected = RankResult(
                rank = 3,
                kind = RankKind.Existing,
            ),
            actual = map.findKeyRank(key = 50),
        )
    }

    @Test
    fun test_findKeyRank_afterInsertion() {
        val map = treeMapOf(10 to "a", 20 to "b", 40 to "d", 50 to "e")

        // Verify key 30 would be at rank 2
        assertEquals(
            expected = RankResult(
                rank = 2,
                kind = RankKind.Potential,
            ),
            actual = map.findKeyRank(key = 30),
        )

        // Insert entry with key 30
        map[30] = "c"

        // Verify key 30 now exists at rank 2
        assertEquals(
            expected = RankResult(
                rank = 2,
                kind = RankKind.Existing,
            ),
            actual = map.findKeyRank(key = 30),
        )

        // Verify other keys have shifted ranks
        assertEquals(
            expected = RankResult(
                rank = 3,
                kind = RankKind.Existing,
            ),
            actual = map.findKeyRank(key = 40),
        )

        assertEquals(
            expected = RankResult(
                rank = 4,
                kind = RankKind.Existing,
            ),
            actual = map.findKeyRank(key = 50),
        )
    }

    @Test
    fun test_findKeyRank_afterValueUpdate() {
        val map = treeMapOf(10 to "a", 20 to "b", 30 to "c")

        // Update value for key 20
        map[20] = "updated"

        // Verify rank stays the same after value update
        assertEquals(
            expected = RankResult(
                rank = 1,
                kind = RankKind.Existing,
            ),
            actual = map.findKeyRank(key = 20),
        )
    }

    @Test
    fun test_findKeyRank_twoEntries() {
        val map = treeMapOf(10 to "a", 20 to "b")

        assertEquals(
            expected = RankResult(
                rank = 0,
                kind = RankKind.Existing,
            ),
            actual = map.findKeyRank(key = 10),
        )

        assertEquals(
            expected = RankResult(
                rank = 1,
                kind = RankKind.Existing,
            ),
            actual = map.findKeyRank(key = 20),
        )

        assertEquals(
            expected = RankResult(
                rank = 0,
                kind = RankKind.Potential,
            ),
            actual = map.findKeyRank(key = 5),
        )

        assertEquals(
            expected = RankResult(
                rank = 1,
                kind = RankKind.Potential,
            ),
            actual = map.findKeyRank(key = 15),
        )

        assertEquals(
            expected = RankResult(
                rank = 2,
                kind = RankKind.Potential,
            ),
            actual = map.findKeyRank(key = 25),
        )
    }

    @Test
    fun test_findKeyRank_largeMap() {
        // Test with a larger map to verify correctness with more complex tree structure
        val keys = listOf(5, 15, 25, 35, 45, 55, 65, 75, 85, 95)
        val map = treeMapOf(*keys.map { it to "v$it" }.toTypedArray())

        // Verify all existing keys have correct ranks
        keys.forEachIndexed { index, key ->
            assertEquals(
                expected = RankResult(
                    rank = index,
                    kind = RankKind.Existing,
                ),
                actual = map.findKeyRank(key = key),
                message = "Failed for key $key at expected rank $index",
            )
        }

        // Verify potential keys between existing ones
        assertEquals(
            expected = RankResult(
                rank = 0,
                kind = RankKind.Potential,
            ),
            actual = map.findKeyRank(key = 1),
        )

        assertEquals(
            expected = RankResult(
                rank = 5,
                kind = RankKind.Potential,
            ),
            actual = map.findKeyRank(key = 50),
        )

        assertEquals(
            expected = RankResult(
                rank = 10,
                kind = RankKind.Potential,
            ),
            actual = map.findKeyRank(key = 100),
        )
    }

    @Test
    fun test_findKeyRank_unsortedInsertionOrder() {
        val map = treeMapOf<Int, String>()

        // Insert in non-sorted order
        map[30] = "c"
        map[10] = "a"
        map[50] = "e"
        map[20] = "b"
        map[40] = "d"

        // Verify ranks are based on sorted key order, not insertion order
        assertEquals(
            expected = RankResult(
                rank = 0,
                kind = RankKind.Existing,
            ),
            actual = map.findKeyRank(key = 10),
        )

        assertEquals(
            expected = RankResult(
                rank = 1,
                kind = RankKind.Existing,
            ),
            actual = map.findKeyRank(key = 20),
        )

        assertEquals(
            expected = RankResult(
                rank = 2,
                kind = RankKind.Existing,
            ),
            actual = map.findKeyRank(key = 30),
        )

        assertEquals(
            expected = RankResult(
                rank = 3,
                kind = RankKind.Existing,
            ),
            actual = map.findKeyRank(key = 40),
        )

        assertEquals(
            expected = RankResult(
                rank = 4,
                kind = RankKind.Existing,
            ),
            actual = map.findKeyRank(key = 50),
        )
    }

    @Test
    fun test_findKeyRank_multipleRemovals() {
        val map = treeMapOf(10 to "a", 20 to "b", 30 to "c", 40 to "d", 50 to "e")

        // Remove multiple entries
        map.remove(key = 20)
        map.remove(key = 40)

        // Verify existing keys have correct ranks
        assertEquals(
            expected = RankResult(
                rank = 0,
                kind = RankKind.Existing,
            ),
            actual = map.findKeyRank(key = 10),
        )

        assertEquals(
            expected = RankResult(
                rank = 1,
                kind = RankKind.Existing,
            ),
            actual = map.findKeyRank(key = 30),
        )

        assertEquals(
            expected = RankResult(
                rank = 2,
                kind = RankKind.Existing,
            ),
            actual = map.findKeyRank(key = 50),
        )

        // Verify removed keys are now potential
        assertEquals(
            expected = RankResult(
                rank = 1,
                kind = RankKind.Potential,
            ),
            actual = map.findKeyRank(key = 20),
        )

        assertEquals(
            expected = RankResult(
                rank = 2,
                kind = RankKind.Potential,
            ),
            actual = map.findKeyRank(key = 40),
        )
    }

    @Test
    fun test_findKeyRank_consistency_with_selectEntry() {
        val map = treeMapOf(10 to "a", 20 to "b", 30 to "c", 40 to "d", 50 to "e")

        // For each existing key, verify that findKeyRank and selectEntry are consistent
        listOf(10, 20, 30, 40, 50).forEach { key ->
            val rankResult = map.findKeyRank(key = key)

            assertEquals(
                expected = RankKind.Existing,
                actual = rankResult.kind,
                message = "Key $key should exist",
            )

            val entry = map.selectEntry(entryRank = rankResult.rank)

            assertEquals(
                expected = key,
                actual = entry?.key,
                message = "selectEntry(findKeyRank($key).keyRank) should return entry with key $key",
            )
        }
    }

    @Test
    fun test_findKeyRank_consistency_with_selectEntry_potentialKeys() {
        val map = treeMapOf(10 to "a", 30 to "c", 50 to "e")

        // For a potential key, selectEntry at its rank should return the ceiling entry
        val key = 20
        val rankResult = map.findKeyRank(key = key)

        assertEquals(
            expected = RankKind.Potential,
            actual = rankResult.kind,
        )

        val entry = map.selectEntry(entryRank = rankResult.rank)

        // The entry at the potential rank should be the ceiling
        assertEquals(
            expected = 30,
            actual = entry?.key,
            message = "selectEntry at potential rank should return ceiling entry",
        )
    }
}
