// https://www.acmicpc.net/problem/2228

package BJ_2228_구간_나누기

import kotlin.math.max

class SharedContext(val m: Int, val a: IntArray)

fun init() = System.`in`.bufferedReader().use { stdin ->
    val (n, m) = stdin.readLine().split(' ').map { it.toInt() }
    SharedContext(m, IntArray(n) { stdin.readLine().toInt() })
}

fun solve(ctx: SharedContext): Long {
    val cumsum = IntArray(ctx.a.size + 1)
    for (i in 1..ctx.a.size) {
        cumsum[i] = cumsum[i - 1] + ctx.a[i - 1]
    }

    val dp = Array(ctx.a.size) { LongArray(ctx.m + 1) { Int.MIN_VALUE.toLong() } }
    dp[0][0] = 0
    dp[0][1] = ctx.a[0].toLong()

    for (i in 1 until ctx.a.size) {
        dp[i - 1].copyInto(dp[i])

        for (j in 0..i) {
            dp[i][1] = max(dp[i][1], (cumsum[i + 1] - cumsum[j]).toLong())
        }

        for (j in 2..i) {
            for (m in 2..ctx.m) {
                dp[i][m] = max(dp[i][m], dp[j - 2][m - 1] + (cumsum[i + 1] - cumsum[j]))
            }
        }
    }

    return dp.map { it.last() }.max()
}

fun main() {
    println(solve(init()))
}
