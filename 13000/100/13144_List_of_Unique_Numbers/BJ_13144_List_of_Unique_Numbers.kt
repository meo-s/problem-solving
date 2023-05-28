// https://www.acmicpc.net/problem/13144

import java.io.BufferedReader

lateinit var A: IntArray

fun initSequence(br: BufferedReader) {
    A = IntArray(br.readLine().toInt())
    for ((i, token) in br.readLine().split(' ').withIndex()) A[i] = token.toInt()
}

fun solve() {
    var k = 0;
    var diversity = 0L;
    val isInSequence = BooleanArray(100_001)
    for (i in A.indices) {
        while (isInSequence[A[i]]) isInSequence[A[k++]] = false
        isInSequence[A[i]] = true
        diversity += 1 + (i - k)
    }
    println(diversity)
}

fun main() = System.`in`.bufferedReader().use {
    initSequence(it)
    solve()
}
