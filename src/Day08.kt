fun main() {

    data class WastelandNode(val id: String, val left: String, val right: String)

    data class Circular(val data: String) {
        fun forEach(f: (Char) -> Boolean) {
            var offset = 0
            while (true) {
                if (f(data[offset])) break
                if (++offset == data.length) {
                    offset = 0
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val directions = Circular(input.first())
        val nodes = input.drop(2).map { line ->
            val (id, left, right) = line.replace("[()]".toRegex(), "").split(" = ", ",")
//            println("$id -> ${left}, ${right}")
            id to WastelandNode(id, left.trim(), right.trim())
        }.toMap()
//        nodes.println()

        var nextNode = nodes["AAA"]!!
        var numSteps = 0
        directions.forEach { direction ->
            nextNode = when (direction) {
                'L' -> nodes[nextNode.left]!!
                'R' -> nodes[nextNode.right]!!
                else -> error("unknown direction $direction")
            }
            numSteps++
//            "next node is $nextNode".println()
            if (nextNode.id == "ZZZ") {
//                "done".println()
                return@forEach true
            }
            false
        }
        return numSteps
    }


    fun part2(input: List<String>): Long {
        val directions = input.first().toList()
        val nodes = input.drop(2).map { line ->
            val (id, left, right) = line.replace("[()]".toRegex(), "").split(" = ", ",")
            id to WastelandNode(id, left.trim(), right.trim())
        }.toMap()

        val startNodes = nodes.filterKeys { it.endsWith("A") }.keys.toList()

        val results = startNodes
            .map { node ->
                var numSteps = 0L
                var i = 0
                var next = node
                while (true) {
                    val lr = directions[i]
                    next = when (lr) {
                        'L' -> nodes[next]!!.left
                        'R' -> nodes[next]!!.right
                        else -> error("unknown direction $lr")
                    }
                    numSteps++
                    i++
//                    "$node: $next".println()
                    if (next.endsWith("Z"))
                        break
                    if (i == directions.size) i = 0
                }
                node to numSteps
            }
        "$results".println()
        val min = results.map { it.second }.min()
        var lcmNext = min
        val lcmSteps = results.map { it.second }

        while (true) {
            if (lcmSteps.all { lcmNext % it == 0L }) break
            lcmNext += min
        }
        return lcmNext
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
//    check(part1(testInput) == 2)
    check(part2(testInput) == 6L)

    val input = readInput("Day08")
//    part1(input).println()
    part2(input).println()
}
