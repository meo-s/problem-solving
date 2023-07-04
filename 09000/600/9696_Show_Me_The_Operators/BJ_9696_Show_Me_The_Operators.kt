// https://www.acmicpc.net/problem/9696

import java.util.PriorityQueue
import java.util.Stack

val Priorities = hashMapOf(
    Pair('+', 0),
    Pair('-', 0),
    Pair('*', 1),
    Pair('/', 1),
)

fun execute(exp: String): String {
    val sb = StringBuilder()

    val operands = Stack<Long>()
    val operators = Stack<Pair<Char, Int>>()

    val history = PriorityQueue<Pair<Char, Int>> { lhs, rhs ->
        val opPriority = -Priorities[lhs.first]!!.compareTo(Priorities[rhs.first]!!)
        return@PriorityQueue if (opPriority != 0) opPriority else lhs.second.compareTo(rhs.second)
    }

    val calculateOnce = {
        val rhs = operands.pop()
        val lhs = operands.pop()
        val op = operators.pop()
        when (op.first) {
            '+' -> operands.push(lhs + rhs)
            '-' -> operands.push(lhs - rhs)
            '*' -> operands.push(lhs * rhs)
            '/' -> operands.push(lhs / rhs)
            else -> {}
        }

        history.add(op)
    }

    for ((i, token) in exp.split(' ').withIndex()) {
        if (token[0] in '0'..'9') {
            operands.push(token.toLong())
        } else {
            while (operators.isNotEmpty() && Priorities[token[0]]!! <= Priorities[operators.peek().first]!!) {
                calculateOnce()
            }

            operators.push(Pair(token[0], i))
        }
    }

    while (operators.isNotEmpty()) {
        calculateOnce()
    }

    while (history.isNotEmpty()) {
        val (op, _) = history.poll()
        sb.append(op)
        sb.append(' ')
    }

    sb.append(operands[0])

    return sb.toString()
}

fun main() = System.`in`.bufferedReader().use { stdin ->
    repeat(stdin.readLine().toInt()) { testCase ->
        println("Case #${testCase + 1}: ${execute(stdin.readLine())}")
    }
}
