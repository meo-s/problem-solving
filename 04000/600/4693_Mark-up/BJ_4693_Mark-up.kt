// https://www.acmicpc.net/problem/4693

package `BJ_4693_Mark-up`

fun parse(html: String): String {
    val sb = StringBuilder()

    var i = 0
    var formatEnabled = true
    while (i < html.length) {
        if (html[i] == '\\' && html[i + 1] == '*') {
            i += 2
            formatEnabled = !formatEnabled
            continue
        }

        if (!formatEnabled || html[i] != '\\') {
            sb.append(html[i++])
            continue
        }

        when (html[++i]) {
            'b' -> {}
            'i' -> {}
            '*' -> formatEnabled = !formatEnabled

            's' -> {
                var afterDot = false
                while (++i < html.length) when {
                    html[i] in '0'..'9' -> {}

                    html[i] == '.' && !afterDot -> {
                        afterDot = true
                    }

                    else -> {
                        --i
                        break
                    }
                }
            }

            else -> {
                sb.append(html[i])
            }
        }  // end of when

        ++i
    }

    return sb.toString()
}


fun main() {
    println(parse(System.`in`.bufferedReader().use { it.readText() }))
}
