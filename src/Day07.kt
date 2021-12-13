import kotlin.math.abs


typealias CrabPositions = List<Int>

fun main() {
    fun constantSum(number: Int): Int = number
    fun linearSum(number: Int): Int = (1..number).sum()

    // part 1
    val positions = readInput("Day07").readText().toCrabPositions()
    val partOne = positions.findMinFuel(::constantSum)
    println("Part 1: $partOne")

    // part 2
    val partTwo = positions.findMinFuel(::linearSum)
    println("Part 2: $partTwo")
}

/**
 * ==========================================
 * Extensions
 * ==========================================
 */

fun String.toCrabPositions(): CrabPositions =
    split(',').map { it.toInt() }


/**
 * finds minimum fuel for crabs to align
 *
 * @param acc accumulator function to calculate fuel spent from distance travelled
 * @return minimum fuel needed for crabs to align
 */
fun CrabPositions.findMinFuel(acc: (Int) -> Int): Int {
    val minPos = minOf { it }
    val maxPos = maxOf { it }
    val fuelSpent = (minPos..maxPos).map { pos ->
        sumOf { crabPos ->  acc(abs(pos - crabPos))}
    }
    return fuelSpent.minOf { it }
}
