// https://www.acmicpc.net/problem/18240

import java.io.BufferedReader
import kotlin.system.exitProcess

var nodeValue = 0
lateinit var g: Array<MutableList<Int>>
lateinit var a: IntArray
lateinit var availables: Array<ArrayDeque<Int>>

fun readInt(br: BufferedReader): Int {
    var n = 0
    while (true) {
        val b = br.read()
        if (b == ' '.code || b == '\n'.code || b == -1) return n
        n = n * 10 + (b - '0'.code)
    }
}

fun init(br: BufferedReader) {
    g = Array(readInt(br)) { ArrayList<Int>() }
    a = IntArray(g.size) { -1 }
    availables = Array(g.size) { ArrayDeque<Int>() }
    availables[0].add(0)
}

fun inorder(u: Int = 0) {
    if (0 < g[u].size) inorder(g[u][0])
    a[u] = ++nodeValue
    if (1 < g[u].size) inorder(g[u][1])
}

fun solve(br: BufferedReader) {
    for (node in 1 until g.size) {
        val depth = readInt(br) - 1
        if (availables[depth].isEmpty()) {
            println(-1)
            exitProcess(0)
        }

        availables[depth + 1].add(node)
        g[availables[depth].first()].add(node)
        if (g[availables[depth].first()].size == 2) availables[depth].removeFirst()
    }

    inorder()

    val sb = StringBuilder()
    a.forEach { sb.append(it); sb.append(' ') }
    println(sb.toString())
}

fun main() = System.`in`.bufferedReader().use {
    init(it)
    solve(it)
}
