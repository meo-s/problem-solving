// https://www.acmicpc.net/problem/1460

package BJ_1460_진욱이의_농장

import java.io.BufferedReader
import kotlin.math.min

typealias Farm = Array<Array<IntArray>>

fun Farm.query(y: Int, x: Int, h: Int, w: Int, seeds: IntArray) {
    val a = this
    val ny = y + h - 1
    val nx = x + w - 1
    for (i in 0..7) {
        seeds[i] = a[ny][nx][i] - a[ny][x - 1][i] - a[y - 1][nx][i] + a[y - 1][x - 1][i]
    }
}

@Suppress("NAME_SHADOWING")
fun initFarm(br: BufferedReader): Farm {
    val (N, M) = br.readLine().split(' ').map { it.toInt() }

    val m = Array(N + 1) { IntArray(N + 1) }
    repeat(M) {
        val (x, y, L, F) = br.readLine().split(' ').map { it.toInt() }
        for (y in y + 1..min(y + L, N)) {
            for (x in x + 1..min(x + L, N)) {
                m[y][x] = F
            }
        }
    }

    val dp = Array(N + 1) { Array(N + 1) { IntArray(8) } }
    for (y in 1..N) {
        for (x in 1..N) {
            for (i in 0..7) dp[y][x][i] = dp[y - 1][x][i] + dp[y][x - 1][i] - dp[y - 1][x - 1][i]
            ++dp[y][x][m[y][x]]
        }
    }

    return dp
}

fun main() {
    val farm = System.`in`.bufferedReader().use { initFarm(it) }

    val N = farm.size - 1
    var L = 0
    val seeds = IntArray(8)

    for (y in 1..N) {
        if (N < y + L - 1) {
            break
        }

        for (x in 1..N) {
            while (y + L <= N && x + L <= N) {
                farm.query(y, x, L + 1, L + 1, seeds)
                if (seeds[0] != 0 || 2 < seeds.drop(1).count { 1 <= it }) {
                    break
                }

                ++L
            }
        }
    }

    println(L * L)
}
