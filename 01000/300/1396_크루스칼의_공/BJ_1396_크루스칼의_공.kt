// https://www.acmicpc.net/problem/1396

package BJ_1396_크루스칼의_공

import java.io.BufferedReader
import kotlin.math.*

var V = -1
var E = -1
lateinit var g: Array<Triple<Int, Int, Int>>
lateinit var mst: Array<MutableList<Pair<Int, Int>>>
lateinit var lca: LCA

class DisjointSet(private val N: Int) {

    private val parents = IntArray(N) { it }
    private val setSizes = IntArray(N) { 1 }

    fun find(u: Int): Int {
        if (parents[u] != u) parents[u] = find(parents[u])
        return parents[u]
    }

    fun union(u: Int, v: Int) {
        val up = find(u)
        val vp = find(v)
        if (up != vp) {
            parents[vp] = up
            setSizes[up] += setSizes[vp]
            setSizes[vp] = 0
        }
    }

    fun sizeOf(u: Int): Int = setSizes[find(u)]

}

class LCA {

    private val L = 1 + floor(ln(V.toDouble()) / ln(2.0)).toInt()
    private val depths = IntArray(V) { -1 }
    private val jumps = Array(V) { IntArray(L) { -1 } }
    private val costs = Array(V) { IntArray(L) { 0 } }

    init {
        for (i in 0 until V) if (depths[i] == -1) dfs(i, i)
        for (i in 1 until L) {
            for (u in 0 until V) {
                jumps[u][i] = jumps[jumps[u][i - 1]][i - 1]
                costs[u][i] = max(costs[jumps[u][i - 1]][i - 1], costs[u][i - 1])
            }
        }
    }

    private fun dfs(u: Int, p: Int, depth: Int = 0) {
        depths[u] = depth
        jumps[u][0] = p
        for ((v, w) in mst[u]) {
            if (v != p) {
                costs[v][0] = w
                dfs(v, u, depth + 1)
            }
        }
    }

    fun query(u: Int, v: Int): Int {
        var (u, v) = if (depths[v] <= depths[u]) Pair(u, v) else Pair(v, u)

        var maxCost = 0
        for (i in L - 1 downTo 0) {
            if (depths[v] == depths[u]) break
            if (depths[v] <= depths[jumps[u][i]]) {
                maxCost = max(maxCost, costs[u][i])
                u = jumps[u][i]
            }
        }
        for (i in L - 1 downTo 0) {
            if (jumps[u][i] != jumps[v][i]) {
                maxCost = maxOf(maxCost, costs[u][i], costs[v][i])
                u = jumps[u][i]
                v = jumps[v][i]
            }
        }
        if (u != v && jumps[u][0] == jumps[v][0]) {
            maxCost = maxOf(maxCost, costs[u][0], costs[v][0])
            u = jumps[u][0]
            v = jumps[v][0]
        }

        return if (u == v) maxCost else -1
    }

}

fun readInt(br: BufferedReader): Int = with(br) {
    var n = 0
    while (true) {
        val b = read()
        if (b == ' '.code || b == '\n'.code || b == -1) break
        n = n * 10 + (b - '0'.code)
    }
    n
}

fun initGraph(br: BufferedReader) {
    V = readInt(br)
    E = readInt(br)
    g = Array(E) { Triple<Int, Int, Int>(readInt(br) - 1, readInt(br) - 1, readInt(br)) }
}

fun composeMst() {
    mst = Array(V) { ArrayList() }
    with(DisjointSet(V)) {
        g.sortBy { it.third }
        for ((u, v, w) in g) {
            if (find(u) != find(v)) {
                union(u, v)
                mst[u].add(Pair(v, w))
                mst[v].add(Pair(u, w))
            }
        }
    }
}

fun doOfflineQuery(br: BufferedReader) {
    lca = LCA()
    val numQueries = readInt(br)
    val pendingQueries = Array(1_000_001) { ArrayList<Int>() }

    var optCosts = IntArray(numQueries) { -1 }
    var optSizes = IntArray(numQueries) { 0 }

    for (queryIndex in 0 until numQueries) {
        val u = readInt(br) - 1
        val v = readInt(br) - 1
        val minCost = lca.query(u, v)
        if (0 < minCost) {
            pendingQueries[minCost].add(queryIndex)
            optCosts[queryIndex] = minCost
        }
    }

    with(DisjointSet(V)) {
        for ((u, v, w) in g) {  // g는 이미 간선 비용을 기준으로 오름차순으로 정렬되어있다.
            union(u, v)
            for (queryIndex in pendingQueries[w]) {
                optSizes[queryIndex] = sizeOf(u)
            }
        }
    }

    val sb = StringBuilder()
    for (i in 0 until numQueries) {
        sb.append(optCosts[i])
        if (0 < optCosts[i]) {
            sb.append(' ')
            sb.append(optSizes[i])
        }
        sb.append('\n')
    }

    print(sb.toString())
}

fun main() = System.`in`.bufferedReader().use() {
    initGraph(it)
    composeMst()
    doOfflineQuery(it)
}
