import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.pow

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "input/$name.txt")

fun <T>File.map(f: (String) -> T): List<T> {
    val mappedLines = ArrayList<T>()
    forEachLine { mappedLines.add(f(it)) }
    return mappedLines
}


/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)


/**
 * Converts binary string to decimal number (integer)
 */
fun binaryToDecimal(binary: String) : Int {
    var decimalNumber = 0
    for (i in binary.indices) {
        val power = binary.length - i - 1
        if (binary[i] == '1')
            decimalNumber += 2.toDouble().pow(power).toInt()
    }
    return decimalNumber
}


/**
 * Transpose for nested lists (grids)
 *
 * lifted from:
 *  https://gist.github.com/clementgarbay/49288c006252955c2a3c6139a61ca92a
 */
fun <E> transpose(xs: List<List<E>>): List<List<E>> {
    // Helpers
    fun <E> List<E>.head(): E = this.first()
    fun <E> List<E>.tail(): List<E> = this.takeLast(this.size - 1)
    fun <E> E.append(xs: List<E>): List<E> = listOf(this).plus(xs)

    xs.filter { it.isNotEmpty() }.let { ys ->
        return when (ys.isNotEmpty()) {
            true -> ys.map { it.head() }.append(transpose(ys.map { it.tail() }))
            else -> emptyList()
        }
    }
}


/**
 * Extension for a stack (using mutable list)
 */
inline fun <T> MutableList<T>.push(item: T) =
    add(item)
inline fun <T> MutableList<T>.pop(): T? =
    if (isNotEmpty()) removeAt(lastIndex) else null