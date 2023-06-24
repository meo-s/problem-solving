// https://www.acmicpc.net/problem/1577

package BJ_1577_도로의_개수

class SharedContext(val w: Int, val h: Int, val edges: Array<Array<BooleanArray>>)

val dxdy = listOf(Pair(1, 0), Pair(0, 1))

fun init() = System.`in`.bufferedReader().use { stdin ->
    val (w, h) = stdin.readLine().split(' ').map { it.toInt() }

    val edges = Array(w + 1) { Array(h + 1) { BooleanArray(2) { true } } }
    repeat(stdin.readLine().toInt()) {
        val (a, b, c, d) = stdin.readLine().split(' ').map { it.toInt() }
        val ab2cd = Pair(c - a, d - b)
        val cd2ab = Pair(a - c, b - d)
        for (i in dxdy.indices) {
            if (dxdy[i] == ab2cd) {
                edges[a][b][i] = false
                break
            }
            if (dxdy[i] == cd2ab) {
                edges[c][d][i] = false
                break
            }
        }
    }

    SharedContext(w, h, edges)
}

@OptIn(ExperimentalUnsignedTypes::class)
fun countPaths(ctx: SharedContext): ULong {
    val dp = Array(ctx.w + 1) { ULongArray(ctx.h + 1) }
    dp[0][0] = 1UL

    for (y in 0..ctx.h) {
        for (x in 0..ctx.w) {
            for (i in dxdy.indices) {
                if (!ctx.edges[x][y][i]) {
                    continue
                }

                val nx = x + dxdy[i].first
                val ny = y + dxdy[i].second
                if (nx <= ctx.w && ny <= ctx.h) {
                    dp[nx][ny] += dp[x][y]
                }
            }
        }
    }

    return dp[ctx.w][ctx.h]
}

fun main() {
    println(countPaths(init()))
}
