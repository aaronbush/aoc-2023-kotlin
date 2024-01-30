

fun main() {
    val textNums = arrayOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    val nums = 1..9

    fun isNum(): (Char) -> Boolean {
        val isNum = { char: Char ->
            try {
                char.digitToInt();
                true
            } catch (e: Exception) {
                false
            }
        }
        return isNum
    }

    fun part1(input: List<String>): Int {
        val firstNum = input.map { it.first(isNum()) }
        val lastNum = input.map { it.last(isNum()) }
        val sum = firstNum.zip(lastNum) { a, b -> "$a$b".toInt()}.sum()
        println(sum)
        return sum
    }

    fun String.allIndicies(s: String): List<Int> {
        var offset = 0
        val end = this.length
        var result = mutableListOf<Int>()
        while (offset < end) {
            val i = this.indexOf(s, offset).apply { if (this >= 0) result += this }
            offset += 1
        }
        return result
    }

    fun part2(input: List<String>): Int {
        val result = input.sumOf { line ->
            val textNumsAt = textNums.flatMapIndexed { i, n ->
                line.allIndicies(n).map { it to i + 1 } // adding one to indicate the 'real' value
            }
                .filter { it.first >= 0 }
                .sortedBy { pair -> pair.first }
            val textNumsFirst = textNumsAt.firstOrNull() ?: (Int.MAX_VALUE to null)
            val textNumsLast = textNumsAt.lastOrNull() ?: (Int.MIN_VALUE to null)

            val numNumsAt = nums.flatMap { n ->
                line.allIndicies(n.toString()).map { it to n }
            }
                .filter { it.first >= 0 }
                .sortedBy { pair -> pair.first }
            val numFirst = numNumsAt.firstOrNull() ?: (Int.MAX_VALUE to null)
            val numLast = numNumsAt.lastOrNull() ?: (Int.MIN_VALUE to null)

            println("line = ${line}")
//            println("textNumsAt = ${textNumsAt}")
//            println("textNumsFirst = ${textNumsFirst}")
//            println("textNumsLast = ${textNumsLast}")
//            println("numNumsAt = ${numNumsAt}")
//            println("numFirst = ${numFirst}")
//            println("numLast = ${numLast}")

            val firstNum = minOf(textNumsFirst, numFirst) { a, b ->
                if (a.first < b.first) -1 else 1
            }
            val lastNum = maxOf(textNumsLast, numLast) { a, b ->
                if (a.first < b.first) -1 else 1
            }

//            println("firstNum = ${firstNum}")
//            println("lastNum = ${lastNum}")
            println("Real Number: ${firstNum.second}${lastNum.second}")
            "${firstNum.second}${lastNum.second}".toInt()
        }
        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 142)

    val input = readInput("Day01")
//    part1(input).println()
    part2(input).println()
}
