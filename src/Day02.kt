fun main() {
    fun getFileInput(): List<Pair<String, Int>> {
        val file = readInput("Day02")
        val input = file.map {
            val label = it.substringBefore(" ")
            val num = it.substringAfter(" ")
            Pair<String, Int>(label, num.toInt())
        }
        return input
    }

    fun part1(input: List<Pair<String, Int>>): Int {
        var horizontal = 0
        var depth = 0
        for ((direction, value) in input) {
            when (direction) {
                "forward" -> horizontal += value
                "down" -> depth += value
                "up" -> depth -= value
            }
        }
        return depth * horizontal
    }

    fun part2(input: List<Pair<String, Int>>): Int {
        var horizontal = 0
        var depth = 0
        var aim = 0

        for ((direction, value) in input) {
            when (direction) {
                "forward" -> {
                    horizontal += value
                    depth += aim * value
                }
                "down" -> aim += value
                "up" -> aim -= value
            }
        }
        return depth * horizontal
    }


    val input = getFileInput()
    var result = part1(input)
    println("Part 1: $result")
    result = part2(input)
    println("Part 2: $result")
}


