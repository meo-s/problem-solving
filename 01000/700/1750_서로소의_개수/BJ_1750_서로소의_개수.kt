// https://www.acmicpc.net/problem/1750

package BJ_1750_서로소의_개수

import kotlin.math.*

const val S = 100_000
const val M = 10_000_003

@Suppress("NAME_SHADOWING")
fun gcd(a: Int, b: Int): Int {
    var (a, b) = Pair(max(a, b), min(a, b))
    while (a % b != 0) {
        val r = a % b
        a = b
        b = r
    }
    return b
}

fun main() = System.`in`.bufferedReader().use { stdin ->
    val dp = ArrayDeque<IntArray>().apply {
        add(IntArray(S + 1) { 0 })
        add(IntArray(S + 1) { 0 })
    }

    repeat(stdin.readLine().toInt()) {
        val prev = dp.last()
        val now = dp.removeFirst().apply { prev.copyInto(this) }

        val n = stdin.readLine().toInt()
        for (i in 1..S) {
            if (0 < prev[i]) {
                val gcd = gcd(i, n)
                now[gcd] = (now[gcd] + prev[i]) % M
            }
        }

        now[n] = (now[n] + 1) % M
        dp.addLast(now)
    }

    println(dp.last()[1])
}
