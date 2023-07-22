// https://www.acmicpc.net/problem/2248

package BJ_2248_이진수_찾기

class nr {
    companion object {
        const val MAX_N = 31
        const val MISS = -1L
        private val dp = Array(MAX_N + 1) { LongArray(MAX_N + 1) { MISS } }

        operator fun invoke(n: Int, r: Int): Long {
            if (dp[n][r] == MISS) {
                if (n < r) {
                    dp[n][r] = 0L
                } else if (r == 0) {
                    dp[n][r] = 1L
                } else if (r == 1) {
                    dp[n][r] = n.toLong()
                } else {
                    dp[n][r] = this(n - 1, r) + this(n - 1, r - 1)
                }
            }

            return dp[n][r]
        }
    }
}

fun findBinary(n: Int, l: Int, k: Long, sb: StringBuilder) {
    if (0 < n) {
        var count = 0L
        for (i in 0..l) {
            count += nr(n - 1, i)
        }

        if (k <= count) {
            sb.append(0)
            findBinary(n - 1, l, k, sb)
        } else {
            sb.append(1)
            findBinary(n - 1, l - 1, k - count, sb)
        }
    }
}

fun main() {
    println(StringBuilder().apply {
        val (n, l, k) = readln().split(' ').map { it.toLong() }
        findBinary(n.toInt(), l.toInt(), k, this)
    })
}
