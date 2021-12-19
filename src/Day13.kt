import java.io.File



fun main() {
    // part 1, fold first line only
    val (coordinates, foldAlong) = readInput("Day13").toPaper()
    var workingCoordinates = fold(coordinates.toSet(), foldAlong.first())
    println("Part 1: ${ workingCoordinates.size }")

    // part 2, fold all lines, print grid to console
    workingCoordinates = coordinates.toSet()
    for (f in foldAlong)
        workingCoordinates = fold(workingCoordinates, f)
    println("Part 2:")
    printGrid(workingCoordinates)
}


fun fold(grid: Set<Pair<Int,Int>>, foldAlong: Pair<Int,Int>): Set<Pair<Int,Int>> {
    val (foldX, foldY) = foldAlong
    return if (foldX == 0)
        foldAlongY(grid, foldY)
    else
        foldAlongX(grid, foldX)
}


fun foldAlongX(grid: Set<Pair<Int,Int>>, x: Int): Set<Pair<Int,Int>> {
    return grid.map { (i,j) ->
        if (i < x ) (i to j)
        else ((2 * x - i) to j)
    }.toSet()
}

fun foldAlongY(grid: Set<Pair<Int,Int>>, y: Int): Set<Pair<Int,Int>> {
    return grid.map { (i,j) ->
        if (j < y ) (i to j)
        else (i to (2 * y - j))
    }.toSet()
}

fun printGrid(grid: Set<Pair<Int,Int>>) {
    val minW = grid.minOf { (x,_) -> x }
    val maxW = grid.maxOf { (x,_) -> x }

    val minH = grid.minOf { (_,y) -> y }
    val maxH = grid.maxOf { (_,y) -> y }

    for (j in minH..maxH) {
        for (i in minW..maxW) {
            if ((i to j) in grid) print("XX")
            else print("  ")
        }
        print("\n")
    }
}



fun File.toPaper(): Pair<List<Pair<Int,Int>>,List<Pair<Int,Int>>> {
    val coordinates = mutableListOf<Pair<Int,Int>>()
    val foldAlong = mutableListOf<Pair<Int,Int>>()

    forEachLine { line ->
        if (line.contains(",")) {
            val (x,y) = line.split(",").map { it.toInt()}
            coordinates.add(x to y)
        }

        if (line.contains("fold")) {
            val equalsIdx = line.indexOf("=")
            val equation = line.substring(equalsIdx - 1)
            val (dir, value) = equation.split("=")
            if (dir == "x") foldAlong.add(value.toInt() to 0)
            if (dir == "y") foldAlong.add(0 to value.toInt())
        }
    }
    return coordinates to foldAlong
}