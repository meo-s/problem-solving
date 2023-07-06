// https://www.acmicpc.net/problem/11339

package BJ_11339_There_is_no_place_for_loopback

import java.io.BufferedReader
import java.io.BufferedWriter

val re = Regex("([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})")

@ExperimentalUnsignedTypes
fun parseIpv4(s: String): UInt? {
    var ipv4 = 0U
    for ((i, token) in (re.matchEntire(s) ?: return null).groupValues.drop(1).withIndex()) {
        val byte = token.toUInt()
        if (255U < byte) {
            return null
        }
        ipv4 += byte shl (8 * (3 - i))
    }

    return ipv4
}

@ExperimentalUnsignedTypes
class Ipv4Range(val name: String, val beg: UInt, val end: UInt) {
    operator fun contains(ipv4: UInt): Boolean = ipv4 in beg..end
}

@ExperimentalUnsignedTypes
class Ipv4Table(addresses: List<Ipv4Range>) {

    private val table = addresses.sortedBy { it.beg }

    fun resolve(ipv4: UInt): String? {
        var lb = 0;
        var ub = table.size;

        while (lb < ub) {
            val mid = (lb + ub) / 2
            if (ipv4 in table[mid]) {
                return table[mid].name
            }

            if (ipv4 < table[mid].beg) {
                ub = mid
            } else {
                lb = mid + 1
            }
        }

        return null
    }

    companion object {
        fun from(br: BufferedReader): Ipv4Table = Ipv4Table(ArrayList<Ipv4Range>().apply {
            repeat(br.readLine().toInt()) {
                val tokens = br.readLine().split(' ').toList()
                if (tokens.size == 2) {
                    val ipv4 = parseIpv4(tokens[1])!!
                    add(Ipv4Range(tokens[0], ipv4, ipv4))
                } else {
                    val ipv4Beg = parseIpv4(tokens[1])!!
                    val ipv4End = parseIpv4(tokens[2])!!
                    add(Ipv4Range(tokens[0], ipv4Beg, ipv4End))
                }
            }
        })
    }

}

@ExperimentalUnsignedTypes
fun runTestcase(br: BufferedReader, bw: BufferedWriter) {
    val ipTable = Ipv4Table.from(br)

    repeat(br.readLine().toInt()) {
        for (token in br.readLine().split(' ')) {
            val ipv4 = parseIpv4(token)
            val name = if (ipv4 != null) ipTable.resolve(ipv4) else null
            bw.write(name ?: token)
            bw.write(" ")
        }

        bw.write("\n")
    }
}

@ExperimentalUnsignedTypes
fun main() {
    System.`in`.bufferedReader().use { stdin ->
        System.out.bufferedWriter().use { stdout ->
            repeat(stdin.readLine().toInt()) { runTestcase(stdin, stdout) }
            stdout.flush()
        }
    }
}
