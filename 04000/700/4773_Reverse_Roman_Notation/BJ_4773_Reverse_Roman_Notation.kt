// https://www.acmicpc.net/problem/4773

package BJ_4773_Reverse_Roman_Notation

import java.util.Stack

val RomanNumerals = listOf(
    Pair("M", 1000),
    Pair("CM", 900),
    Pair("D", 500),
    Pair("CD", 400),
    Pair("C", 100),
    Pair("XC", 90),
    Pair("L", 50),
    Pair("XL", 40),
    Pair("X", 10),
    Pair("IX", 9),
    Pair("V", 5),
    Pair("IV", 4),
    Pair("I", 1),
)

val RomanNumeralToInt = hashMapOf(
    Pair('I', 1),
    Pair('V', 5),
    Pair('X', 10),
    Pair('L', 50),
    Pair('C', 100),
    Pair('D', 500),
    Pair('M', 1000),
)

fun Long.toRoman() = StringBuilder().run {
    var n = this@toRoman
    for ((k, v) in RomanNumerals) {
        if (n == 0L) break
        while (v <= n) {
            append(k)
            n -= v
        }
    }

    toString()
}

fun String.toLongFromRomanNumeral(): Long {
    var n = 0L
    val numerals = this.map { RomanNumeralToInt[it]!! }.toIntArray()

    var i = 0
    while (i < numerals.size) {
        if (i + 1 < numerals.size && numerals[i] < numerals[i + 1]) {
            n += numerals[i + 1] - numerals[i]
            i += 2
        } else {
            n += numerals[i++]
        }
    }

    return n
}

fun main() = System.`in`.bufferedReader().use { stdin ->
    val sb = StringBuilder()
    val st = Stack<Long>()
    val checkStack = { if (st.size < 2) sb.append("stack underflow\n"); 2 <= st.size }

    while (true) when (val s = stdin.readLine()) {
        null -> break

        else -> when (s[0]) {
            '=' -> {
                if (st.empty()) sb.append("stack underflow\n")
                else if (st.peek() !in 1L..4999L) sb.append("out of range exception\n")
                else {
                    sb.append(st.peek().toRoman())
                    sb.append('\n')
                }
            }

            '+' -> if (checkStack()) st.push(st.pop() + st.pop())

            '*' -> if (checkStack()) st.push(st.pop() * st.pop())

            '-' -> if (checkStack()) {
                val rhs = st.pop()
                val lhs = st.pop()
                st.push(lhs - rhs)
            }

            '/' -> if (checkStack()) {
                val divisor = st.pop()
                val dividend = st.pop()
                if (divisor != 0L) {
                    st.push(dividend / divisor)
                } else {
                    sb.append("division by zero exception\n")
                    st.push(dividend)
                }
            }

            else -> st.push(s.toLongFromRomanNumeral())
        }
    }

    print(sb.toString())
}
