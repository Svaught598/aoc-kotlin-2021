fun main() {
    // Getting file
    val file = readInput("Day01")
    val input = file.map { it.toInt() }

    // Part 1
    val part1 = input.countInc()
    println("Part 1: $part1")

    // Part 2
    val tripletSums: List<Int> = input.windowed(3){ tripleList -> tripleList.sum() }
    val part2 = tripletSums.countInc()
    println("Part 2: $part2")
}


fun List<Int>.countInc(): Int =
    zipWithNext().count { pair: Pair<Int, Int> ->
        pair.second > pair.first
    }
