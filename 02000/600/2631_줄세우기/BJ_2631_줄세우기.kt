// https://www.acmicpc.net/problem/2631

package BJ_2631_줄세우기

@Suppress("NAME_SHADOWING")
fun upperBound(a: IntArray, v: Int, lb: Int = 0, ub: Int = a.size): Int {
    var (lb, ub) = Pair(lb, ub)
    while (lb < ub) {
        val mid = (lb + ub) / 2
        if (v <= a[mid]) ub = mid
        else lb = mid + 1
    }
    return lb
}

fun lis(a: IntArray): Int {
    var len = 1
    val dp = IntArray(a.size); dp[0] = a[0]
    for (n in a.asIterable().drop(1)) {
        if (dp[len - 1] < n) {
            dp[len++] = n
        } else {
            dp[upperBound(dp, n, ub = len - 1)] = n
        }
    }

    return len
}

fun main() = System.`in`.bufferedReader().use { stdin ->
    val a = IntArray(stdin.readLine().toInt()) { stdin.readLine().toInt() }
    println(a.size - lis(a))
}
