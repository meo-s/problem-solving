// https://www.acmicpc.net/problem/14311

package BJ_14311_Evaluation

fun evaluate(assignments: Array<String>): String {
    val dependencies = HashMap<String, Int>()
    val pendings = HashMap<String, MutableList<String>>()

    for (assignment in assignments) {
        val (varName, varValue) = assignment.split('=')
        val args = varValue.dropLast(1).split("(")[1].split(',').filter { it.isNotEmpty() }

        dependencies[varName] = args.size
        for (arg in args) {
            if (arg !in pendings) pendings[arg] = ArrayList()
            pendings[arg]!!.add(varName)
        }
    }

    val queue = ArrayDeque<String>()

    for ((k, v) in dependencies.entries) {
        if (v == 0) {
            queue.add(k)
        }
    }

    while (queue.isNotEmpty()) {
        val varName = queue.first()
        queue.removeFirst()

        for (pending in pendings[varName] ?: continue) {
            dependencies[pending] = dependencies[pending]!! - 1
            if (dependencies[pending]!! == 0) {
                queue.add(pending)
            }
        }
    }

    return if (dependencies.values.all { it == 0 }) "GOOD" else "BAD"
}

fun main() = System.`in`.bufferedReader().use { stdin ->
    val sb = StringBuilder()

    repeat(stdin.readLine().toInt()) { testCase ->
        val result = evaluate(Array(stdin.readLine().toInt()) { stdin.readLine() })
        sb.append("Case #${testCase + 1}: $result\n")
    }

    print(sb.toString())
}
