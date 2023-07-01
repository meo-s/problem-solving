// https://www.acmicpc.net/problem/1670

package BJ_1670_정상_회담_2

import kotlin.math.max

fun main() {
    val n = readln().toInt()

    val dp = LongArray(max(n + 1, 3)) { 0 }.also {
        it[0] = 1
        it[2] = 1
    }

    for (k in 4..n step 2) {
        for (i in 2..k) {
            dp[k] += dp[k - i] * dp[i - 2]
            dp[k] = dp[k] % 987654321
        }
    }

    println(dp[n])
}
