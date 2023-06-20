// https://www.acmicpc.net/problem/1965

import kotlin.math.max

fun main() = System.`in`.bufferedReader().use { stdin ->
    stdin.readLine()
    val boxes = stdin.readLine()
        .split(' ')
        .map { it.toInt() }

    val dp = IntArray(1001)
    for (box in boxes) {
        for (i in box - 1 downTo 0) {
            dp[box] = max(dp[box], dp[i] + 1)
        }
    }

    println(dp.max())
}
