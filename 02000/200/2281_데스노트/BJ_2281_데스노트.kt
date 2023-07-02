// https://www.acmicpc.net/problem/2281

package BJ_2281_데스노트

import kotlin.math.min

class SharedContext(val n: Int, val m: Int, val names: IntArray)

fun init() = System.`in`.bufferedReader().use { stdin ->
    val (n, m) = stdin.readLine().split(' ').map { it.toInt() }
    SharedContext(n, m, IntArray(n) { stdin.readLine().toInt() })
}

fun writeDeathNote(ctx: SharedContext, dp: IntArray, nameIndex: Int): Int {
    if (dp[nameIndex] == Int.MAX_VALUE) {
        var lineLen = 0
        for (i in nameIndex until ctx.n) {
            lineLen += ctx.names[i] + (if (nameIndex < i) 1 else 0)
            if (ctx.m < lineLen) {
                break
            }

            if (i + 1 == ctx.n) {
                dp[nameIndex] = 0
                break
            } else {
                val whitespaces = (ctx.m - lineLen) * (ctx.m - lineLen)
                dp[nameIndex] = min(dp[nameIndex], whitespaces + writeDeathNote(ctx, dp, i + 1))
            }
        }
    }

    return dp[nameIndex]
}

fun writeDeathNote(ctx: SharedContext) = writeDeathNote(ctx, IntArray(ctx.n) { Int.MAX_VALUE }, 0)

fun main() {
    println(writeDeathNote(init()))
}
