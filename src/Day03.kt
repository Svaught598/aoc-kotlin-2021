enum class Criteria {
    OXYGEN, CO2
}

fun main() {
    fun getFileInput(): List<String> {
        val file = readInput("Day03")
        return file.map { it }
    }

    fun getPowerConsumption(input: List<String>): Int {
        var gammaBinary = ""
        var epsilonBinary = ""

        for (i in 0 until input[0].length) {
            val ones = input.count { it[i] == '1'}
            val zeros = input.count { it[i] == '0' }
            gammaBinary += if (ones > zeros) '1' else '0'
            epsilonBinary += if (ones > zeros) '0' else '1'
        }

        val gammaVal = binaryToDecimal(gammaBinary)
        val epsilonVal = binaryToDecimal(epsilonBinary)
        return gammaVal * epsilonVal
    }

    /**
     * To find oxygen generator rating, determine the most common value (0 or 1)
     * in the current bit position, and keep only numbers with that bit in that position.
     * If 0 and 1 are equally common, keep values with a 1 in the position being considered.
     *
     * To find CO2 scrubber rating, determine the least common value (0 or 1)
     * in the current bit position, and keep only numbers with that bit in that position.
     * If 0 and 1 are equally common, keep values with a 0 in the position being considered.
     */
    fun getRating(input: List<String>, index: Int, criteria: Criteria): Int {
        val ones = input.count { it[index] == '1'}
        val zeros = input.count { it[index] == '0' }

        val bitNumbers = when (criteria) {
            Criteria.OXYGEN ->
                input.filter { it[index] == if (ones >= zeros) '1' else '0' }
            Criteria.CO2 ->
                input.filter { it[index] == if (ones >= zeros) '0' else '1' }
        }

        if (bitNumbers.size == 1)
            return binaryToDecimal(bitNumbers[0])
        return getRating(bitNumbers, index + 1, criteria)
    }

    fun getLifeSupportRating(input: List<String>): Int {
        val oxygenRating = getRating(input, 0, Criteria.OXYGEN)
        val co2Rating = getRating(input, 0, Criteria.CO2)
        return oxygenRating * co2Rating
    }

    val input = getFileInput()
    val powerConsumption = getPowerConsumption(input)
    println("Part 1: $powerConsumption")
    val lifeSupportRating = getLifeSupportRating(input)
    println("Part 2: $lifeSupportRating")
}


