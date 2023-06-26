// https://www.acmicpc.net/problem/2253

package BJ_2253_점프

import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.sqrt

class SharedContext(val n: Int, val usable: BooleanArray)

fun init(): SharedContext = System.`in`.bufferedReader().use { stdin ->
    val (n, m) = stdin.readLine().split(' ').map { it.toInt() }

    val usable = BooleanArray(n) { true }
    repeat(m) {
        usable[stdin.readLine().toInt() - 1] = false
    }

    SharedContext(n, usable)
}

fun cross(ctx: SharedContext): Int {
    val L = ceil(sqrt(2.0 * ctx.n)).toInt()
    val dp = Array(ctx.n) { IntArray(L) { Int.MAX_VALUE } }
    dp[0][0] = 0

    for (i in 0 until ctx.n - 1) {
        if (!ctx.usable[i]) {
            continue
        }

        for (x in 0 until L) {
            if (dp[i][x] != Int.MAX_VALUE) {
                if (0 < x - 1 && i + (x - 1) < ctx.n) {
                    dp[i + (x - 1)][x - 1] = min(dp[i + (x - 1)][x - 1], dp[i][x] + 1)
                }
                if (x + 1 < L && i + (x + 1) < ctx.n) {
                    dp[i + (x + 1)][x + 1] = min(dp[i + (x + 1)][x + 1], dp[i][x] + 1)
                }
                if (i + x < ctx.n) {
                    dp[i + x][x] = min(dp[i + x][x], dp[i][x] + 1)
                }
            }
        }
    }

    val ans = dp.last().min()
    return if (ans == Int.MAX_VALUE) -1 else ans
}

fun main() {
    println(cross(init()))
}
