import kotlin.math.*

fun main() {

    fun quadratic(a: Long, b: Long, c: Long): Pair<Double, Double> {
        val x1 = (-b - sqrt(b.toDouble().pow(2) - 4 * a * c)) / 2 * a
        val x2 = (-b + sqrt(b.toDouble().pow(2) - 4 * a * c)) / 2 * a
        return x1 to x2
    }

    fun part1(races: List<Pair<Long, Long>>): Long {
        // constants: high_score, time_allowed
        // time_button_held = 0..time_allowed
        // dist = remaining_time * velocity
        // velocity = time_button_held
        // remaining_time = time_allowed - time_button_held
        // win = dist > high_score
        // [t(allow) - t(held)] * t(held) > high_score
        // Ex:
        // [7 - t(h)] * t(h) > 9
        // [7 - x] * x - 9 > 0
        // 7x - x^2 - 9 > 0
        // x^2 -7x + 9 < 0 // reverse inequality due to *(-1)
        // 7 +- sqrt(-7^2 - 4*1*9) / 2*1
        // 7 +- sqrt(13) / 2
        // [7 +- 3.6] / 2
        // 10.6 / 2 => 5.3
        // 3.4 / 2 => 1.7
        // <-----------1.7-----------------5.3------->
        // try 6 => 6^2 - 7*6 + 9 < 0?   36-42+9 = 3 Nope
        // try 2 => 2^2 - 7*2 + 9 < 0?   4-14+9 = -1 Yep
        // try 0 => 9 < 0 Nope // try 5 => 25 -7*5 + 9
        // range is (1.7, 5.3) => [2 - 5] (whole numbers)
        val result = races.map { (timeAllowed, highScore) ->
            val a = 1L
            val b = timeAllowed * -1
            val c = highScore
            val (n1, n2) = quadratic(a, b, c)
            "$n1 and $n2".println()
            ceil(min(n1, n2) + .1).toLong()..floor(max(n1, n2) - .1).toLong()
        }
            .also {
                it.println()
                it.forEach { r -> r.count().println() }
            }
        return result.map { it.last - it.first + 1 }.reduce { a, b -> a * b }
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    //Time:      7  15   30
    //Distance:  9  40  200
    val races = listOf<Pair<Long, Long>>(7L to 9, 15L to 40, 30L to 200)
    check(part1(races) == 288L)
    part1(listOf(71530L to 940200)).println()

    val input = readInput("Day02")
    //Time:        48     93     85     95
    //Distance:   296   1928   1236   1391
    val races2 = listOf<Pair<Long, Long>>(48L to 296L, 93L to 1928, 85L to 1236, 95L to 1391)
    part1(races2).println()
    part1(listOf(48938595L to 296192812361391)).println()

}
