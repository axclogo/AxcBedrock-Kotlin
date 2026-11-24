package com.axclogo.bedrock

import kotlinx.serialization.json.*
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.*

class ParityTest {
    @TestFactory fun sharedSwiftDartCases(): List<DynamicTest> {
        val fixture = javaClass.getResourceAsStream("/parity.json")!!.bufferedReader().use {
            Json.parseToJsonElement(it.readText()).jsonObject
        }
        val tests = mutableListOf<DynamicTest>()
        for ((index, entry) in fixture.getValue("listObject").jsonArray.withIndex()) {
            tests += DynamicTest.dynamicTest("shared index $index") {
                val row = entry.jsonObject
                val values = row.getValue("values").jsonArray.map { it.jsonPrimitive.int }
                assertEquals(row.getValue("expected").jsonPrimitive.intOrNull,
                    values.axc.objectAt(row.getValue("index").jsonPrimitive.int))
            }
        }
        for ((index, entry) in fixture.getValue("listChunked").jsonArray.withIndex()) {
            tests += DynamicTest.dynamicTest("shared chunk $index") {
                val row = entry.jsonObject
                val values = row.getValue("values").jsonArray.map { it.jsonPrimitive.int }
                val expected = row.getValue("expected").jsonArray.map { chunk ->
                    chunk.jsonArray.map { it.jsonPrimitive.int }
                }
                assertEquals(expected, values.axc.chunked(row.getValue("size").jsonPrimitive.int))
            }
        }
        for ((index, entry) in fixture.getValue("clamped").jsonArray.withIndex()) {
            tests += DynamicTest.dynamicTest("shared clamp $index") {
                val row = entry.jsonObject
                val value = when (row["kind"]?.jsonPrimitive?.content) {
                    "nan" -> Double.NaN
                    "infinity" -> Double.POSITIVE_INFINITY
                    "negativeInfinity" -> Double.NEGATIVE_INFINITY
                    else -> row.getValue("value").jsonPrimitive.double
                }
                val result = value.axc.clamped(row.getValue("min").jsonPrimitive.double,
                    row.getValue("max").jsonPrimitive.double)
                if (value.isNaN()) assertTrue(result.isNaN())
                else assertEquals(row.getValue("expected").jsonPrimitive.double, result)
            }
        }
        return tests
    }
}
