# AxcBedrock for Kotlin

English · [简体中文](README.zh_CN.md) · [CI](https://github.com/axclogo/AxcBedrock-Kotlin/actions/workflows/ci.yml)

A small Kotlin/JVM core with typed `value.axc.operation` namespaces. It keeps native Kotlin types and delegates common operations to the standard library.

The [Swift library](https://github.com/axclogo/AxcBedrock-Swift) targets Apple platforms; the [Dart library](https://github.com/axclogo/AxcBedrock-Dart) provides a Dart core and Flutter adapters. This version covers a deliberately small subset of shared capabilities. See the [API comparison](doc/api-parity.md).

**0.1.0-SNAPSHOT is a local development version. It has not been published to Maven Central.**

## A note from the author

I finally found time to use Codex to wrap up this stage of the Bedrock series. Swift, Dart, and Kotlin do not need identical feature lists, but each should have a complete small scope with examples, tests, and documentation, instead of leaving the original idea unfinished indefinitely.

In the AI era, I honestly do not know how much value libraries like these still have. Finishing what I set out to do gives me some closure. If it helps someone else, even better.

My focus is now on Agent research and [AxcAgentEngine](https://github.com/axclogo/AxcAgentEngine). This library will stay within its current scope for now, without a commitment to regular updates. If more people use it and clear needs emerge, I will consider continuing maintenance.

## Scope

| Namespace | Helpers |
| --- | --- |
| `List<T>.axc` | Safe indexing, bounds checks, chunks, stable distinct values, append/insert/remove |
| `Map<K,V>.axc` | Typed lookup, key presence, merge |
| `String.axc` | Complete text prefixes/suffixes, trimming, nullable number parsing |
| `Int.axc` / `Double.axc` | Typed inclusive clamping |

UI, associated objects, once, dates, digests, character-count slicing, and Kotlin Multiplatform targets are outside this version. Grapheme slicing needs its own design and validation; UTF-16 indexing is not a substitute.

## Build and consume

Use **JDK 17**. The checked-in Wrapper pins **Gradle 8.14.3** with a SHA-256 checksum; the project pins **Kotlin 2.2.21** and targets JVM 17. Android projects and other JVM versions have not been validated. The only runtime dependency is the Kotlin standard library.

```sh
./gradlew check publishToMavenLocal
./gradlew -p example run
```

On Windows, use `gradlew.bat`. `check` runs unit tests, 13 shared behavior fixtures, and compiler checks that reject incorrect types. The example is an independent Gradle project consuming the Maven artifact, without a source or project dependency.

After publishing locally, a consumer can use:

```kotlin
repositories {
    mavenLocal { content { includeModule("com.axclogo", "axc-bedrock") } }
    mavenCentral()
}
dependencies {
    implementation("com.axclogo:axc-bedrock:0.1.0-SNAPSHOT")
}
```

These are local development coordinates, not a claim of public registration or publication. JSON parsing and compiler tools are test/development dependencies only.

## Example

```kotlin
import com.axclogo.bedrock.axc

val item: Int? = listOf(10, 20).axc.objectAt(1)
val groups = listOf(1, 2, 3).axc.chunked(by = 2)
val unique = listOf(2, 1, 2).axc.removeRepeat()
val title: String? = mapOf("title" to "Bedrock").axc.valueFor("title")
val text = "👋你好".axc.removePrefixString("👋")
val progress: Double = 1.5.axc.clamped(min = 0.0, max = 1.0)
```

## Contracts

- Namespaces use statically resolved extension properties and value classes, preserving generic types. Some usages can avoid wrappers; generic/interface usages can still box.
- Collection edits return shallow copies and leave the input unchanged. Elements remain shared; Kotlin read-only interfaces do not imply deep immutability.
- Safe indexing returns null outside bounds. Insertion clamps the index, removal ignores invalid indices, and chunk sizes must be positive.
- Use `hasKey` to distinguish a missing map key from a stored null.
- Double clamping preserves an input NaN and in-range negative zero. NaN or reversed bounds throw IllegalArgumentException. Kotlin Int is 32-bit; integer ranges are not identical across the three languages.
- Affix operations match complete text, case-sensitively, without Unicode normalization. Trimming and parsing follow Kotlin/JVM rules. `asInt` rejects surrounding whitespace and returns null for malformed or overflowing values.

## Validation and maintenance

CI runs `check` on JDK 17, publishes to a temporary Maven repository, and runs the independent consumer. See Actions for actual run results.

Public API comments and the detailed [API comparison](doc/api-parity.md) are in Chinese. See [CHANGELOG](CHANGELOG.md) for this release's scope. Reproducible issues and focused fixes are welcome; additions need a concrete use case, documented behavior, and tests. There is no planned expansion into a full cross-language framework.

[MIT](LICENSE) · axclogo@163.com
