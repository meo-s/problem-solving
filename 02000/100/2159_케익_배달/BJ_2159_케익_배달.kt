// https://www.acmicpc.net/problem/2159

package BJ_2159_케익_배달

import kotlin.math.abs
import kotlin.math.min

private const val D = 5
private const val C = 100_000
private val dy = intArrayOf(0, 1, 0, -1, 0)
private val dx = intArrayOf(0, 0, 1, 0, -1)

fun main() = System.`in`.bufferedReader().use { stdin ->
    val N = stdin.readLine().toInt()
    var (px, py) = stdin.readLine().split(' ').map { it.toInt() }

    val dp = Array(N + 1) { LongArray(D) { C.toLong() * 2 * 100_000 } }.apply {
        this[0][0] = 0
    }

    for (k in 1..N) {
        val (x, y) = stdin.readLine().split(' ').map { it.toInt() }

        for (i in 0 until D) {
            val xx = x + dx[i]
            val yy = y + dy[i]
            if (xx !in 1..C || yy !in 1..C) {
                continue
            }

            for (j in 0 until D) {
                val pxx = px + dx[j]
                val pyy = py + dy[j]
                if (pxx !in 1..C || pyy !in 1..C) {
                    continue
                }

                dp[k][i] = min(dp[k][i], dp[k - 1][j] + abs(xx - pxx) + abs(yy - pyy))
            }
        }

        px = x
        py = y
    }

    println(dp.last().min())
}
