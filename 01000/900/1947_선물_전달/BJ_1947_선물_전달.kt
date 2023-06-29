// https://www.acmicpc.net/problem/1947

fun main() {
    val n = readln().toInt()
    if (n == 1) {
        println("0")
    } else {
        var now = 1L
        var bef = 0L
        for (i in 3..n) {
            val next = ((i - 1) * (now + bef)) % 1_000_000_000
            bef = now
            now = next
        }

        println(now)
    }
}
