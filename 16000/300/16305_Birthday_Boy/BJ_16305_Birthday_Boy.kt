// https://www.acmicpc.net/problem/16305

package BJ_16305_Birthday_Boy

val MonthDays = arrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
val Oct27 = "10-27".toDays()

fun String.toDays(): Int {
    val (month, day) = this.split('-').map { it.toInt() }
    return MonthDays.take(month - 1).sum() + day
}

fun Int.yesterday() = if (0 < this - 1) this - 1 else 365

fun daydist(from: Int, to: Int) = if (to == from) 365 else if (from < to) to - from else (to - from + 365) % 365

fun main() = System.`in`.bufferedReader().use { stdin ->
    val birthDays = IntArray(stdin.readLine().toInt()) { stdin.readLine().split(' ')[1].toDays() }
    birthDays.sort()

    var maxDiff = Pair(birthDays[0].yesterday(), daydist(birthDays.last(), birthDays.first()))
    for (i in 1 until birthDays.size) {
        val diff = birthDays[i] - birthDays[i - 1]
        if (maxDiff.second == diff) {
            if (daydist(Oct27, birthDays[i].yesterday()) < daydist(Oct27, maxDiff.first)) {
                maxDiff = Pair(birthDays[i].yesterday(), diff)
            }
        } else if (maxDiff.second < diff) {
            maxDiff = Pair(birthDays[i].yesterday(), diff)
        }
    }

    var month = 1
    var day = maxDiff.first
    for (monthDay in MonthDays) {
        if (day <= monthDay) {
            break
        }

        ++month
        day -= monthDay
    }

    println("${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}")
}
