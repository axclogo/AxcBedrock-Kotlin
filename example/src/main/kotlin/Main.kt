import com.axclogo.bedrock.axc

fun main() {
    val item: Int? = listOf(10, 20).axc.objectAt(1)
    check(item == 20)
    check(listOf(1, 2, 3, 4, 5).axc.chunked(by = 2) == listOf(listOf(1, 2), listOf(3, 4), listOf(5)))
    check(listOf(1, 2, 1).axc.removeRepeat() == listOf(1, 2))
    check(mapOf("name" to "Bedrock").axc.valueFor("name") == "Bedrock")
    check("👋你好".axc.removePrefixString("👋") == "你好")
    val progress: Double = 1.5.axc.clamped(min = 0.0, max = 1.0)
    check(progress == 1.0)
    println("PASS: independently consumed com.axclogo:axc-bedrock:0.1.0-SNAPSHOT")
}
