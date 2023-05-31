// https://www.acmicpc.net/problem/10868

package BJ_10868_최솟값

import java.io.BufferedReader
import kotlin.math.*

class SegmentTree(a: IntArray) {
    private val N = a.size
    private val tree = IntArray(1 shl (ceil(ln(a.size.toDouble()) / ln(2.0)) + 1).toInt())

    init {
        init(a, 1, 1, a.size + 1)
    }

    private fun init(a: IntArray, node: Int, left: Int, right: Int) {
        if (right - left == 1) tree[node] = a[left - 1]
        else {
            val mid = (left + right) / 2
            init(a, node * 2, left, mid)
            init(a, node * 2 + 1, mid, right)
            tree[node] = min(tree[node * 2], tree[node * 2 + 1])
        }
    }

    private fun query(node: Int, left: Int, right: Int, beg: Int, end: Int): Int {
        if (right <= beg || end <= left) return Int.MAX_VALUE
        if (beg <= left && right <= end) return tree[node]
        else {
            val mid = (left + right) / 2
            val lr = query(node * 2, left, mid, beg, end)
            val rr = query(node * 2 + 1, mid, right, beg, end)
            return min(lr, rr)
        }
    }

    fun query(beg: Int, end: Int) = query(1, 1, N + 1, beg, end)
}

lateinit var segmentTree: SegmentTree

fun init(br: BufferedReader): Int {
    val (N, Q) = br.readLine().split(' ').map { it.toInt() }
    val a = IntArray(N) { br.readLine().toInt() }
    segmentTree = SegmentTree(a)
    return Q
}

fun solve(br: BufferedReader, numQueries: Int) {
    val sb = StringBuilder()
    repeat(numQueries) {
        val (beg, end) = br.readLine().split(' ').map { it.toInt() }
        sb.append(segmentTree.query(beg, end + 1))
        sb.append('\n')
    }

    print(sb.toString())
}

fun main() = System.`in`.bufferedReader().use { stdin ->
    solve(stdin, init(stdin))
}
