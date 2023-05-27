// https://www.acmicpc.net/problem/1152

import java.io.BufferedReader
import java.io.InputStreamReader

fun main() = with(BufferedReader(InputStreamReader(System.`in`))) {
    println(readLine().splitToSequence(' ').filter { s -> s.isNotEmpty() }.count())
}
