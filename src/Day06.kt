/**
 * ==========================================
 * Class Declarations
 * ==========================================
 */

// represent school of fish by
//    key:   timer of fish
//    value: number of fish with this timer
typealias LanternSchool = Map<Int, Long>


/**
 * ==========================================
 * Main
 * ==========================================
 */
fun main() {
    // read in lantern fish ages
    val ages = readInput("Day06")
        .readText()
        .split(",")
        .map { it.toInt() }
    val fishes = ages.toLanternSchool()

    val partOne = (fishes simulate 80).sum()
    println("Part 1: $partOne")

    val partTwo = (fishes simulate 256).sum()
    println("Part 1: $partTwo")
}


/**
 * ==========================================
 * Methods for Classes
 * ==========================================
 */
private infix fun LanternSchool.simulate(days: Int): LanternSchool {
    return (1..days).fold(this) { school, _ ->
        school.simulateDay()
    }
}

fun LanternSchool.simulateDay(): LanternSchool {
    val numberOfNewFish = getPopulationFromTimer(0)
    val newSchool = mapKeys { (timer,_) -> timer - 1 }.toMutableMap()
    newSchool[8] = numberOfNewFish
    newSchool[6] = numberOfNewFish + getPopulationFromTimer(7)
    newSchool.remove(-1)
    return newSchool
}

fun LanternSchool.sum(): Long =
    values.sum()

fun LanternSchool.getPopulationFromTimer(timer: Int): Long =
    getOrDefault(timer, 0)



/**
 * ==========================================
 * Extensions
 * ==========================================
 */
fun List<Int>.toLanternSchool(): LanternSchool {
    return groupingBy { age -> age }
        .eachCount()
        .mapValues { (_, number) -> number.toLong() }
}