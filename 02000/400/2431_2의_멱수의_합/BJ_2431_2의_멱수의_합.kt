// https://www.acmicpc.net/problem/2431

package BJ_2431_2의_멱수의_합

val M = 1_000_000_000
val dp = IntArray(1_000_001) { 1 }

fun main() = System.`in`.bufferedReader().use { stdin ->
    for (i in dp.indices.drop(2)) {
        if (i % 2 == 0) dp[i] = (dp[i / 2] + dp[i - 1]) % M
        else dp[i] = dp[i - 1]
    }
    println(dp[stdin.readLine().toInt()])
}
