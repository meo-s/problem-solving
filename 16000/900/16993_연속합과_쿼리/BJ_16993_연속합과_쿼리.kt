// https://www.acmicpc.net/problem/16993

package BJ_16993_연속합과_쿼리

import java.io.BufferedReader
import kotlin.math.*

class SegmentTree(a: IntArray) {

    class RangeInfo(
        var acont: Long = Int.MIN_VALUE.toLong(),
        var rcont: Long = Int.MIN_VALUE.toLong(),
        var lcont: Long = Int.MIN_VALUE.toLong(),
        var ncont: Long = Int.MIN_VALUE.toLong(),
    ) {}

    private val tree = Array(1 shl (ceil(ln(a.size.toDouble()) / ln(2.0)).toInt()) + 1) { RangeInfo() }

    init {
        init(a, 1, 1, a.size + 1)
    }

    private fun init(a: IntArray, node: Int, left: Int, right: Int) {
        if (right - left == 1) {
            tree[node].acont = a[left - 1].toLong()
            tree[node].rcont = a[left - 1].toLong()
            tree[node].lcont = a[left - 1].toLong()
            tree[node].ncont = a[left - 1].toLong()
        } else {
            val mid = (left + right) / 2
            init(a, node * 2, left, mid)
            init(a, node * 2 + 1, mid, right)

            tree[node].acont = tree[node * 2].acont + tree[node * 2 + 1].acont
            tree[node].rcont = max(tree[node * 2 + 1].rcont, tree[node * 2].rcont + tree[node * 2 + 1].acont)
            tree[node].lcont = max(tree[node * 2].lcont, tree[node * 2].acont + tree[node * 2 + 1].lcont)
            tree[node].ncont = max(tree[node * 2].ncont, tree[node * 2 + 1].ncont)
            tree[node].ncont = max(tree[node].ncont, tree[node * 2].rcont + tree[node * 2 + 1].lcont)
        }
    }

    fun query(node: Int, left: Int, right: Int, beg: Int, end: Int): RangeInfo {
        if (right <= beg || end <= left) return RangeInfo()

        if (beg <= left && right <= end) return tree[node]
        else {
            val mid = (left + right) / 2
            val lr = query(node * 2, left, mid, beg, end)
            val rr = query(node * 2 + 1, mid, right, beg, end)

            val rcont = max(rr.rcont, lr.rcont + rr.acont)
            val lcont = max(lr.lcont, lr.acont + rr.lcont)
            var ncont = max(max(lr.ncont, rr.ncont), lr.rcont + rr.lcont)
            return RangeInfo(lr.acont + rr.acont, rcont, lcont, ncont)
        }
    }
}

var N: Int = -1
lateinit var segmentTree: SegmentTree

fun initSegmentTree(br: BufferedReader) {
    N = br.readLine().toInt()

    val tokens = br.readLine().split(' ')
    val a = IntArray(N) { i -> tokens[i].toInt() }
    segmentTree = SegmentTree(a)
}

fun executeQueries(br: BufferedReader) {
    var sb = StringBuilder()
    var numQueries = br.readLine().toInt()
    while (0 <= --numQueries) {
        val (beg, end) = br.readLine().split(' ').map { it.toInt() }
        sb.append(segmentTree.query(1, 1, N + 1, beg, end + 1).ncont)
        sb.append('\n')
    }

    println(sb.toString())
}

fun main() = System.`in`.bufferedReader().use { stdin ->
    initSegmentTree(stdin)
    executeQueries(stdin)
}
