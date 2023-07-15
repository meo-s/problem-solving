// https://www.acmicpc.net/problem/3902

import java.io.BufferedReader

fun parseSubscriptExpression(exp: String): Pair<String, String> {
    val openBracketIndex = exp.indexOf('[')
    return Pair(exp.substring(0, openBracketIndex), exp.substring(openBracketIndex + 1, exp.length - 1))
}

fun evaluate(ctx: HashMap<String, Pair<Int, HashMap<Int, Int>>>, exp: String): Pair<Boolean, Int> {
    if (exp[0] in '0'..'9') {
        return Pair(true, exp.toInt())
    }

    val (arrayName, indexExp) = parseSubscriptExpression(exp)
    if (arrayName !in ctx) {
        return Pair(false, -1)
    }

    val (err, index) = evaluate(ctx, indexExp)
    if (!err) {
        return Pair(false, -1)
    }

    val (length, elements) = ctx[arrayName]!!
    if (length <= index || index !in elements) {
        return Pair(false, -1)
    }

    return Pair(true, elements[index]!!)
}

fun interpret(br: BufferedReader): Pair<Boolean, Int> {
    var context = HashMap<String, Pair<Int, HashMap<Int, Int>>>()
    var interpretedLines = 0

    while (true) {
        val line = br.readLine()
        if (line == ".") break

        ++interpretedLines

        if (line.indexOf('=') == -1) {
            val (arrayName, arrayLength) = parseSubscriptExpression(line)
            context[arrayName] = Pair(arrayLength.toInt(), HashMap())
        } else {
            val (lhs, rhs) = line.split('=')

            val (rhsEvalErr, valueToAssign) = evaluate(context, rhs)
            if (!rhsEvalErr) {
                return Pair(false, interpretedLines)
            }

            val (arrayName, indexExp) = parseSubscriptExpression(lhs)
            if (arrayName !in context) {
                return Pair(false, interpretedLines)
            }

            val (arrayLength, arrayValues) = context[arrayName]!!

            val (indexEvalErr, index) = evaluate(context, indexExp)
            if (!indexEvalErr || arrayLength <= index) {
                return Pair(false, interpretedLines)
            }

            arrayValues[index] = valueToAssign
        }
    }

    return Pair(true, interpretedLines)
}

fun main() = System.`in`.bufferedReader().use { stdin ->
    val sb = StringBuilder()

    while (true) {
        val (succeeded, interpretedLines) = interpret(stdin)
        if (!succeeded) {
            while (stdin.readLine() != ".");
            sb.append(interpretedLines)
            sb.append('\n')
        } else {
            if (interpretedLines == 0) break
            else sb.append("0\n")
        }
    }

    print(sb.toString())
}
