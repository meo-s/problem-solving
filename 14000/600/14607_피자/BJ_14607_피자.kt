// https://www.acmicpc.net/problem/14607

fun divide(n: Long): Long = when (n) {
    1L -> 0
    2L -> 1
    else -> {
        if (n % 2 == 0L) {
            val happiness = divide(n / 2)
            (n / 2) * (n / 2) + happiness + happiness
        } else {
            ((n / 2) * (n / 2 + 1)) + divide(n / 2) + divide(n / 2 + 1)
        }
    }
}

fun main() {
    println(divide(readln().toLong()))
}
