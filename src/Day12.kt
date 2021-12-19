import java.io.File



fun main() {
    val input = readInput("Day12").toCaveSys()
    val partOne = countPaths(input, "start", setOf("start"))
    println("Part 1: $partOne")

    val partTwo = countPaths(input, "start", setOf("start"), bonus = true)
    println("Part 2: $partTwo")
}


fun countPaths(
    caveSys: Map<String,List<String>>,
    starting: String,
    route: Set<String>,
    bonus: Boolean = false,
): Int {
    var count = 0
    val nextPaths = caveSys[starting]

    for (next in nextPaths!!) {
        if (next == "end"){
            count += 1
            println(route)
            continue
        }

        if (next == "start")
            continue

        if (next.isLower()) {
            if (next !in route)
                count += countPaths(caveSys, next, route + next, bonus = bonus)
            else if (bonus)
                count += countPaths(caveSys, next, route + next, bonus = false)
        }

        else
            count += countPaths(caveSys, next, route + next, bonus = bonus)
    }
    return count
}

fun File.toCaveSys(): Map<String,List<String>> {
    val caveSys = mutableMapOf<String, List<String>>()

    forEachLine { line ->
        val (start, end) = line.split("-")

        // mapping front to back
        if (caveSys.containsKey(start)) {
            val endValues = caveSys[start]
            if (endValues != null) { caveSys[start] = endValues + end }
        }
        else caveSys[start] = listOf(end)

        // mapping reverse relationship
        if (caveSys.containsKey(end)) {
            val startValues = caveSys[end]
            if (startValues != null) { caveSys[end] = startValues + start}
        }
        else caveSys[end] = listOf(start)
    }
    return caveSys
}


