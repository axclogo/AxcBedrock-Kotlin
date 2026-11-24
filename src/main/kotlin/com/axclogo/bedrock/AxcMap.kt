package com.axclogo.bedrock

/** 字典命名空间，保留键和值的类型。 */
@JvmInline
public value class AxcMapSpace<K, V>(public val base: Map<K, V>) {
    /** 读取值；不存在和存储 null 均返回 null，需区分时使用 hasKey。 */
    public fun valueFor(key: K): V? = base[key]

    /** 判断键是否存在，包括值为 null 的键。 */
    public fun hasKey(key: K): Boolean = base.containsKey(key)

    /** 合并为新字典，重复键使用新值，不修改原字典。 */
    public fun append(values: Map<K, V>): Map<K, V> = base + values
}

/** 保留键值类型的字典入口。 */
public val <K, V> Map<K, V>.axc: AxcMapSpace<K, V> get() = AxcMapSpace(this)
