// https://www.acmicpc.net/problem/16500

package BJ_16500_문자열_판별

fun main() = System.`in`.bufferedReader().use { stdin ->
    val S = stdin.readLine().trimEnd()
    val dict = Array(stdin.readLine().toInt()) { stdin.readLine().trimEnd() }
    val isComposable = BooleanArray(S.length + 1) { it == 0 }
    for (k in 1..S.length) {
        for (word in dict) {
            if (k - word.length < 0) continue
            if (isComposable[k - word.length]) {
                isComposable[k] = S.startsWith(word, k - word.length)
                if (isComposable[k]) break
            }
        }
    }

    println(if (isComposable[S.length]) 1 else 0)
}
