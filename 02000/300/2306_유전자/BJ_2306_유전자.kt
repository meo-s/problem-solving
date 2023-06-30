// https://www.acmicpc.net/problem/2306

import kotlin.math.max

@Suppress("NAME_SHADOWING")
fun longest(dna: String, dp: Array<IntArray>? = null, beg: Int = -1, end: Int = -1): Int {
    if (dp == null) {
        val dp = Array(dna.length) { IntArray(dna.length) { -1 } }
        return longest(dna, dp, 0, dna.length - 1)
    }

    if (end <= beg) {
        return 0
    }

    if (dp[beg][end] == -1) {
        for (i in beg + 1 until end) {
            dp[beg][end] = max(dp[beg][end], longest(dna, dp, beg, i) + longest(dna, dp, i + 1, end))
        }

        if ((dna[beg] == 'a' && dna[end] == 't') || (dna[beg] == 'g' && dna[end] == 'c')) {
            dp[beg][end] = max(dp[beg][end], longest(dna, dp, beg + 1, end - 1) + 2)
        }

        dp[beg][end] = max(dp[beg][end], longest(dna, dp, beg + 1, end))
    }

    return dp[beg][end]
}

fun main() {
    println(longest(readln()))
}
