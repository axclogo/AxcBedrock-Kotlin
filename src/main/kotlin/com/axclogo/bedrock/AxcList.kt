package com.axclogo.bedrock

/** 列表命名空间；操作保留元素类型，不修改原列表，结果为浅拷贝。 */
@JvmInline
public value class AxcListSpace<T>(public val base: List<T>) {
    /** 安全读取元素，索引越界返回 null。 */
    public fun objectAt(index: Int): T? = base.getOrNull(index)

    /** 判断索引是否超出已有元素范围。 */
    public fun isCrossing(index: Int): Boolean = index !in base.indices

    /** 按正整数大小分块；末块可以不足，非法大小抛出 IllegalArgumentException。 */
    public fun chunked(by: Int): List<List<T>> = base.chunked(by)

    /** 按相等性稳定去重，保留首次出现顺序。 */
    public fun removeRepeat(): List<T> = base.distinct()

    /** 追加元素并返回新列表。 */
    public fun append(values: Iterable<T>): List<T> = base + values

    /** 插入元素；位置限制到 0 到列表长度之间。 */
    public fun insert(value: T, at: Int): List<T> = base.toMutableList().apply {
        add(at.coerceIn(0, size), value)
    }

    /** 删除指定位置；越界时返回内容不变的浅拷贝。 */
    public fun remove(at: Int): List<T> = base.toMutableList().apply {
        if (at in indices) removeAt(at)
    }
}

/** 保留元素类型的列表入口；不提供泛化到 Any 的入口。 */
public val <T> List<T>.axc: AxcListSpace<T> get() = AxcListSpace(this)
