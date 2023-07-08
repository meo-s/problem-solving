// https://www.acmicpc.net/problem/14014

package BJ_14014_Dudu_of_English

import java.io.BufferedReader

val Prepositions = hashSetOf("to", "into", "onto", "above", "below", "from", "by", "is", "at")
val Vowels = "aeiou"

fun compressWord(word: String): String {
    if (word in Prepositions) {
        return "of"
    }

    val sb = StringBuilder()
    var vowels = word.count { it in Vowels } / 2

    for (c in word) {
        if (0 < vowels && c in Vowels) {
            --vowels
            continue
        }

        if (c in 'a'..'z') {
            sb.append(c)
        }
    }

    return sb.toString()
}

fun translateToDudu(br: BufferedReader) = StringBuilder().run {
    var lineLength = 0

    repeat(br.readLine().toInt()) {
        for (token in br.readLine().split(' ')) {
            if (token.isEmpty()) {
                continue
            }

            val word = compressWord(token.lowercase())
            if (word.isNotEmpty()) {
                if (20 < lineLength) {
                    lineLength = 0
                    append('\n')
                } else if (0 < lineLength) {
                    append(' ')
                }

                lineLength += word.length
                append(word)
            }
        }
    }

    toString()
}

fun main() = System.`in`.bufferedReader().use { stdin ->
    println(translateToDudu(stdin))
}
