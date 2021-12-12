import java.io.File

/**
 * ==========================================
 * Class Declarations
 * ==========================================
 */
data class Game(
    var boards: List<Board>,
    var winners: List<Pair<Board, Int>>) // Pair contains board & last called number

data class Board(
    var board: List<List<BoardNumber>>)

data class BoardNumber(
    var num: Int,
    var found: Boolean)


/**
 * ==========================================
 * Main
 * ==========================================
 */
fun main() {
    // parse input into bingo calls & boards
    val (calls, boards) = readInput("Day04").parseToBingo()
    var game = Game(boards = boards, winners = emptyList())

    for (call in calls) {
        game = game.makeCall(call)
    }
    val winnerScores = game.winners.map { (board, lastNum) ->
        board.calcScore(lastNum)
    }

    // 25023
    val partOne = winnerScores.first()
    println("Part 1: $partOne")

    // 2634
    val partTwo = winnerScores.last()
    println("Part 1: $partTwo")
}


/**
 * ==========================================
 * Game Methods
 * ==========================================
 */
fun Game.makeCall(call: Int): Game {
    val newBoards = boards.map { board ->
        val newBoard = board.mark(call)
        newBoard
    }
    val (newWinners, stillPlaying) = newBoards.partition { board -> board.checkIfWon() }
    val winners = winners + newWinners.map { Pair(it, call)}
    return Game(stillPlaying, winners)
}


/**
 * ==========================================
 * Board Methods
 * ==========================================
 */
fun Board(numbers: String): Board {
    // functional madness to get board numbers into a list of lists
    val lines = numbers.split("\n")
    val grid = lines.map {row ->
        row.split(' ')
            .filter { it != ""}
            .map { BoardNumber(num=it.toInt(), found=false) }
    }
    return Board(grid)
}

fun Board.mark(number: Int): Board {
    val newBoard = board.map { row -> row.map { cell ->
        if (cell.num == number) BoardNumber(num = cell.num, found = true)
        else cell
    }}
    return Board(board=newBoard)
}

fun Board.checkIfWon(): Boolean {
    val solvedRow = board.any { row -> row.all { it.found } }
    val solvedCol = transpose(board).any { row -> row.all { it.found } }
    return solvedRow || solvedCol
}

fun Board.sumOfUnmarked(): Int {
    return board.sumOf { row ->
        row.sumOf { cell ->
            if (!cell.found) cell.num else 0
        }
    }
}

fun Board.calcScore(lastCall: Int): Int {
    return sumOfUnmarked() * lastCall
}


/**
 * ==========================================
 * Extensions
 * ==========================================
 */
fun File.parseToBingo(): Pair<List<Int>, List<Board>> {
    val sections = readText().split("\n\n").toMutableList()
    val calls = sections.removeFirst().split(",").map { it.toInt() }
    val boards = sections.map { boardString -> Board(boardString) }
    val numOfBoards = boards.size
    println("boards loaded: $numOfBoards")
    return Pair(calls, boards)
}
