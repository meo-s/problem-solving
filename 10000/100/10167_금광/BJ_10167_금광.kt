// https://www.acmicpc.net/problem/10167

package BJ_10167_금광

import java.io.BufferedReader
import java.util.TreeSet
import kotlin.math.*

fun log2(n: Double): Double = ln(n) / ln(2.0)

class SegmentTree(N: Int) {

    private class Node(
        var acont: Long = Int.MIN_VALUE.toLong(),
        var rcont: Long = Int.MIN_VALUE.toLong(),
        var lcont: Long = Int.MIN_VALUE.toLong(),
        var ncont: Long = Int.MIN_VALUE.toLong(),
    ) {

        fun clear(initialValue: Long = 0) {
            acont = initialValue
            rcont = initialValue
            lcont = initialValue
            ncont = initialValue
        }

        fun merge(left: Node, right: Node) {
            acont = left.acont + right.acont  // OOOO
            rcont = max(right.rcont, left.rcont + right.acont)  // XXXO
            lcont = max(left.lcont, left.acont + right.lcont)  //  OXXX
            ncont = max(max(left.ncont, right.ncont), left.rcont + right.lcont) // XOOX
        }

    }

    private val tree = Array(1 shl (ceil(log2(N.toDouble())).toInt() + 1)) { Node(0, 0, 0, 0) }

    fun clear() {
        for (node in tree) node.clear()
    }

    fun update(node: Int, left: Int, right: Int, index: Int, dv: Int) {
        if (index !in left until right) return

        if (right - left == 1) tree[node].clear(tree[node].acont + dv.toLong())
        else {
            val mid = (left + right) / 2
            update(node * 2, left, mid, index, dv)
            update(node * 2 + 1, mid, right, index, dv)
            tree[node].merge(tree[node * 2], tree[node * 2 + 1])
        }
    }

    fun query(): Long = tree[1].ncont

}

class Mine(var x: Int, var y: Int, var w: Int)

val mines = ArrayList<Mine>()
lateinit var segmentTree: SegmentTree

fun init(br: BufferedReader) {
    for (i in 1..br.readLine().toInt()) {
        val (x, y, w) = br.readLine().split(' ').map { it.toInt() }
        mines.add(Mine(x, y, w))
    }

    val set = TreeSet<Int>()
    set.addAll(mines.map { it.x })
    for (mine in mines) mine.x = set.indexOf(mine.x) + 1

    set.clear()
    set.addAll(mines.map { it.y })
    for (mine in mines) mine.y = set.indexOf(mine.y) + 1

    segmentTree = SegmentTree(mines.size)
}

fun findMaxProfit(): Long {
    var maxProfit = Long.MIN_VALUE

    for ((primaryAxisOf, auxiliaryAxisOf) in arrayOf(
        Pair({ mine: Mine -> mine.x }, { mine: Mine -> mine.y }),
        Pair({ mine: Mine -> mine.y }, { mine: Mine -> mine.x }),
    )) {
        mines.sortBy(primaryAxisOf)

        for (k in mines.indices) {
            if (0 < k && primaryAxisOf(mines[k]) == primaryAxisOf(mines[k - 1])) continue

            segmentTree.clear()

            var i = k
            while (i < mines.size) {
                var isFirst = true
                while (isFirst || (i < mines.size && primaryAxisOf(mines[i]) == primaryAxisOf(mines[i - 1]))) {
                    isFirst = false
                    segmentTree.update(1, 1, mines.size + 1, auxiliaryAxisOf(mines[i]), mines[i].w)
                    ++i
                }

                maxProfit = max(maxProfit, segmentTree.query())
            }
        }
    }

    return maxProfit
}

fun main() = System.`in`.bufferedReader().use { stdin ->
    init(stdin)
    println(findMaxProfit())
}
