fun main() {


    data class Point(val row: Int, val column: Int, val value: String? = null)

    fun Point.neighboringPoints(): List<Point> {
        val result = mutableListOf<Point>()
        result += this.copy(value = null, row = row - 1) // N
        result += this.copy(value = null, row = row + 1) // S

        result += this.copy(value = null, column = column + 1) // E
        result += this.copy(value = null, column = column - 1) // W

        result += this.copy(value = null, row = row - 1, column = column + 1) // NE
        result += this.copy(value = null, row = row - 1, column = column - 1) // NW

        result += this.copy(value = null, row = row + 1, column = column + 1) // SE
        result += this.copy(value = null, row = row + 1, column = column - 1) // SW

        return result.toList()
    }
    data class Schematic(val points: List<Point>)

    fun Schematic.valueAt(point: Point): Point? {
        return points.firstOrNull { p: Point ->
            p.row == point.row && p.column == point.column
        }
    }

    fun Schematic.allGears(): List<Point> {
        return points.filter { point: Point -> point.value == "*" }
    }


    data class NumberWithCoordinate(val points: List<Point>, val value: Int)

    fun Schematic.allNumbers(): List<NumberWithCoordinate> {
        val result = mutableListOf<NumberWithCoordinate>()
        points.groupBy {it.row }.forEach { entry ->
            val num = StringBuilder("")
            val numPoints = mutableListOf<Point>()
            entry.value.forEach { point ->
                if (point.value?.toIntOrNull() != null) {
                    num.append(point.value)
                    numPoints += point
                } else {
                    if (num.isNotEmpty()) {
                        result += NumberWithCoordinate(numPoints.toList(), num.toString().toInt())
                    }
                    num.clear()
                    numPoints.clear()
                }
            }
        }
        return result
    }

    fun Schematic.adjacentParts(point: Point): List<NumberWithCoordinate> {
        val parts = allNumbers()
        val adjacentParts = point.neighboringPoints().filter { neighbor ->
            parts.any { part -> part.points.any {
                it.row == neighbor.row && it.column == neighbor.column
            } }
        }
        val result = parts.filter { numberedPart ->
            numberedPart.points.any { partPoint ->
                adjacentParts.any() { it.row == partPoint.row && it.column == partPoint.column }
            }
        }
        return result
    }

    fun NumberWithCoordinate.hasAdjacentSymbol(
        schematic: Schematic,
        exclude: Set<String> = setOf(".") + (0..9).map { it.toString() }.toSet()
    ): Boolean {
//        println("checking $this against $exclude")
        val adjacentPoints = this.points.filter { point: Point ->
                point.neighboringPoints().any { neighbor ->
                    val neighborValue = schematic.valueAt(neighbor)?.value
                    if (neighborValue == null)
                        false
                    else {
                    val hasValidNeighbor = ! exclude.contains(neighborValue)
//                    println("neighbor($neighbor) value $neighborValue --> $hasValidNeighbor")
                    hasValidNeighbor}
                }
            }
       return adjacentPoints.isNotEmpty()
    }

    fun List<String>.toSchematic(): Schematic {
        val points = this.flatMapIndexed { row: Int, line: String ->
            line.mapIndexed { column, value ->
                Point(row, column, value.toString())
            }
        }
        return Schematic(points)
    }

    fun part1(input: List<String>): Int {
        val schematic = input.toSchematic()
//        println(schematic.allNumbers())
//        println(schematic.allNumbers().filter { it.hasAdjacentSymbol(schematic) })
        return schematic.allNumbers().filter { it.hasAdjacentSymbol(schematic) }.sumOf {
            it.value
        }
    }

    fun part2(input: List<String>): Int {
        val schematic = input.toSchematic()
//        schematic.allGears().println()
        val partsNearGears = schematic.allGears().map { point: Point ->
            schematic.adjacentParts(point)
        }
        return partsNearGears.filter { it.size > 1 }.sumOf { parts -> parts.fold(1) {acc: Int, p: NumberWithCoordinate -> acc * p.value } }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
//    println(part1(testInput))
//    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)
    part2(testInput).println()

    val input = readInput("Day03")
//    part1(input).println()
    part2(input).println()
}


