// https://www.acmicpc.net/problem/2229

package BJ_2229_조_짜기

import kotlin.math.max
import kotlin.math.min

data class SharedContext(val a: IntArray, val dp: IntArray)

fun init() = System.`in`.bufferedReader().use { stdin ->
    stdin.readLine()
    val a = stdin.readLine().split(' ').map { it.toInt() }.toIntArray()
    SharedContext(a, IntArray(a.size) { -1 })
}

fun teamUp(ctx: SharedContext, offset: Int = 0): Int = when {
    ctx.a.size == offset -> 0
    ctx.dp[offset] != -1 -> ctx.dp[offset]
    else -> {
        val (a, dp) = ctx
        var minScore = Int.MAX_VALUE
        var maxScore = Int.MIN_VALUE

        for (i in offset until a.size) {
            minScore = min(minScore, a[i])
            maxScore = max(maxScore, a[i])
            dp[offset] = max(dp[offset], (maxScore - minScore) + teamUp(ctx, i + 1))
        }

        dp[offset]
    }
}

fun main() {
    println(teamUp(init()))
}
