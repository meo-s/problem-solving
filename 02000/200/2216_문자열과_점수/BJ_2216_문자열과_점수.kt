// https://www.acmicpc.net/problem/2216

package BJ_2216_문자열과_점수

import kotlin.math.max

class SharedContext(val a: Int, val b: Int, val c: Int, val s1: String, val s2: String)

fun init() = System.`in`.bufferedReader().use { stdin ->
    val (a, b, c) = stdin.readLine().split(' ').map { it.toInt() }
    SharedContext(a, b, c, stdin.readLine(), stdin.readLine())
}

fun match(ctx: SharedContext): Int {
    val dp = Array(ctx.s1.length + 1) { IntArray(ctx.s2.length + 1) { Int.MIN_VALUE } }.apply { this[0][0] = 0 }

    for (i in 0..ctx.s1.length) {
        for (j in 0..ctx.s2.length) {
            if (i + 1 <= ctx.s1.length) {
                dp[i + 1][j] = max(dp[i + 1][j], dp[i][j] + ctx.b)
            }
            if (j + 1 <= ctx.s2.length) {
                dp[i][j + 1] = max(dp[i][j + 1], dp[i][j] + ctx.b)
            }
            if (i + 1 <= ctx.s1.length && j + 1 <= ctx.s2.length) {
                dp[i + 1][j + 1] = max(dp[i + 1][j + 1], dp[i][j] + if (ctx.s1[i] == ctx.s2[j]) ctx.a else ctx.c)
            }
        }
    }

    return dp.last().last()
}

fun main() {
    println(match(init()))
}
