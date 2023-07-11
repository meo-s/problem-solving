// https://www.acmicpc.net/problem/20500

package BJ_20500_Ezreal_여눈부터_가네_ㅈㅈ

fun main() {
    val N = readln().toInt()
    val dp = Array(N + 1) { LongArray(15) }
    dp[1][1] = 1
    dp[1][5] = 1

    for (i in 2..N) {
        for (r in 0 until 15) {
            dp[i][(r * 10 + 1) % 15] += dp[i - 1][r]
            dp[i][(r * 10 + 5) % 15] += dp[i - 1][r]
        }

        for (r in 0 until 15) {
            dp[i][r] = dp[i][r] % 1_000_000_007
        }
    }

    println(dp[N][0])
}
