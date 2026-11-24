package com.axclogo.bedrock

import kotlin.test.*

class CollectionsTest {
    @Test fun safeAccessPreservesTypeAndChecksBoundaries() {
        val source = listOf(10, 20)
        val item: Int? = source.axc.objectAt(1)
        assertEquals(20, item)
        for (index in listOf(Int.MIN_VALUE, -1, 2, Int.MAX_VALUE)) {
            assertNull(source.axc.objectAt(index))
            assertTrue(source.axc.isCrossing(index))
        }
        assertFalse(source.axc.isCrossing(0))
        assertNull(emptyList<Int>().axc.objectAt(0))
    }

    @Test fun chunkingRejectsInvalidSizesAndPreservesRemainder() {
        assertEquals(listOf(listOf(1, 2), listOf(3)), listOf(1, 2, 3).axc.chunked(2))
        assertEquals(emptyList(), emptyList<Int>().axc.chunked(2))
        assertEquals(listOf(listOf(1)), listOf(1).axc.chunked(Int.MAX_VALUE))
        for (size in listOf(0, -1)) assertFailsWith<IllegalArgumentException> { listOf(1).axc.chunked(size) }
    }

    @Test fun deduplicationPreservesOrderAndNull() {
        assertEquals(listOf(2, null, 1), listOf(2, null, 2, 1, null).axc.removeRepeat())
    }

    @Test fun editsPreserveOriginalAndBoundInsertion() {
        val source = mutableListOf(1, 2)
        assertEquals(listOf(1, 2, 3), source.axc.append(listOf(3)))
        assertEquals(listOf(0, 1, 2), source.axc.insert(0, Int.MIN_VALUE))
        assertEquals(listOf(1, 2, 3), source.axc.insert(3, Int.MAX_VALUE))
        assertEquals(listOf(1, 9, 2), source.axc.insert(9, 1))
        assertEquals(listOf(2), source.axc.remove(0))
        assertEquals(source, source.axc.remove(-1))
        assertEquals(source, source.axc.remove(2))
        assertEquals(listOf(1, 2), source)
    }

    @Test fun returnedCollectionsAreShallowSnapshots() {
        val nested = mutableListOf(1)
        val source = mutableListOf(nested)
        val result = source.axc.append(emptyList())
        source.clear()
        assertEquals(1, result.size)
        nested.add(2)
        assertEquals(listOf(1, 2), result.single())
    }

    @Test fun mapDistinguishesMissingKeyAndNullValue() {
        val source = mapOf("present" to null, "value" to 1)
        assertTrue(source.axc.hasKey("present"))
        assertFalse(source.axc.hasKey("missing"))
        assertNull(source.axc.valueFor("present"))
        assertNull(source.axc.valueFor("missing"))
        val value: Int? = source.axc.valueFor("value")
        assertEquals(1, value)
    }

    @Test fun mapMergePreservesOriginalAndUsesNewValues() {
        val source = linkedMapOf("a" to 1, "b" to 2)
        val merged = source.axc.append(mapOf("b" to 3, "c" to 4))
        assertEquals(mapOf("a" to 1, "b" to 3, "c" to 4), merged)
        assertEquals(mapOf("a" to 1, "b" to 2), source)
        assertEquals(listOf("a", "b", "c"), merged.keys.toList())
    }
}
