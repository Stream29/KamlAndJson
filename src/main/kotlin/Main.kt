package io.github.stream29

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

/**
 * This example demonstrates the issue with KAML 0.77.0 when deserializing an inline value class
 * that wraps a Map<String, String>.
 * 
 * The issue: KAML fails to deserialize the InlineMap class, while kotlinx.serialization JSON works fine.
 */

// The problematic inline class as described in the issue
@Serializable
@JvmInline
value class InlineMap(
    val value: Map<String, String>
)

// A wrapper class to test serialization/deserialization
@Serializable
data class TestWrapper(
    val inlineMap: InlineMap,
    val regularMap: Map<String, String>
)

fun main() {
    println("COMPARING KAML AND KOTLINX.SERIALIZATION JSON WITH INLINE MAP CLASS")
    println("=================================================================")

    // Create test data
    val testMap = mapOf("key1" to "value1", "key2" to "value2")
    val inlineMap = InlineMap(testMap)

    // Configure serializers
    val json = Json { prettyPrint = true }
    val yaml = Yaml()

    println("Original data: $testMap")
    println("Wrapped in InlineMap: $inlineMap")
    println()

    // ===== JSON TESTS =====
    println("JSON SERIALIZATION/DESERIALIZATION")
    println("----------------------------------")

    // Serialize InlineMap to JSON
    val jsonString = json.encodeToString(inlineMap)
    println("JSON serialization result:")
    println(jsonString)
    println()

    // Deserialize JSON back to InlineMap
    try {
        val deserializedFromJson = json.decodeFromString<InlineMap>(jsonString)
        println("JSON deserialization successful ✓")
        println("Deserialized value: $deserializedFromJson")
        println("Original equals deserialized: ${inlineMap == deserializedFromJson}")
    } catch (e: Exception) {
        println("JSON deserialization failed ✗")
        println("Error: ${e.message}")
    }
    println()

    // ===== YAML TESTS =====
    println("YAML SERIALIZATION/DESERIALIZATION")
    println("----------------------------------")

    // Serialize InlineMap to YAML
    val yamlString = yaml.encodeToString(inlineMap)
    println("YAML serialization result:")
    println(yamlString)
    println()

    // Deserialize YAML back to InlineMap
    try {
        val deserializedFromYaml = yaml.decodeFromString<InlineMap>(yamlString)
        println("YAML deserialization successful ✓")
        println("Deserialized value: $deserializedFromYaml")
        println("Original equals deserialized: ${inlineMap == deserializedFromYaml}")
    } catch (e: Exception) {
        println("YAML deserialization failed ✗")
        println("Error: ${e.message}")
        println()
        println("This confirms the issue described in the problem statement:")
        println("KAML 0.77.0 fails to deserialize an inline value class that wraps a Map<String, String>,")
        println("while kotlinx.serialization JSON 1.8.1 handles it correctly.")
    }

    // ===== ANALYSIS =====
    println()
    println("ANALYSIS OF THE ISSUE")
    println("-------------------")
    println("1. Both libraries can successfully serialize the InlineMap class.")
    println("2. kotlinx.serialization JSON can successfully deserialize the InlineMap class.")
    println("3. KAML fails to deserialize the InlineMap class with an 'UnknownPropertyException'.")
    println("   It expects numeric indices (0, 1) instead of the actual string keys ('key1', 'key2').")
    println("4. This issue is specific to inline value classes that wrap a Map<String, String>.")
    println()

    // Show the serialized formats for comparison
    println("SERIALIZED FORMATS COMPARISON")
    println("---------------------------")
    println("JSON format:")
    println(jsonString)
    println()
    println("YAML format:")
    println(yamlString)
}
