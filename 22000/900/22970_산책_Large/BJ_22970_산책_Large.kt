// https://www.acmicpc.net/problem/22970

package BJ_22970_산책_Large

import java.util.PriorityQueue
import kotlin.math.*

var s = -1
var e = -1
lateinit var g: Array<MutableList<Pair<Int, Int>>>
lateinit var dists: IntArray
lateinit var prevs: Array<MutableList<Int>>
lateinit var blocked: BooleanArray

fun init() = System.`in`.bufferedReader().use { br ->
    val (n, m) = br.readLine().split(' ').map { it.toInt() }

    g = Array(n) { ArrayList() }
    for (i in 1..m) {
        val (u, v, w) = br.readLine().split(' ').map { it.toInt() }
        g[u - 1].add(Pair(v - 1, w))
        g[v - 1].add(Pair(u - 1, w))
    }

    val tokens = br.readLine().split(' ')
    s = tokens[0].toInt() - 1
    e = tokens[1].toInt() - 1
    dists = IntArray(g.size) { Int.MAX_VALUE }
    prevs = Array(g.size) { ArrayList() }
    blocked = BooleanArray(g.size) { false }
}

fun dijkstra(s: Int, e: Int): Int {
    dists.fill(Int.MAX_VALUE)
    dists[s] = 0

    val waypoints = PriorityQueue<Pair<Int, Int>> { lhs, rhs -> lhs.first.compareTo(rhs.first) }
    waypoints.add(Pair(0, s))

    while (waypoints.isNotEmpty()) {
        val (w0, u) = waypoints.poll()
        if (dists[u] == w0) {
            for ((v, w) in g[u]) if (!blocked[v]) {
                if (w0 + w == dists[v]) {
                    prevs[v].add(u)
                    continue
                }

                if (w0 + w < dists[v]) {
                    prevs[v].clear()
                    prevs[v].add(u)
                    dists[v] = w0 + w
                    if (v != e) waypoints.add(Pair(dists[v], v))
                }
            }
        }
    }

    return dists[e]
}

fun traceBack() {
    val nexts = IntArray(g.size) { Int.MAX_VALUE }
    val visited = BooleanArray(g.size)
    val waypoints = ArrayDeque<Int>()
    waypoints.addLast(e)

    while (waypoints.isNotEmpty()) {
        val u = waypoints.removeFirst()
        for (v in prevs[u]) {
            nexts[v] = min(nexts[v], u)
            if (!visited[v]) {
                visited[v] = true
                waypoints.add(v)
            }
        }
    }

    var u = s
    while (u != e) {
        u = nexts[u]
        blocked[u] = true
    }
}

fun main() {
    init()
    val s2e = dijkstra(s, e)
    traceBack()
    val e2s = dijkstra(e, s)
    println(s2e + e2s)
}
