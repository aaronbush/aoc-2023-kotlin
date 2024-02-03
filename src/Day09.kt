import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.io.PrintStream


fun main() {

    fun diffs(readings: List<Long>): List<Long> {
        return readings.windowed(2, 1).map { (a, b) ->
            a - b
        }
    }

    fun resolveLevels(readings: List<Long>): List<List<Long>> {
        fun loop(readings: List<Long>, accum: MutableList<List<Long>>) {
            if (readings.isEmpty() || readings.all { it == 0L }) return
            val nextLevel = diffs(readings)
            accum += nextLevel
            loop(nextLevel, accum)
        }
        val accum = mutableListOf(readings)
        loop(readings, accum)
//        accum.println()
        return accum
    }

    fun part1(input: List<String>): Long {
        val results = input.map { line ->
            val readings = line.split(" ").map { it.toLong() }.reversed()
            val resolvedLevels = resolveLevels(readings)
            val constants = resolvedLevels.reversed().map { it.first() }
//            constants.println()
            val newNums = mutableListOf(0L)
            repeat(constants.size -1) {
                newNums += newNums[it] + constants[it+1]
            }
//            newNums.println()
            newNums.last()
        }
        results.println()
      return results.sum()
    }



    fun part2(input: List<String>): Long {
        val results = input.map { line ->
            val readings = line.split(" ").map { it.toLong() }.reversed()
            val resolvedLevels = resolveLevels(readings)
//            resolvedLevels.println()
            val constants = resolvedLevels.reversed().map { it.last() }
//            constants.println()
            val newNums = mutableListOf(0L)
            // p - x = n; solve for x => p - n
            // ex: 2 - x = 0
            repeat(constants.size -1) {
                newNums +=  constants[it+1] - newNums[it]
            }
//            newNums.println()
            newNums.last()
        }
//        results.println()
        return results.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
//    check(part1(testInput) == 114L)
//    part2(testInput)

    val input = readInput("Day09")
//    part1(input).println()
    part2(input).println()
}

