# KAML vs kotlinx.serialization JSON with Inline Value Classes

This repository demonstrates an issue with KAML 0.77.0 when deserializing an inline value class that wraps a `Map<String, String>`, while kotlinx.serialization JSON 1.8.1 handles it correctly.

## The Issue

When using KAML 0.77.0 to deserialize an inline value class that wraps a `Map<String, String>`, it fails with an `UnknownPropertyException`. The error message indicates that KAML expects numeric indices (0, 1) instead of the actual string keys ('key1', 'key2').

Error message:
```
UnknownPropertyException at key1 on line 1, column 1: Unknown property 'key1'. Known properties are: 0, 1
```

## Example Code

```kotlin
@Serializable
@JvmInline
value class InlineMap(
    val value: Map<String, String>
)
```

## Behavior Comparison

### JSON Serialization/Deserialization

1. **Serialization**: kotlinx.serialization JSON successfully serializes the `InlineMap` class.
2. **Deserialization**: kotlinx.serialization JSON successfully deserializes the `InlineMap` class.

Example JSON output:
```json
{
    "value": {
        "key1": "value1",
        "key2": "value2"
    }
}
```

### YAML Serialization/Deserialization

1. **Serialization**: KAML successfully serializes the `InlineMap` class.
2. **Deserialization**: KAML fails to deserialize the `InlineMap` class with an `UnknownPropertyException`.

Example YAML output:
```yaml
value:
  key1: value1
  key2: value2
```

## Analysis

1. Both libraries can successfully serialize the `InlineMap` class.
2. kotlinx.serialization JSON can successfully deserialize the `InlineMap` class.
3. KAML fails to deserialize the `InlineMap` class with an `UnknownPropertyException`.
   It expects numeric indices (0, 1) instead of the actual string keys ('key1', 'key2').
4. This issue is specific to inline value classes that wrap a `Map<String, String>`.

## Dependencies

- KAML 0.77.0
- kotlinx.serialization JSON 1.8.1

## How to Run

1. Clone this repository
2. Run the test: `./gradlew test`

The test will demonstrate the difference in behavior between KAML and kotlinx.serialization JSON when handling the `InlineMap` class.