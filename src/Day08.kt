import java.io.File



fun main() {
    // Part 1
    val (inputs, outputs) = readInput("Day08").parseFile()
    val partOne = outputs.sumOf { line ->
        line.count { str ->
            str.length == 2 || str.length == 3 || str.length == 4 || str.length == 7
        }
    }
    println("Part 1: $partOne")


    // Part 2
    val result = inputs.zip(outputs).sumOf { (input, output) ->
        val mapping = decodeSignals(input)
        output.map { mapping[it.sort()] }.joinToString(separator = "").toInt()
    }
    println("Part 2: $result")


}

fun decodeSignals(inputSignals: List<String>): Map<String, String> {
    // Please God, forgive me for this abomination
    val sortedSignals = inputSignals.map { it.sort() }.sortedBy { it.length }

    val one = sortedSignals[0]
    val seven = sortedSignals[1]
    val four = sortedSignals[2]
    val eight = sortedSignals[9]

    val nine = sortedSignals.first { sig -> four.all { char -> char in sig } && sig.length == 6 }
    val zero = sortedSignals.first { sig -> one.all { char -> char in sig } && sig.length == 6 && sig != nine }
    val six = sortedSignals.first { sig -> sig.length == 6 && sig != nine && sig != zero }

    val topRightSignal = eight.split("").find { char -> !six.contains(char) }

    val three = sortedSignals.first { sig -> sig.length == 5 && one.all { char -> char in sig } }
    val two = sortedSignals.first() { sig -> sig.length == 5 && sig.contains(topRightSignal!!) && sig != three }
    val five = sortedSignals.first() { sig -> sig.length == 5 && sig != three && sig != two }

    return mapOf(
        one to "1",
        two to "2",
        three to "3",
        four to "4",
        five to "5",
        six to "6",
        seven to "7",
        eight to "8",
        nine to "9",
        zero to "0",
    )
}

fun File.parseFile(): Pair<List<List<String>>, List<List<String>>> {
    val inputs = readLines().map { line ->
        line.split(" | ").first().split(" ")
    }
    val outputs = readLines().map { line ->
        line.split(" | ").last().split(" ")
    }
    return Pair(inputs, outputs)
}

fun String.sort(): String =
    this.toCharArray().sorted().joinToString("")