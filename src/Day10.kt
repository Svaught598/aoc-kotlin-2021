import java.util.*

val CORRUPTED_SCORE_MAPPING = mapOf(
    ")" to 3,
    "]" to 57,
    "}" to 1197,
    ">" to 25137,)

val COMPLETED_SCORE_MAPPING = mapOf(
    ")" to 1,
    "]" to 2,
    "}" to 3,
    ">" to 4,)

val CLOSED_MAPPINGS = mapOf(
    "(" to ")",
    "[" to "]",
    "{" to "}",
    "<" to ">",)

val OPEN_CHARS = setOf("(", "[", "{", "<")
val CLOSE_CHARS = setOf(")", "]", "}", ">")
const val FIVE: Long = 5
const val ZERO: Long = 0


fun main() {
    var inputs = readInput("Day10").readLines().map { it.split("") }
    val partOne = inputs.mapNotNull { getCorruptedValue(it) }.sum()
    println("Part 1: $partOne")

    inputs = readInput("Day10").readLines().map { it.split("") }
    val completedScores = inputs.mapNotNull { getCompletedValue(it) }.sorted().filter { it != 0.toLong() }
    val index = completedScores.size/2
    val partTwo = completedScores[index]
    println("Part2: $partTwo")
}


fun getCorruptedValue(str: List<String>): Int? {
    val stack = mutableListOf<String>()

    for (char in str) {
        if (char in OPEN_CHARS) stack.push(char)
        if (char in CLOSE_CHARS) {
            val lastOpenChar = stack.pop()
            if (CLOSED_MAPPINGS[lastOpenChar] != char)
                return CORRUPTED_SCORE_MAPPING[char]
        }
    }
    return 0
}

fun getCompletedValue(str: List<String>): Long? {
    val stack = mutableListOf<String>()

    for (char in str) {
        if (char in OPEN_CHARS) stack.push(char)
        if (char in CLOSE_CHARS) {
            val lastOpenChar = stack.pop()
            if (CLOSED_MAPPINGS[lastOpenChar] != char)
                return ZERO
        }
    }
    return calculateScore(stack)
}

fun calculateScore(stack: List<String>): Long {
    val matchingBraces = stack
        .mapNotNull { CLOSED_MAPPINGS.getOrDefault(it, null) }
        .reversed()

    val scores = matchingBraces
        .mapNotNull { COMPLETED_SCORE_MAPPING.getOrDefault(it,null) }
        .map { it.toLong() }
        .toMutableList()

    // add 0 to start (for accumulator)
    scores.add(0, 0)
    if (scores.isNotEmpty())
        return scores.reduce { acc: Long, score: Long -> ((acc * FIVE) + score) as Long }
    return ZERO
}