package com.axclogo.bedrock

import kotlin.test.*

class ValuesTest {
    @Test fun completeTextAffixesPreserveUnicode() {
        val text = "👨‍👩‍👧‍👦你好"
        assertEquals("前$text", text.axc.appendPrefix("前"))
        assertEquals("${text}后", text.axc.appendSuffix("后"))
        assertEquals("你好", text.axc.removePrefixString("👨‍👩‍👧‍👦"))
        assertEquals("👨‍👩‍👧‍👦你", text.axc.removeSuffixString("好"))
        assertEquals(text, text.axc.removePrefixString("不存在"))
        assertEquals(text, text.axc.removeSuffixString(""))
        assertEquals("xx", "xxx".axc.removePrefixString("x"))
        assertEquals("", "".axc.appendPrefix(""))
    }

    @Test fun trimmingOnlyRemovesBoundaryWhitespace() {
        assertEquals("a b", " \ta b\n".axc.trimmed)
        assertEquals("", "\t\n ".axc.trimmed)
    }

    @Test fun optionalIntegerParsingRejectsOverflowAndWhitespace() {
        assertEquals(42, "42".axc.asInt())
        assertEquals(Int.MIN_VALUE, "-2147483648".axc.asInt())
        for (text in listOf("2147483648", " 1 ", "1.0", "", "abc")) assertNull(text.axc.asInt())
    }

    @Test fun optionalDoubleParsingRetainsNativeSpecialValues() {
        assertEquals(1.25, "1.25".axc.asDouble())
        assertTrue("NaN".axc.asDouble()!!.isNaN())
        assertEquals(Double.POSITIVE_INFINITY, "Infinity".axc.asDouble())
        assertNull("abc".axc.asDouble())
    }

    @Test fun integerClampingPreservesTypeAndHandlesExtremeValues() {
        val result: Int = Int.MAX_VALUE.axc.clamped(-2, 2)
        assertEquals(2, result)
        assertEquals(-2, Int.MIN_VALUE.axc.clamped(-2, 2))
        assertEquals(1, 1.axc.clamped(0, 2))
        assertEquals(1, 9.axc.clamped(1, 1))
        assertFailsWith<IllegalArgumentException> { 1.axc.clamped(2, 0) }
    }

    @Test fun doubleClampingHandlesNaNInfinitiesAndSignedZero() {
        assertTrue(Double.NaN.axc.clamped(0.0, 1.0).isNaN())
        assertEquals(1.0, Double.POSITIVE_INFINITY.axc.clamped(0.0, 1.0))
        assertEquals(0.0, Double.NEGATIVE_INFINITY.axc.clamped(0.0, 1.0))
        assertEquals((-0.0).toBits(), (-0.0).axc.clamped(0.0, 1.0).toBits())
        assertEquals(Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY.axc.clamped(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY))
        for ((min, max) in listOf(Double.NaN to 1.0, 0.0 to Double.NaN, 2.0 to 1.0)) {
            assertFailsWith<IllegalArgumentException> { 0.5.axc.clamped(min, max) }
        }
    }
}
