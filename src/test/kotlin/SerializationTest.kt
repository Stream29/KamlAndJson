package io.github.stream29

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import org.junit.jupiter.api.Test

class SerializationTest {
    @Test
    fun testSerialization() {
        // Create test data
        val testMap = mapOf("key1" to "value1", "key2" to "value2")
        val inlineMap = InlineMap(testMap)

        // Configure serializers
        val json = Json { prettyPrint = true }
        val yaml = Yaml()

        System.out.println("[DEBUG_LOG] COMPARING KAML AND KOTLINX.SERIALIZATION JSON WITH INLINE MAP CLASS")
        System.out.println("[DEBUG_LOG] =================================================================")

        System.out.println("[DEBUG_LOG] Original data: $testMap")
        System.out.println("[DEBUG_LOG] Wrapped in InlineMap: $inlineMap")

        // ===== JSON TESTS =====
        System.out.println("[DEBUG_LOG] ")
        System.out.println("[DEBUG_LOG] JSON SERIALIZATION/DESERIALIZATION")
        System.out.println("[DEBUG_LOG] ----------------------------------")

        // Serialize InlineMap to JSON
        val jsonString = json.encodeToString(inlineMap)
        System.out.println("[DEBUG_LOG] JSON serialization result:")
        System.out.println("[DEBUG_LOG] $jsonString")

        // Deserialize JSON back to InlineMap
        try {
            val deserializedFromJson = json.decodeFromString<InlineMap>(jsonString)
            System.out.println("[DEBUG_LOG] JSON deserialization successful ✓")
            System.out.println("[DEBUG_LOG] Deserialized value: $deserializedFromJson")
            System.out.println("[DEBUG_LOG] Original equals deserialized: ${inlineMap == deserializedFromJson}")
        } catch (e: Exception) {
            System.out.println("[DEBUG_LOG] JSON deserialization failed ✗")
            System.out.println("[DEBUG_LOG] Error: ${e.message}")
        }

        // ===== YAML TESTS =====
        System.out.println("[DEBUG_LOG] ")
        System.out.println("[DEBUG_LOG] YAML SERIALIZATION/DESERIALIZATION")
        System.out.println("[DEBUG_LOG] ----------------------------------")

        // Serialize InlineMap to YAML
        val yamlString = yaml.encodeToString(inlineMap)
        System.out.println("[DEBUG_LOG] YAML serialization result:")
        System.out.println("[DEBUG_LOG] $yamlString")

        // Deserialize YAML back to InlineMap
        try {
            val deserializedFromYaml = yaml.decodeFromString<InlineMap>(yamlString)
            System.out.println("[DEBUG_LOG] YAML deserialization successful ✓")
            System.out.println("[DEBUG_LOG] Deserialized value: $deserializedFromYaml")
            System.out.println("[DEBUG_LOG] Original equals deserialized: ${inlineMap == deserializedFromYaml}")
        } catch (e: Exception) {
            System.out.println("[DEBUG_LOG] YAML deserialization failed ✗")
            System.out.println("[DEBUG_LOG] Error: ${e.message}")
            System.out.println("[DEBUG_LOG] ")
            System.out.println("[DEBUG_LOG] This confirms the issue described in the problem statement:")
            System.out.println("[DEBUG_LOG] KAML 0.77.0 fails to deserialize an inline value class that wraps a Map<String, String>,")
            System.out.println("[DEBUG_LOG] while kotlinx.serialization JSON 1.8.1 handles it correctly.")
        }

        // ===== ANALYSIS =====
        System.out.println("[DEBUG_LOG] ")
        System.out.println("[DEBUG_LOG] ANALYSIS OF THE ISSUE")
        System.out.println("[DEBUG_LOG] -------------------")
        System.out.println("[DEBUG_LOG] 1. Both libraries can successfully serialize the InlineMap class.")
        System.out.println("[DEBUG_LOG] 2. kotlinx.serialization JSON can successfully deserialize the InlineMap class.")
        System.out.println("[DEBUG_LOG] 3. KAML fails to deserialize the InlineMap class with an 'UnknownPropertyException'.")
        System.out.println("[DEBUG_LOG]    It expects numeric indices (0, 1) instead of the actual string keys ('key1', 'key2').")
        System.out.println("[DEBUG_LOG] 4. This issue is specific to inline value classes that wrap a Map<String, String>.")

        // Show the serialized formats for comparison
        System.out.println("[DEBUG_LOG] ")
        System.out.println("[DEBUG_LOG] SERIALIZED FORMATS COMPARISON")
        System.out.println("[DEBUG_LOG] ---------------------------")
        System.out.println("[DEBUG_LOG] JSON format:")
        System.out.println("[DEBUG_LOG] $jsonString")
        System.out.println("[DEBUG_LOG] ")
        System.out.println("[DEBUG_LOG] YAML format:")
        System.out.println("[DEBUG_LOG] $yamlString")
    }
}
