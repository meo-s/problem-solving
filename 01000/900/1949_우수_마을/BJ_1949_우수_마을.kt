// https://www.acmicpc.net/problem/1949

package BJ_1949_우수_마을

import kotlin.math.max

class SharedContext(val g: Array<Pair<Int, ArrayList<Int>>>, val dp: Array<IntArray>)

fun initGraph() = System.`in`.bufferedReader().use { stdin ->
    val N = stdin.readLine().toInt()
    val tokens = stdin.readLine().split(' ')

    val g = Array(N) { Pair(tokens[it].toInt(), ArrayList<Int>()) }
    repeat(g.size - 1) {
        val (u, v) = stdin.readLine().split(' ').map { it.toInt() }
        g[u - 1].second.add(v - 1)
        g[v - 1].second.add(u - 1)
    }

    g
}

fun find(ctx: SharedContext, u: Int = 0, p: Int = -1, type: Int = -1): Int {
    if (type == -1) {
        return max(find(ctx, u, p, 0), find(ctx, u, p, 1))
    }

    if (ctx.dp[type][u] == -1) {
        val nextType = if (type == 1) 0 else -1
        ctx.dp[type][u] = if (type == 1) ctx.g[u].first else 0

        for (v in ctx.g[u].second) {
            if (v != p) {
                ctx.dp[type][u] += find(ctx, v, u, nextType)
            }
        }
    }

    return ctx.dp[type][u]
}

fun main() {
    val g = initGraph()
    val dp = Array(2) { IntArray(g.size) { -1 } }
    println(find(SharedContext(g, dp), type = -1))
}
