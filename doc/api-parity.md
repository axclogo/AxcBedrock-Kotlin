# Bedrock 系列的最小对应范围

本轮只实现类型安全的 `.axc` 和少量能自然对应的基础操作，不承诺三个版本功能数量相同。

| 能力 | Kotlin | Swift / Dart 对应情况 |
| --- | --- | --- |
| 列表安全索引 | `objectAt`、`isCrossing` | 对应安全访问与越界判断；越界返回 null/nil |
| 分块 | `chunked(by)` | 正数大小、末块允许不足；非法参数按各语言错误机制处理 |
| 去重 | `removeRepeat` | 保留首次出现顺序，依赖本语言相等性与哈希契约 |
| 集合增删 | `append`、`insert`、`remove` | 不修改原集合；插入限制位置，越界删除内容不变 |
| 字典 | `valueFor`、`hasKey`、`append` | 保留泛型；同名键以后值覆盖，null 用键存在性区分 |
| 字符串前后缀 | `appendPrefix/appendSuffix`、`removePrefixString/removeSuffixString` | 对应完整文本操作；不要求复制其他版本的实现缺陷 |
| 修剪与解析 | `trimmed`、`asInt`、`asDouble` | 委托 Kotlin/JVM；空白定义、数值范围和合法语法不保证三端完全相同 |
| 区间限制 | `Int/Double.axc.clamped` | 有序闭区间；Double 输入 NaN 保留，NaN 边界拒绝 |

`src/test/resources/parity.json` 选取 Dart 仓库 `b95da8d` 中 `test/fixtures/parity.json`（位于 `packages/axc_bedrock`）的列表访问、分块和 Double 区间限制样例，共 13 项，保持输入与期望值不变。这些样例也由 Swift 的共享验证程序验证过。它们不证明全部 API 等价。

以下部分有意暂缓：

- 按字素簇截取：Kotlin String 的普通下标不能代替 Swift Character / Dart characters。当前只提供完整文本前后缀操作，不承诺 Unicode 规范化或按字素边界匹配。
- 关联对象、弱引用与 once：涉及身份、生命周期、线程和异常语义，不为凑齐 API 引入全局映射或同步框架。
- 日期、摘要、更多数值类型：按实际需求再选择平台或成熟依赖。
- Android UI、Compose、Kotlin Multiplatform：本轮只有 Kotlin/JVM 17；不把 JVM 构建成功描述为 Android 或多平台支持。

`Any.axc`、反射派发和第二套集合类型不在设计中。新增能力优先组合标准库；只在三端确实需要一致行为时增加适配和共享测试。
