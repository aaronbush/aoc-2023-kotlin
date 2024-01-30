fun main() {


    data class Play(val color: String, val count: Int)

    data class Game(val id: Int, val plays: Map<Int, List<Play>>)

    fun Game.canBePlayedWith(criteria: Play): Boolean {
        return plays.values.filter { play ->
            play.any { it.color == criteria.color }
        }.all { play ->
            play.none {it.color == criteria.color && it.count > criteria.count}
        }
    }

    fun Game.minimumPieces(): List<Play> {
        // what is the max of each color across all plays
        val gamePlays = plays.values.flatten()
        val gamePlaysByColor = gamePlays.groupingBy { it.color }
        val greatest = gamePlaysByColor.reduce { _: String, accumulator: Play, element: Play ->
            if (element.count > accumulator.count) {
                element
            } else {
                accumulator
            }
        }
        return greatest.values.toList()
    }

    fun String.parseGame(): Game {
        val gameId = this.substringBefore(':').substringAfter("Game ").toInt()
        val plays = this.substringAfter(":").split(";")
            .mapIndexed { playNum, play ->
                val cubes = play.split(",").map { cube ->
                    val count = cube.trim().split(" ").first().toInt()
                    val color = cube.trim().split(" ").last()
                    Play(color, count)
                }
                playNum to cubes
            }
        val p = plays.associate { it.first to it.second }
        return Game(gameId, p)
    }

    fun part1(input: List<String>): Int {
        // test: 12 red cubes, 13 green cubes, and 14 blue cubes
        val criteria = listOf(
            Play("red", 12),
            Play("green", 13),
            Play("blue", 14))

        val games = input.map { line ->
            line.parseGame()
        }
        val possibleGames = games.filter { game ->
            criteria.all { criterion ->
                game.canBePlayedWith(criterion)
            }
        }
        val notPossibleGames = games.filterNot { game ->
            criteria.all { criterion ->
                if (! game.canBePlayedWith(criterion)) println("criterion = ${criterion}")
                game.canBePlayedWith(criterion)
            }
        }
//        println("possibleGames = ${possibleGames.joinToString(separator = "\n")}")
        println("notPossibleGames = ${notPossibleGames.joinToString("\n")}")
        val result = possibleGames.sumOf { game -> game.id }
//        println(games[2])
//        println(games[2].canBePlayedWith(Play("red", 12)))
        return result
    }



    fun part2(input: List<String>): Int {
        val games = input.map { line ->
            line.parseGame()
        }
        val gamePieces = games.map {
            val minPieces = it.minimumPieces()
            minPieces.map { play -> play.count }.reduce { acc: Int, i: Int -> acc * i }
        }

        return gamePieces.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
//    check(part1(testInput) == 8)
    part2(testInput)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
//    part1(input).println()
    part2(input).println()
}
