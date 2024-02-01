import kotlin.math.pow


fun main() {

    data class Card(val cardNumber: Int, val winningNumbers: List<Int>, val cardNumbers: List<Int>)

    fun String.toCard(): Card {
        val cardNum = this.substringBefore(":").substringAfter("Card ").trim().toInt()
        val winningString = this.substringAfter(":").substringBefore("|")
        val playedString = this.substringAfter("|")

        val winningInts = winningString.split(" ").map { it.toIntOrNull() }.filterNotNull()
        val playedInts = playedString.split(" ").map { it.toIntOrNull() }.filterNotNull()

        return Card(cardNum, winningInts, playedInts)
    }

    fun List<Card>.getCardsWithNumbers(range: IntRange): List<Card> {
        return filter { it.cardNumber in range }
    }

    fun Card.numWins(): Int {
        return cardNumbers.count { winningNumbers.contains(it) }
    }

    fun numWins(cards: List<Card>): List<Int> {
        val numWins = cards.map { card ->
            card.numWins()
        }.filter { it > 0 }
        return numWins
    }

    fun sumWins(cards: List<Card>): Int {
        val numWins = numWins(cards)
        return numWins.map { it - 1 }.map { it.toDouble() }.sumOf { 2.0.pow(it) }.toInt()
    }

    fun part1(input: List<String>): Int {
        val cards = input.map { it.toCard() }
        return sumWins(cards)
    }


    fun part2(input: List<String>): Int {
        val cards = input.map { it.toCard() }
        TODO()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
//    part1(testInput).println()
//    part2(testInput).println()
//    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
//    part1(input).println()
    part2(input).println()
}
