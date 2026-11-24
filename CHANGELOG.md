# Changelog

## 0.1.0-SNAPSHOT

- 将空的 Android 模块原型整理为独立 Kotlin/JVM 17 库，移除模板 UI 依赖和机器本地配置。
- 增加 String、List、Map、Int、Double 的类型安全 `.axc` 入口。
- 提供安全索引、分块、稳定去重、集合增删、字典合并、完整文本前后缀与数值限制。
- 明确浅拷贝、null、非法范围、NaN、负零和整数范围语义。
- 增加标准 Gradle Wrapper、中文公开注释、边界测试、共享样例、编译期反例和 CI。
- 使用独立示例验证本地 Maven 发布与消费；尚未发布到 Maven Central。
- 本轮作为 Bedrock 系列的小范围收尾，后续维护根据实际使用情况决定。
