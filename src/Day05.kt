import java.io.File


/**
 * ==========================================
 * Class Declarations
 * ==========================================
 */
data class LineSegment(
    val start: Pair<Int, Int>,
    val end: Pair<Int, Int>)

data class LineCell(
    val coordinates: Pair<Int, Int>,
    val numOfLines: Int)

data class LineBoard(
    var board: MutableList<MutableList<LineCell>>)


/**
 * ==========================================
 * Main
 * ==========================================
 */
fun main() {
    // part 1 (6564)
    val lines = readInput("Day05").parseInput()
    val lineBoard = LineBoard(1000, 1000)
    lineBoard.addStraightLines(lines)
    var numOfOverlaps = lineBoard.countOverlaps(2)
    println("part 1: $numOfOverlaps")

    // part 2
    lineBoard.addDiagonalLines(lines)
    numOfOverlaps = lineBoard.countOverlaps(2)
    println("part 2: $numOfOverlaps")
}


/**
 * ==========================================
 * Methods for Classes
 * ==========================================
 */
fun LineBoard(length: Int, width: Int): LineBoard {
    val board = (0 until length).map { rowNumber ->
        (0 until width).map { colNumber ->
            val coordinates = Pair(colNumber, rowNumber)
            LineCell(
                coordinates = coordinates,
                numOfLines = 0)
        }.toMutableList()
    }
    return LineBoard(board = board.toMutableList())
}

fun LineBoard.addStraightLines(lines: List<LineSegment>) {
    for (line in lines) {
        val cells = line.getStraightCoveredCells()
        addVisited(cells)
    }
}

fun LineBoard.addDiagonalLines(lines: List<LineSegment>) {
    for (line in lines) {
        val cells = line.getDiagonalCoveredCells()
        addVisited(cells)
    }
}

fun LineBoard.addVisited(visitedCoordinates: List<Pair<Int, Int>>) {
    for (cell in visitedCoordinates) {
        val (y, x) = cell
        val oldCell = board[x][y]

        board[x][y] = LineCell(
            coordinates = Pair(x, y),
            numOfLines = oldCell.numOfLines + 1)
    }
}

fun LineBoard.countOverlaps(numOfOverlaps: Int): Int {
    return board.sumOf { row ->
        row.count { cell ->
            cell.numOfLines >= numOfOverlaps
        }
    }
}

fun LineSegment.getStraightCoveredCells(): List<Pair<Int, Int>> {
    val (x1, y1) = start
    val (x2, y2) = end
    var cells = emptyList<Pair<Int, Int>>()

    if (x1 == x2 && y1 > y2)
        cells = (y1 downTo  y2).map { y -> Pair(x1, y) }
    if (x1 == x2 && y1 < y2)
        cells = (y1..y2).map { y -> Pair(x1, y) }
    if (y1 == y2 && x1 > x2)
        cells = (x1 downTo x2).map { x -> Pair(x, y1) }
    if (y1 == y2 && x1 < x2)
        cells = (x1..x2).map { x -> Pair(x, y1) }

    return cells
}

fun LineSegment.getDiagonalCoveredCells(): List<Pair<Int, Int>> {
    val (x1, y1) = start
    val (x2, y2) = end
    var cells = emptyList<Pair<Int, Int>>()

    // early return for straight lines
    if (x1 == x2 || y1 == y2) return cells

    if (x1 < x2 && y1 < y2) // both increase
        cells = (x1..x2).zip(y1..y2)
    if (x1 < x2 && y1 > y2) // x increase, y decrease
        cells = (x1..x2).zip(y1 downTo y2)
    if (x1 > x2 && y1 < y2) // y increase, x decrease
        cells = (x1 downTo x2).zip(y1..y2)
    if (x1 > x2 && y1 > y2) // both decrease
        cells = (x1 downTo x2).zip(y1 downTo y2)

    return cells
}


/**
 * ==========================================
 * Extensions
 * ==========================================
 */
fun File.parseInput(): List<LineSegment>{
    return map { fileLine ->
        val pairs = fileLine.split(" -> ")
        val coordinatePairs = pairs.map { pair ->
            val coordinates = pair.split(",").map { it.toInt() }
            Pair(coordinates.first(), coordinates.last())
        }
        LineSegment(
            start = coordinatePairs.first(),
            end = coordinatePairs.last())
    }
}
