// https://www.acmicpc.net/problem/21173

package BJ_21173_ICPC_Record_Matching

import java.io.BufferedReader

class Name(firstName: String, lastName: String) {

    val fullName = "$lastName $firstName"
    private val fullNameLowercase = fullName.lowercase()

    override fun hashCode() = fullNameLowercase.hashCode()

    override fun equals(other: Any?): Boolean {
        return other is Name && fullNameLowercase == other.fullNameLowercase
    }

    override fun toString() = fullName
}

class Email(private val email: String) : Comparable<Email> {

    private val emailLowercase = email.lowercase()

    override operator fun compareTo(other: Email): Int {
        return emailLowercase.compareTo(other.emailLowercase)
    }

    override fun hashCode() = emailLowercase.hashCode()

    override fun equals(other: Any?): Boolean {
        return other is Email && other.emailLowercase == emailLowercase
    }

    override fun toString() = email
}

class Record(val name: Name, val email: Email, var matched: Boolean = false)

val WhitespaceRegex = Regex("\\s+")

fun forEachLine(br: BufferedReader, block: (String) -> Unit) {
    while (true) {
        val line = br.readLine()
        if (line == null || line.isBlank()) break
        block(line)
    }
}

fun main() = System.`in`.bufferedReader().use { stdin ->
    val insideRecords = ArrayList<Record>()
    val outsideRecords = ArrayList<Record>()
    val nameMap = HashMap<Name, Record>()
    val emailMap = HashMap<Email, Record>()

    forEachLine(stdin) { line ->
        val (firstName, lastName, email) = line.split(WhitespaceRegex)
        val record = Record(Name(firstName, lastName), Email(email))
        insideRecords.add(record)

        nameMap[record.name] = record
        emailMap[record.email] = record
    }

    forEachLine(stdin) { line ->
        val (firstName, lastName, email) = line.split(WhitespaceRegex)
        val record = Record(Name(firstName, lastName), Email(email))

        var matched = false
        if (record.name in nameMap) {
            nameMap[record.name]!!.matched = true
            matched = true
        }
        if (record.email in emailMap) {
            emailMap[record.email]!!.matched = true
            matched = true
        }
        if (!matched) {
            outsideRecords.add(record)
        }
    }

    print(
        StringBuilder().run {
            insideRecords
                .filter { !it.matched }
                .sortedBy { it.email }
                .forEach { append("I ${it.email} ${it.name}\n") }
            outsideRecords
                .sortedBy { it.email }
                .forEach { append("O ${it.email} ${it.name}\n") }

            ifEmpty { "No mismatches." }
        }
    )
}
