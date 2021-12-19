import java.io.File



data class OctopusGrid(
    val grid: List<Int>,
    val numFlashes: Int,
    val w: Int,
    val l: Int,
    val allFlashed: Boolean = false)



fun main() {
    // Part 1 (100 iterations)
    val input = readInput("Day11").toOctopusGrid()
    var octGrid = input
    for (i in 1..100) { octGrid = octGrid.iterate() }
    println("Part 1: ${ octGrid.numFlashes }")

    // Part 2 (How many iterations until all octopus flash simultaneously?)
    octGrid = input
    var counter = 0
    while (!octGrid.allFlashed) {
        octGrid = octGrid.iterate()
        counter += 1
    }
    println("Part 2: $counter")
}


fun OctopusGrid.iterate(): OctopusGrid {
    var newGrid = grid.map { it + 1 }.toMutableList()
    val flashing = getFlashing(newGrid).toMutableList()
    val justFlashed = flashing.toMutableSet()
    var flashes = 0

    while (flashing.isNotEmpty()) {
        val (i,j) = flashing.removeFirst()
        justFlashed.add(i to j)
        flashes += 1

        for ((x,y) in getNeighborsCoords(i,j)) {
            if ((x to y) in justFlashed) continue

            val index = x + (y * l)
            newGrid[index] += 1

            if (newGrid[index] > 9) {
                flashing.add(x to y)
                justFlashed.add(x to y)
            }
        }
    }
    newGrid = newGrid.map { if (it > 9) 0 else it }.toMutableList()
    val allFlashing = flashes == l*w

    return OctopusGrid(
        grid = newGrid,
        numFlashes = numFlashes + flashes,
        w = w,
        l = l,
        allFlashed = allFlashing)
}

fun OctopusGrid.getFlashing(newGrid: List<Int>): List<Pair<Int,Int>> {
    val xs = (0 until l).toList()
    val ys = (0 until w).toList()
    val indices = xs.product(ys)
    return indices.filter { (i,j) -> newGrid[i+(j*w)] > 9 }
}

fun OctopusGrid.getNeighborsCoords(i: Int, j: Int): List<Pair<Int,Int>> {
    val xs = (i-1..i+1).toList()
    val ys = (j-1..j+1).toList()
    val indices = xs.product(ys).filter { (x,y) -> (x to y) != (i to j) }
    return indices.mapNotNull { (x,y) -> getOrNull(x, y) }
}

fun OctopusGrid.getOrNull(i: Int, j: Int): Pair<Int, Int>? {
    return if (i < 0 || i >= l || j < 0 || j >= w) null
    else Pair(i, j)
}

fun File.toOctopusGrid(): OctopusGrid {
    val fileLines = readLines()
    val w = fileLines.size
    val l = fileLines[0].length
    val grid = fileLines
        .flatMap { line -> line.split("") }
        .filter { it in "0123456789" && it.isNotEmpty()}
        .map { it.toInt() }

    return OctopusGrid(
        grid = grid,
        numFlashes = 0,
        w = w,
        l = l,)
}