// https://www.acmicpc.net/problem/9519

package BJ_9519_졸려

fun nextWord(word: String): String {
    val sb = StringBuilder()
    var head = 0
    var tail = word.length - 1
    while (head <= tail) {
        sb.append(word[head++])
        if (head <= tail) {
            sb.append(word[tail--])
        }
    }

    return sb.toString()
}


fun main() {
    val x = readln().toInt()
    val words = mutableListOf(readln())

    while (true) {
        val word = nextWord(words.last())
        if (word == words.first()) {
            break
        }
        words.add(word)
    }

    println(words[(words.size - (x % words.size)) % words.size])
}
