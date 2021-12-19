import java.io.File



fun main() {
    // Part 1: 10 iterations
    val (template, rules) = readInput("Day14").toPolymerInstructions()
    val polymerPairs = template.windowed(2, 1)
    var polymerPairFrequency = polymerPairs.associateWith { pair -> polymerPairs.count { it == pair }.toLong() }

    for (i in 1..10)
        polymerPairFrequency = polymerStep(polymerPairFrequency, rules)

    var score = calcPolymerScore(polymerPairFrequency, template)
    println("Part 1: $score")


    // Part 2: 40 iterations
    polymerPairFrequency = polymerPairs.associateWith { pair -> polymerPairs.count { it == pair }.toLong() }

    for (i in 1..40)
        polymerPairFrequency = polymerStep(polymerPairFrequency, rules)

    score = calcPolymerScore(polymerPairFrequency, template)
    println("Part 2: $score")
}


fun calcPolymerScore(pairFreq: Map<String,Long>, template: String): Long {
    // count first char of each pair
    val scores = pairFreq.toList().fold(mutableMapOf<String,Long>()) { acc, (pair, number) ->
        val key = pair.first().toString()
        acc[key] = acc.getOrDefault(key, 0) + number
        acc
    }

    // correct for last char in template string (same as end result)
    val lastChar = template.last().toString()
    scores[lastChar] = scores.getOrDefault(lastChar, 0) + 1
    return scores.values.maxOf { it }.toLong() - scores.values.minOf { it }.toLong()
}


fun polymerStep(pairFreq: Map<String,Long>, rules: Map<String,String>): Map<String,Long> {
    val nextPairFreq = mutableMapOf<String,Long>()
    pairFreq.forEach { (pair, number) ->
        if (pair in rules) {
            val insertion = rules[pair]!!
            val first = pair[0] + insertion
            val second = insertion + pair[1]

            nextPairFreq[first] = nextPairFreq.getOrDefault(first, 0) + number
            nextPairFreq[second] = nextPairFreq.getOrDefault(second, 0) + number
        }
        else
            nextPairFreq[pair] = nextPairFreq.getOrDefault(pair, 0) + number
    }
    return nextPairFreq
}


fun File.toPolymerInstructions(): Pair<String,Map<String, String>> {
    val template = readLines()[0]
    val rules = mutableMapOf<String,String>()
    forEachLine { str ->
        if (str.contains(" -> ")) {
            val (first,second) = str.split(" -> ")
            rules[first] = second
        }
    }
    return template to rules
}