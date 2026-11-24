package com.axclogo.bedrock

/** 保留 Int 类型的数值命名空间。 */
@JvmInline
public value class AxcIntSpace(public val base: Int) {
    /** 限制到闭区间；反向边界抛出 IllegalArgumentException。 */
    public fun clamped(min: Int, max: Int): Int = base.coerceIn(min, max)
}

/** 整数的唯一命名空间入口。 */
public val Int.axc: AxcIntSpace get() = AxcIntSpace(this)

/** 保留 Double 类型的数值命名空间。 */
@JvmInline
public value class AxcDoubleSpace(public val base: Double) {
    /** 限制到闭区间；保留输入 NaN 和区间内的负零，禁止 NaN 或反向边界。 */
    public fun clamped(min: Double, max: Double): Double {
        require(!min.isNaN() && !max.isNaN() && min <= max) { "边界必须有序且不能为 NaN" }
        return base.coerceIn(min, max)
    }
}

/** 浮点数的唯一命名空间入口。 */
public val Double.axc: AxcDoubleSpace get() = AxcDoubleSpace(this)
