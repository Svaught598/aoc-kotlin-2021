import java.io.File
import java.util.*


fun main() {
    // This part sucked
    val heightMap = readInput("Day09").toHeightMap()
    val minHeights = heightMap.getMinHeights()
    val partOne = minHeights.sumOf { it.third + 1 }
    println("Part 1: $partOne")

    // This part also sucked,
    val basinSizes = heightMap.findAllBasins(minHeights)
    val (b1, b2, b3) = basinSizes.sortedDescending()
    val partTwo = b1*b2*b3
    println("Part2: $partTwo")
}


data class HeightMap(
    val map: List<Int>,
    val w: Int,
    val l: Int)


fun HeightMap.findAllBasins(mins: List<Triple<Int,Int,Int>>): List<Int> {
    val coordinates = mins.map { Pair(it.first, it.second) }
    return coordinates.map { findBasin(it) }
}

fun HeightMap.findBasin(min: Pair<Int,Int>): Int {
    val queue = LinkedList<Pair<Int,Int>>(listOf(min))
    val visited = mutableSetOf<Pair<Int,Int>>()
    val basins = mutableListOf<Pair<Int,Int>>()

    while (queue.isNotEmpty()) {
        val (x,y) = queue.poll()

        if (Pair(x,y) in visited)
            continue
        else
            visited.add(Pair(x,y))
            if (getOrNull(x,y) != 9) {
                basins.add(Pair(x,y))
                queue.addAll(getNeighborsCoords(x,y))
            }

    }
    return basins.size
}

fun HeightMap.getMinHeights(): List<Triple<Int, Int, Int>> {
    val minHeights = (0 until w).map { i ->
        (0 until l).map { j ->
            getRiskLevel(i, j)
        }
    }.flatten().mapNotNull { it }
    return minHeights
}

fun HeightMap.getRiskLevel(i: Int, j: Int): Triple<Int, Int, Int>? {
    val cell = map[i+(j*w)]
    val risk = getNeighbors(i, j).all { it > cell }
    return if (!risk || cell == 9) null else Triple(i,j,cell)
}

fun HeightMap.getNeighbors(i: Int, j: Int): List<Int> {
    val left = getOrNull(i-1, j)
    val right = getOrNull(i+1, j)
    val top = getOrNull(i, j-1)
    val bottom = getOrNull(i, j+1)
    return listOf(left, right, top, bottom).mapNotNull { it }
}

fun HeightMap.getNeighborsCoords(i: Int, j: Int): List<Pair<Int,Int>> {
    val left = if (getOrNull(i-1, j) != null) Pair(i-1,j) else null
    val right = if (getOrNull(i+1, j) != null) Pair(i+1,j) else null
    val top = if (getOrNull(i, j-1) != null) Pair(i,j-1) else null
    val bottom = if (getOrNull(i, j+1) != null) Pair(i,j+1) else null
    return listOf(left, right, top, bottom).mapNotNull { it }
}

fun HeightMap.getOrNull(i: Int, j: Int): Int? {
    return if (i < 0 || i > w-1 || j < 0 || j > l-1)
        null
    else
        map[i+(j*w)]
}

fun File.toHeightMap(): HeightMap {
    val map = readText()
        .toCharArray()
        .filter { it.isDigit() }
        .map { it.toString().toInt() }
    val w = readLines()[0].length
    val l = readLines().size
    return HeightMap(map = map, w = w, l = l)
}