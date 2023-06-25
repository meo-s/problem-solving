// https://www.acmicpc.net/problem/1796

package BJ_1796_신기한_키보드

import kotlin.math.min

class SharedContext(val s: String, val alphabets: Array<ArrayList<Int>>, val dp: Array<IntArray>)

fun init(): SharedContext {
    val s = readln()
    val alphabets = Array('z'.code - 'a'.code + 1) { ArrayList<Int>() }
    for ((i, c) in s.withIndex()) {
        alphabets[c.code - 'a'.code].add(i)
    }

    return SharedContext(s, alphabets, Array('z'.code - 'a'.code + 1) { IntArray(50) { -1 } })
}

fun upload(ctx: SharedContext, depth: Int = 0, cursor: Int = 0): Int {
    if (ctx.alphabets.size == depth) {
        return 0
    }

    if (ctx.alphabets[depth].isEmpty()) {
        return upload(ctx, depth + 1, cursor)
    }

    if (ctx.dp[depth][cursor] == -1) {
        val first = ctx.alphabets[depth][0]
        val last = ctx.alphabets[depth].last()

        var lcost: Int
        if (first < cursor) {
            lcost = cursor - first
            if (cursor < last) {
                lcost += (last - first) + upload(ctx, depth + 1, last)
            } else {
                lcost += upload(ctx, depth + 1, first)
            }
        } else {
            lcost = (last - cursor) + upload(ctx, depth + 1, last)
        }

        var rcost: Int
        if (cursor < last) {
            rcost = last - cursor
            if (first < cursor) {
                rcost += (last - first) + upload(ctx, depth + 1, first)
            } else {
                rcost += upload(ctx, depth + 1, last)
            }
        } else {
            rcost = (cursor - first) + upload(ctx, depth + 1, first)
        }

        ctx.dp[depth][cursor] = min(lcost, rcost) + ctx.alphabets[depth].size
    }

    return ctx.dp[depth][cursor]
}

fun main() {
    println(upload(init()))
}
