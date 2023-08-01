// https://www.acmicpc.net/problem/1808

package BJ_1808_숌작업

import java.io.BufferedReader
import kotlin.math.max

fun initTree(br: BufferedReader): Array<List<Int>> {
    return Array(br.readLine().toInt()) { ArrayList<Int>() }.run {
        repeat(size - 1) {
            val (u, v) = br.readLine().split(' ').map { it.toInt() }
            get(u).add(v)
        }
        this as Array<List<Int>>
    }
}

fun measureMaxHeight(g: Array<List<Int>>, heights: IntArray, u: Int = 0, depth: Int = 0): Int {
    heights[u] = depth
    for (v in g[u]) {
        heights[u] = max(heights[u], measureMaxHeight(g, heights, v, depth + 1))
    }
    return heights[u]
}

fun pullUp(g: Array<List<Int>>, heights: IntArray, H: Int, u: Int = 0, depth: Int = 0, dh: Int = 0): Int {
    if (heights[u] - dh <= H) {
        return 0
    }

    var cost = if (1 < depth) 1 else 0
    for (v in g[u]) {
        cost += pullUp(g, heights, H, v, depth + 1, dh + if (1 < depth) 1 else 0)
    }

    return cost
}

fun main() = System.`in`.bufferedReader().use { stdin ->
    val g = initTree(stdin)
    val H = stdin.readLine().toInt()
    val heights = IntArray(g.size) { 0 }.apply { measureMaxHeight(g, this) }

    println(pullUp(g, heights, H))
}
