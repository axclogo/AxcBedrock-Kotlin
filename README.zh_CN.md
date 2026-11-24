# AxcBedrock for Kotlin

[English](README.md) · 简体中文 · [CI](https://github.com/axclogo/AxcBedrock-Kotlin/actions/workflows/ci.yml)

AxcBedrock 的 Kotlin 最小核心版：通过 `value.axc.operation` 保留熟悉的调用方式，使用 Kotlin 原生类型与标准库完成常见的小操作。

[Swift 版](https://github.com/axclogo/AxcBedrock-Swift) 面向 Apple 平台，[Dart 版](https://github.com/axclogo/AxcBedrock-Dart) 提供 Dart 核心与 Flutter 适配。本库只覆盖可自然对应的一小部分基础能力，具体范围见 [API 对照](doc/api-parity.md)。

当前版本为 **0.1.0-SNAPSHOT**，仅验证本地 Maven 发布和消费，**尚未发布到 Maven Central**。

## 作者的话

终于有空，借助 Codex 给 Bedrock 系列做一轮收尾。Swift、Dart 和 Kotlin 不必每个功能都一样，但至少把各自这一阶段的小功能、示例、测试和文档做成一个闭环，别让当初的想法一直烂尾。

到了 AI 时代，我也不确定这样的基础工具库还剩下多少价值。先把自己想做的事情做完，给过去的投入一个交代；如果恰好还能帮到别人，就更好了。

我现在主要研究 Agent，正在做 [AxcAgentEngine](https://github.com/axclogo/AxcAgentEngine)。这个小版本完成后暂时以现有范围收尾，不承诺持续更新；以后如果用的人多、出现明确需求，再考虑继续维护。

## 本轮范围

| 入口 | 能力 |
| --- | --- |
| `List<T>.axc` | 安全索引、越界判断、分块、稳定去重、追加、插入、删除 |
| `Map<K,V>.axc` | 类型化读取、键存在判断、合并 |
| `String.axc` | 完整文本前后缀添加/移除、修剪空白、可选数值转换 |
| `Int.axc` / `Double.axc` | 保留类型的闭区间限制 |

不包含 UI、关联对象、once、日期、摘要、字符数量截取或 Kotlin Multiplatform 适配。字符串的字素簇截取需要单独设计和验证，不用 UTF-16 下标冒充 Swift/Dart 的字符语义。

## 构建和接入

构建使用 **JDK 17、Kotlin 2.2.21、Gradle 8.14.3**，Wrapper 与发行包校验值随仓库提供。产物以 JVM 17 为目标；本轮没有验证 Android 工程或其他 JVM 版本。运行时依赖只有 Kotlin 标准库，不依赖 Android SDK、AppCompat 或 Material。

```sh
./gradlew check publishToMavenLocal
./gradlew -p example run
```

Windows 使用 `gradlew.bat`。`check` 同时运行单元测试、13 个共享行为样例和编译期正反例。示例是独立 Gradle 工程，从 Maven 仓库解析产物，没有通过 project dependency 或源码包含绕过发布流程。

本地发布完成后，使用方可配置：

```kotlin
repositories {
    mavenLocal { content { includeModule("com.axclogo", "axc-bedrock") } }
    mavenCentral()
}
dependencies {
    implementation("com.axclogo:axc-bedrock:0.1.0-SNAPSHOT")
}
```

这里的坐标供本地开发使用，不表示已经注册或公开发布。测试 JSON 解析库和编译期检查工具只用于开发，不进入运行时依赖。

## 使用示例

```kotlin
import com.axclogo.bedrock.axc

val item: Int? = listOf(10, 20).axc.objectAt(1)
val groups = listOf(1, 2, 3).axc.chunked(by = 2)
val unique = listOf(2, 1, 2).axc.removeRepeat()
val title: String? = mapOf("title" to "Bedrock").axc.valueFor("title")
val text = "👋你好".axc.removePrefixString("👋")
val progress: Double = 1.5.axc.clamped(min = 0.0, max = 1.0)
```

## 行为约定

- `.axc` 是静态扩展属性，保留泛型，不使用反射或 Any 转发。value class 在部分调用中可消除包装，但泛型、接口等场景仍可能装箱，不承诺绝对零分配。
- 集合操作不修改原集合，返回浅拷贝；元素仍可共享。Kotlin 的只读 List/Map 接口不等于深度不可变。
- 安全索引越界返回 null；插入位置限制在合法范围；删除位置越界时内容不变；分块大小必须为正。
- `Map.valueFor` 无法单独区分缺失键与 null 值，用 `hasKey` 区分。
- Double 区间限制保留输入 NaN 和区间内的负零；NaN 边界与反向区间抛出 IllegalArgumentException。Int 是 32 位，不能假设与 Swift/Dart 的整数范围相同。
- 文本前后缀是大小写敏感的完整文本匹配；不做 Unicode 规范化。trim 和数值解析遵循 Kotlin/JVM 的规则；`asInt` 不隐式修剪空白，非法输入或溢出返回 null。

## 验证与维护

CI 在 JDK 17 上运行完整 `check`，将产物发布到临时 Maven 仓库，再用独立示例验证消费。运行结果见 Actions；配置了 CI 不等于每次构建都已通过。

[CHANGELOG](CHANGELOG.md) 记录本轮改动。欢迎带复现的 Issue 和范围明确的修复；新增能力先证明实际需求，并补齐行为约定和测试。当前不扩充为全量跨语言框架。

[MIT](LICENSE) · axclogo@163.com
