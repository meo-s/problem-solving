// https://www.acmicpc.net/problem/11954

package BJ_11954_NIZOVI

import java.util.Stack

interface Formattable {
    fun format(depth: Int, out: StringBuilder)
}

class Text(private val value: String) : Formattable {
    override fun format(depth: Int, out: StringBuilder) {
        out.append(" ".repeat(2 * depth))
        out.append(value)
    }
}

class Scope : Formattable {
    private val elements = ArrayList<Formattable>()

    fun add(element: Formattable) = elements.add(element)

    override fun format(depth: Int, out: StringBuilder) {
        out.append(" ".repeat(2 * depth))
        out.append("{\n")

        for ((i, e) in elements.withIndex()) {
            e.format(depth + 1, out)
            out.append(if (i < elements.size - 1) ",\n" else "\n")
        }

        out.append(" ".repeat(2 * depth))
        out.append('}')
    }
}

fun beautify() = StringBuilder().run {
    System.`in`.bufferedReader().use { stdin ->
        val sb = StringBuilder()
        val st = Stack<Scope>()

        val popAndAppend = {
            if (sb.isNotEmpty()) {
                st.peek().add(Text(sb.toString()))
                sb.clear()
            }
        }

        while (true) {
            when (val b = stdin.read()) {
                '{'.code -> st.add(Scope())

                '}'.code -> {
                    popAndAppend()
                    if (1 < st.size) {
                        val scope = st.pop()
                        st.peek().add(scope)
                    }
                }

                ','.code -> popAndAppend()

                -1 -> break

                else -> sb.append(b.toChar())
            }
        }

        st.peek().format(0, this@run)
    }

    toString()
}

fun main() {
    println(beautify())
}
