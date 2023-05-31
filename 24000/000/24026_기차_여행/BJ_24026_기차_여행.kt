// https://www.acmicpc.net/problem/24026

package BJ_24026_기차_여행

import java.io.BufferedReader
import java.lang.StringBuilder
import kotlin.math.*

class SegmentTree(a: Array<MinMaxPair>) {

    class MinMaxPair(var min: Int = Int.MAX_VALUE, var max: Int = Int.MIN_VALUE) {
        operator fun component1() = min
        operator fun component2() = max
    }

    val N = a.size
    val tree = Array(1 shl ceil(ln(N.toDouble()) / ln(2.0) + 1).toInt()) { MinMaxPair() }

    init {
        init(a, 1, 1, N + 1)
    }

    private fun init(a: Array<MinMaxPair>, node: Int, left: Int, right: Int) {
        if (right - left == 1) {
            tree[node].min = a[left - 1].min
            tree[node].max = a[left - 1].max
        } else {
            val mid = (left + right) / 2
            init(a, node * 2, left, mid)
            init(a, node * 2 + 1, mid, right)
            tree[node].min = min(tree[node * 2].min, tree[node * 2 + 1].min)
            tree[node].max = max(tree[node * 2].max, tree[node * 2 + 1].max)
        }
    }

    private fun query(node: Int, left: Int, right: Int, beg: Int, end: Int): MinMaxPair {
        if (right <= beg || end <= left) return MinMaxPair()
        if (beg <= left && right <= end) return tree[node]
        else {
            val mid = (left + right) / 2
            val lr = query(node * 2, left, mid, beg, end)
            val rr = query(node * 2 + 1, mid, right, beg, end)
            return MinMaxPair(min(lr.min, rr.min), max(lr.max, rr.max))
        }
    }

    fun query(beg: Int, end: Int) = query(1, 1, N + 1, beg, end)

}

val jumps = ArrayList<SegmentTree>()
lateinit var coverages: Array<Array<SegmentTree.MinMaxPair>>

fun init(br: BufferedReader): Int {
    val (N, Q) = br.readLine().split(' ').map { it.toInt() }
    val L = floor(ln(N.toDouble()) / ln(2.0)).toInt()
    coverages = Array(L + 1) { Array(N) { SegmentTree.MinMaxPair() } }

    for (u in 0 until N) {
        val (beg, end) = br.readLine().split(' ').map { it.toInt() }
        coverages[0][u].min = beg
        coverages[0][u].max = end
    }
    jumps.add(SegmentTree(coverages[0]))

    for (i in 1..L) {
        for (u in 0 until N) {
            val (beg, end) = coverages[i - 1][u]
            coverages[i][u] = jumps.last().query(beg, end + 1)
        }
        jumps.add(SegmentTree(coverages[i]))
    }

    return Q
}

fun executeQueries(br: BufferedReader, numQueries: Int) {
    val sb = StringBuilder()
    repeat(numQueries) {
        var (start, goal) = br.readLine().split(' ').map { it.toInt() }

        var numTransfers = 0
        if (start != goal) {
            var (beg, end) = coverages[0][start - 1]
            ++numTransfers

            for (i in jumps.indices.reversed()) {
                val (nextBeg, nextEnd) = jumps[i].query(beg, end + 1)
                if (goal !in nextBeg..nextEnd) {
                    numTransfers += 1 shl i
                    beg = nextBeg
                    end = nextEnd
                }
            }

            if (goal !in beg..end) {
                val (nextBeg, nextEnd) = jumps[0].query(beg, end + 1)
                ++numTransfers
                beg = nextBeg
                end = nextEnd
            }

            if (goal !in beg..end) {
                numTransfers = -1
            }
        }

        sb.append(numTransfers)
        sb.append('\n')
    }

    println(sb.toString())
}

fun main() = System.`in`.bufferedReader().use { stdin ->
    executeQueries(stdin, init(stdin))
}
