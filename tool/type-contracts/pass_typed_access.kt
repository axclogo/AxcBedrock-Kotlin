import com.axclogo.bedrock.axc

fun typedAccess() {
    val item: Int? = listOf(1).axc.objectAt(0)
    val value: Int? = mapOf("a" to 1).axc.valueFor("a")
    val integer: Int = 12.axc.clamped(0, 10)
    val decimal: Double = 1.5.axc.clamped(0.0, 1.0)
    println(listOf(item, value, integer, decimal))
}
