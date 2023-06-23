// https://www.acmicpc.net/problem/2157

package BJ_2157_여행

import kotlin.math.max

data class SharedContext(val n: Int, val m: Int, val g: Array<IntArray>)

fun init() = System.`in`.bufferedReader().use { stdin ->
    val (n, m, k) = stdin.readLine().split(' ').map { it.toInt() }

    val g = Array(n) { IntArray(n) }
    for (i in 1..k) {
        val (u, v, w) = stdin.readLine().split(' ').map { it.toInt() }
        if (u < v) g[u - 1][v - 1] = max(g[u - 1][v - 1], w)
    }

    SharedContext(n, m, g)
}

fun plan(ctx: SharedContext): Int {
    val dp = Array(ctx.n) { IntArray(ctx.m) { -1 } }
    dp[0][0] = 0

    for (u in 0 until ctx.n - 1) {
        for (i in 0 until ctx.m - 1) {
            if (dp[u][i] < 0) continue

            for (v in u + 1 until ctx.n) {
                val w = ctx.g[u][v]
                if (0 < w && dp[v][i + 1] < dp[u][i] + w) {
                    dp[v][i + 1] = dp[u][i] + w
                }
            }
        }
    }

    return dp.last().max()
}

fun main() {
    println(plan(init()))
}
