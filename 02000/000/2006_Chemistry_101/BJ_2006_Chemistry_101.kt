// https://www.acmicpc.net/problem/2006

package BJ_2006_Chemistry_101

import kotlin.math.abs

val MoleculeRegex = Regex("^(\\d*)(.+)$")

fun analyzeMolecule(molecule: String, out: HashMap<String, Int>) {
    val (_, head, body) = MoleculeRegex.find(molecule)!!.groupValues
    val coeff = if (head.isBlank()) 1 else head.toInt()

    var atomCount = 1
    val atomNameBuilder = StringBuilder()

    val commitAtomInfo = {
        val atomName = atomNameBuilder.toString()
        out[atomName] = (out[atomName] ?: 0) + atomCount * coeff;

        atomNameBuilder.clear()
        atomCount = 1
    }

    var i = 0
    while (i < body.length) {
        if (body[i].isDigit()) {
            atomCount = 0
            while (i < body.length && body[i].isDigit()) {
                atomCount = atomCount * 10 + (body[i] - '0')
                ++i
            }

            commitAtomInfo()
            continue
        } else {
            atomNameBuilder.append(body[i])
            if ((i + 1 == body.length || body[i + 1].isUpperCase())) {
                commitAtomInfo()
            }
        }

        ++i
    }
}

@Suppress("NAME_SHADOWING")
fun analyzeEquation(equation: String): List<Pair<String, Int>> {
    val (lhs, rhs) = equation.replace(" ", "").split("=")

    val lhsInfo = HashMap<String, Int>()
    lhs.split('+').forEach { analyzeMolecule(it, lhsInfo) }
    val rhsInfo = HashMap<String, Int>()
    rhs.split('+').forEach { analyzeMolecule(it, rhsInfo) }

    for ((k, v) in lhsInfo.entries) {
        lhsInfo[k] = 0
        rhsInfo[k] = (rhsInfo[k] ?: 0) - v
    }

    return rhsInfo.entries
        .filter { (_, v) -> v != 0 }
        .map { (k, v) -> Pair(k, v) }
        .toList()
        .sortedBy { it.first }
}

fun main() = System.`in`.bufferedReader().use { stdin ->
    val sb = StringBuilder()
    var equationIndex = 1

    while (true) {
        val equation = stdin.readLine()
        if (equation == "#") {
            break
        }

        val unbalances = analyzeEquation(equation)
        if (unbalances.isEmpty()) {
            sb.append("Equation $equationIndex is balanced.\n")
        } else {
            sb.append("Equation $equationIndex is unbalanced.\n")
            for ((k, v) in unbalances) {
                sb.append("You have ${if (v < 0) "destroyed" else "created"} ${abs(v)} atom${if (1 < abs(v)) "s" else ""} of $k.\n")
            }

            sb.append('\n')
        }

        ++equationIndex;
    }

    print(sb.toString())
}
