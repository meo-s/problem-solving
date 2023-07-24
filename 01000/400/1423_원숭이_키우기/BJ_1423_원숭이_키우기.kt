// https://www.acmicpc.net/problem/1423

package BJ_1423_원숭이_키우기

import kotlin.math.*

fun main() = System.`in`.bufferedReader().use { stdin ->
    val maxLevel = stdin.readLine().toInt()
    val numCharacters = stdin.readLine().split(' ').map { it.toInt() }
    val strengths = stdin.readLine().split(' ').map { it.toLong() }
    val duration = stdin.readLine().toInt()

    val dp = Array(2) { LongArray(duration + 1) { 0 } }

    for (from in 0 until maxLevel - 1) {
        repeat(min(duration, numCharacters[from])) {
            dp[0].copyInto(dp[1])

            for (to in from + 1 until maxLevel) {
                val strDiff = strengths[to] - strengths[from]
                if (strDiff <= 0) {
                    continue
                }

                for (i in 0..duration - (to - from)) {
                    dp[0][i + (to - from)] = max(dp[0][i + (to - from)], dp[1][i] + strDiff)
                }
            }
        }
    }

    println(numCharacters.zip(strengths).sumOf { (n, s) -> n * s } + dp[0].max())
}
