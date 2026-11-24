package com.axclogo.bedrock

/** 字符串命名空间；本轮仅处理完整文本，不提供按字符数量截取。 */
@JvmInline
public value class AxcStringSpace(public val base: String) {
    /** 添加完整文本前缀。 */
    public fun appendPrefix(string: String): String = string + base

    /** 添加完整文本后缀。 */
    public fun appendSuffix(string: String): String = base + string

    /** 移除一次匹配的完整前缀，不匹配则保持原值。 */
    public fun removePrefixString(prefix: String): String = base.removePrefix(prefix)

    /** 移除一次匹配的完整后缀，不匹配则保持原值。 */
    public fun removeSuffixString(suffix: String): String = base.removeSuffix(suffix)

    /** 按 Kotlin 的空白定义去除两端空白，不改变中间内容。 */
    public val trimmed: String get() = base.trim()

    /** 十进制整数转换，格式不符或超出 Int 范围返回 null；不隐式去除空白。 */
    public fun asInt(): Int? = base.toIntOrNull()

    /** 按 Kotlin/JVM 规则转换浮点数；非法文本返回 null，允许 NaN 和无穷。 */
    public fun asDouble(): Double? = base.toDoubleOrNull()
}

/** 字符串的唯一命名空间入口。 */
public val String.axc: AxcStringSpace get() = AxcStringSpace(this)
