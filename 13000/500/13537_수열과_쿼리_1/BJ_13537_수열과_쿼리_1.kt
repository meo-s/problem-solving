// https://www.acmicpc.net/problem/13537

package BJ_13537_수열과_쿼리

import java.io.BufferedReader

class SegmentTree(val a: List<Int>) {

    private val size = a.size
    private val root = Node()

    init {
        sort(root, 0, a.size, a)
    }

    private fun merge(node: Node) {
        var lp = 0
        var rp = 0
        while (lp < node.l.values.size && rp < node.r.values.size) {
            if (node.l.values[lp] < node.r.values[rp]) {
                node.values.add(node.l.values[lp++])
            } else {
                node.values.add(node.r.values[rp++])
            }
        }
        while (lp < node.l.values.size) {
            node.values.add(node.l.values[lp++])
        }
        while (rp < node.r.values.size) {
            node.values.add(node.r.values[rp++])
        }
    }


    private fun sort(node: Node, left: Int, right: Int, a: List<Int>) {
        if (right - left == 1) {
            node.values.add(a[left])
        } else {
            val mid = (left + right) / 2
            sort(node.l, left, mid, a)
            sort(node.r, mid, right, a)
            merge(node)
        }
    }

    private fun query(node: Node, left: Int, right: Int, beg: Int, end: Int, k: Int): Int {
        if (end <= left || right <= beg) return 0

        if (beg <= left && right <= end) {
            var lb = 0
            var ub = node.values.size
            var index = node.values.size

            while (lb < ub) {
                val mid = (lb + ub) / 2
                if (k < node.values[mid]) {
                    index = mid
                    ub = mid
                } else {
                    lb = mid + 1
                }
            }

            return node.values.size - index
        } else {
            val mid = (left + right) / 2
            return query(node.l, left, mid, beg, end, k) + query(node.r, mid, right, beg, end, k)
        }
    }

    fun query(beg: Int, end: Int, k: Int) = query(root, 0, size, beg, end, k)

    private class Node {
        private var _l: Node? = null
        val l: Node
            get() {
                if (_l == null) _l = Node()
                return _l!!
            }

        private var _r: Node? = null
        val r: Node
            get() {
                if (_r == null) _r = Node()
                return _r!!
            }

        val values = ArrayList<Int>()
    }

}

lateinit var tree: SegmentTree

fun init(br: BufferedReader) {
    val N = br.readLine().toInt()
    val a = br.readLine().split(' ').map { it.toInt() }.toList()
    tree = SegmentTree(a)
}

fun executeQueries(br: BufferedReader) {
    val ans = StringBuilder()
    val Q = br.readLine().toInt()
    for (q in 1..Q) {
        val (i, j, k) = br.readLine().split(' ').map { it.toInt() }
        ans.append(tree.query(i - 1, j, k))
        ans.append('\n')
    }

    println(ans.toString())
}

fun main() = System.`in`.bufferedReader().use { stdin ->
    init(stdin)
    executeQueries(stdin)
}
