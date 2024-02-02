import kotlin.coroutines.EmptyCoroutineContext.fold
import kotlin.math.max

fun main() {


    data class Hand(val cards: String, val bid: Int)

    fun Hand.strength(): Int {
        // 32T3K ==> 32tk
        // T55J5 ==> t5j
        // t5t5j ==> t5j
        // 555tj ==> 5tj
        // 555tt ==> 5t
        // 5555t ==> 5t
        // 55555 ==> 5
        val handSet = cards.toSet()
        val strength = when (handSet.size) {
            5 -> {
                // 1: high card
                1
            }

            4 -> {
                // 2: one pair
                2
            }

            3 -> {
                if (handSet.map { c ->
                        cards.count { it == c }
                    }.any { it == 3 }) {
                    //4: three of a kind
                    4
                } else {
                    //3: two pair
                    3
                }
            }

            2 -> {
                if (handSet.map { c ->
                        cards.count { it == c }
                    }.any { it == 4 }) {
                    // 4 of a kind
                    6
                } else {
                    // full house
                    5
                }
            }

            1 -> {
                //7 5 of a kind
                7
            }

            else -> {
                TODO()
            }
        }
        return strength
    }

    fun Hand.maximize(): Int {
        if (! cards.contains("J")) {
            return strength()
        }
        val baseStrength = strength()
        val mostSeenCards = cards.filterNot { it == 'J' }.fold(mutableMapOf<Char,Int>()) { acc: MutableMap<Char, Int>, c: Char ->
            if (acc.containsKey(c)) {
                acc[c] = acc[c]!!.plus(1)
            } else
                acc[c] = 1
            acc
        }
        val mostSeenCard = if (mostSeenCards.isEmpty()) {
            'A'
        } else {
            mostSeenCards.maxBy { entry -> entry.value }.key
        }
        val newCards = cards.replace('J', mostSeenCard)
        val newHand = Hand(newCards, bid)
        check(newHand.strength() >= baseStrength) { "new hand $newHand not stronger than $this" }
        return newHand.strength()
    }

    fun Char.toCardValue(): Int {
        return when (this) {
            'A' -> 14
            'K' -> 13
            'Q' -> 12
            'J' -> 0 // 11
            'T' -> 10
            else -> this.toString().toInt()
        }
    }

    fun Hand.compareTo2(other: Hand): Int {
        val thisStrength = maximize()
        val otherStrength = other.maximize()
        if (thisStrength > otherStrength)
            return 1
        else if (thisStrength == otherStrength) {
            // order-wise compare
            repeat(5) {
                val a = cards[it]
                val b = other.cards[it]
//                "compare $a to $b: ${a.toCardValue() > b.toCardValue()}".println()

                when {
                    a.toCardValue() > b.toCardValue() -> return 1
                    a.toCardValue() < b.toCardValue() -> return -1
                }
            }
            return -1
        } else {
            return -1
        }
    }

    operator fun Hand.compareTo(other: Hand): Int {
        val thisStrength = strength()
        val otherStrength = other.strength();
        if (thisStrength > otherStrength)
            return 1
        else if (thisStrength == otherStrength) {
            // order-wise compare
            repeat(5) {
                val a = cards[it]
                val b = other.cards[it]
//                "compare $a to $b: ${a.toCardValue() > b.toCardValue()}".println()

                when {
                    a.toCardValue() > b.toCardValue() -> return 1
                    a.toCardValue() < b.toCardValue() -> return -1
                }
            }
            return -1
        } else {
            return -1
        }
    }

    fun part1(input: List<String>): Int {
       val hands = input.map { line ->
           val (h, b) = line.split(" ")
           Hand(h, b.toInt())
       }

        val sorted = hands.sortedWith(Hand::compareTo)
//        hands.println()
//        sorted.println()
        val result = sorted.mapIndexed { index, hand -> hand.bid*(index+1) }
//        result.println()
        return result.sum()
    }


    fun part2(input: List<String>): Int {
        val hands = input.map { line ->
            val (h, b) = line.split(" ")
            Hand(h, b.toInt())
        }

        val sorted = hands.sortedWith(Hand::compareTo2)
//        hands.println()
//        sorted.println()
        val result = sorted.mapIndexed { index, hand -> hand.bid*(index+1) }
//        result.println()
        return result.sum()
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
//    check(part1(testInput) == 6440)
//    part2(testInput).println()

    val input = readInput("Day07")
//    part1(input).println()
    part2(input).println()
}

