// https://www.acmicpc.net/problem/16074

package BJ_16074_Mountaineers

import java.io.BufferedReader
import java.lang.StringBuilder
import kotlin.math.*

class DisjointSet(size: Int) {

    private var parents = IntArray(size) { it }

    fun clear() {
        for (i in parents.indices) parents[i] = i
    }

    fun find(u: Int): Int {
        if (parents[u] != u) parents[u] = find(parents[u])
        return parents[u]
    }

    fun union(u: Int, v: Int): Boolean {
        val up = find(u)
        val vp = find(v)
        if (up != vp) {
            parents[vp] = up
            return true
        }
        return false
    }

}

var H = -1
var W = -1
var numQueries = -1
val edges = Array((1e6 + 1).toInt()) { ArrayList<Pair<Int, Int>>() }
lateinit var m: Array<IntArray>

fun flatten(y: Int, x: Int) = y * W + x

fun readInt(br: BufferedReader): Int {
    var n = 0
    while (true) {
        val b = br.read()
        if (b == '\n'.code || b == ' '.code || b == -1) return n
        else n = n * 10 + (b - '0'.code)
    }
}

fun init(br: BufferedReader) {
    H = readInt(br)
    W = readInt(br)
    numQueries = readInt(br)

    m = Array(H) { IntArray(W) }
    for (y in 0 until H) for (x in 0 until W) m[y][x] = readInt(br)
    for (y in 0 until H) for (x in 0 until W) {
        if (x + 1 < W) edges[max(m[y][x], m[y][x + 1])].add(Pair(flatten(y, x), flatten(y, x + 1)))
        if (y + 1 < H) edges[max(m[y][x], m[y + 1][x])].add(Pair(flatten(y, x), flatten(y + 1, x)))
    }
}

fun executeQueries(br: BufferedReader) {
    val queryResults = IntArray(numQueries)

    val lb = IntArray(numQueries) { 1 }
    val ub = IntArray(numQueries) { (1e6 + 1).toInt() }
    val disjointSet = DisjointSet(H * W)
    val queryQueue = Array((1e6 + 1).toInt()) { ArrayDeque<Int>() }
    val queries = Array(numQueries) { queryIndex ->
        val (uy, ux, vy, vx) = br.readLine().split(' ').map { it.toInt() }
        val u = flatten(uy - 1, ux - 1)
        val v = flatten(vy - 1, vx - 1)
        if (u == v) {
            ub[queryIndex] = 0
            queryResults[queryIndex] = m[uy - 1][ux - 1]
        }
        Pair(u, v)
    }
    while (true) {
        var numRemainedQueries = 0
        for (i in 0 until numQueries) {
            if (lb[i] < ub[i]) {
                queryQueue[(lb[i] + ub[i]) / 2].addLast(i)
                ++numRemainedQueries
            }
        }

        if (numRemainedQueries == 0) break

        disjointSet.clear()
        for (i in 1..1e6.toInt()) {
            if (numRemainedQueries == 0) break

            for ((u, v) in edges[i]) disjointSet.union(u, v)
            while (!queryQueue[i].isEmpty()) {
                val queryIndex = queryQueue[i].removeFirst()
                --numRemainedQueries

                val (u, v) = queries[queryIndex]
                if (disjointSet.find(u) == disjointSet.find(v)) {
                    ub[queryIndex] = i
                    queryResults[queryIndex] = i
                } else {
                    lb[queryIndex] = i + 1
                }
            }
        }
    }

    val sb = StringBuilder()
    queryResults.forEach { sb.append(it); sb.append('\n') }
    print(sb.toString())
}

fun main() = System.`in`.bufferedReader().use { stdin ->
    init(stdin)
    executeQueries(stdin)
}
