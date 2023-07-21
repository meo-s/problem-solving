// https://www.acmicpc.net/problem/4686

import java.util.PriorityQueue

typealias OperatorQueue = PriorityQueue<Pair<Int, Node>>

class Node(var prev: Node? = null, var next: Node? = null, var value: String? = null)

fun Node.toString(sb: StringBuilder) {
    sb.append(value!!)
    if (next != null) {
        sb.append(' ')
        next!!.toString(sb)
    }
}

fun String.priority(): Int = when (this) {
    "+" -> 1
    "-" -> 1
    else -> 0
}

fun parseTerm(exp: String, offset: Int): String {
    var i = if (exp[offset] == '+' || exp[offset] == '-') 1 else 0
    while (offset + i < exp.length && exp[offset + i] !in "+-/*") {
        ++i
    }

    return exp.substring(offset, offset + i)
}

fun parseExpression(exp: String): Pair<Node, OperatorQueue> {
    val termsAndOpsHead = Node()
    var termsAndOpsTail = termsAndOpsHead

    val ops = OperatorQueue { lhs, rhs ->
        val lhsOp = lhs.second.value!!
        val rhsOp = rhs.second.value!!

        if (lhsOp.priority() == rhsOp.priority()) {
            lhs.first.compareTo(rhs.first)
        } else {
            lhsOp.priority().compareTo(rhsOp.priority())
        }
    }

    var i = 0
    while (i < exp.length) {
        var term = parseTerm(exp, i)
        i += term.length

        term = if (term.startsWith('+')) term.substring(1) else term
        termsAndOpsTail.next = Node(termsAndOpsTail, value = term)
        termsAndOpsTail = termsAndOpsTail.next!!

        if (i < exp.length) {
            termsAndOpsTail.next = Node(termsAndOpsTail, value = exp[i++].toString())
            termsAndOpsTail = termsAndOpsTail.next!!
            ops.add(Pair(i, termsAndOpsTail))
        }
    }

    return Pair(termsAndOpsHead, ops)
}

fun eval(op: String, lhs: Int, rhs: Int) = when (op) {
    "+" -> lhs + rhs
    "-" -> lhs - rhs
    "*" -> lhs * rhs
    else -> lhs / rhs
}

fun main() = System.`in`.bufferedReader().use { stdin ->
    val sb = StringBuilder()

    while (true) {
        val line = stdin.readLine()
        if (line == null) {
            break
        }

        if (sb.isNotEmpty()) {
            sb.append('\n')
        }

        val (expression, unknown) = line.replace(" ", "").split('=')
        val (termsAndOps, ops) = parseExpression(expression)

        termsAndOps.next!!.toString(sb)
        sb.append(" = $unknown\n")

        while (ops.isNotEmpty()) {
            val (_, iter) = ops.poll()
            val prev = iter.prev!!
            val next = iter.next!!

            prev.prev!!.next = iter
            if (next.next != null) next.next!!.prev = iter

            iter.prev = prev.prev
            iter.next = next.next
            iter.value = eval(iter.value!!, prev.value!!.toInt(), next.value!!.toInt()).toString()

            termsAndOps.next!!.toString(sb)
            sb.append(" = $unknown\n")
        }
    }

    print(sb)
}
