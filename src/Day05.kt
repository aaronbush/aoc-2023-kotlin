import kotlin.math.min

fun main() {

    data class MappingRange(val destinationStart: Long, val sourceStart: Long, val length: Long) {
        //        val destinationRange = (destinationStart..<destinationStart + length)
        val sourceRange = sourceStart..<sourceStart + length

        fun locate(id: Long): Long? {
            if (id in sourceRange) {
                val offset = id - sourceStart
                return destinationStart + offset
            }
            return null
        }
    }

    data class
    Mapping(val mapName: String, val mappingRanges: List<MappingRange>) {
        fun locate(id: Long): Long {
            return mappingRanges.mapNotNull { range ->
                range.locate(id)
            }
                .getOrElse(0) {
                    id
                }
        }
    }


    fun getMappingFor(mapName: String, input: List<String>) =
        input.dropWhile { !it.contains(mapName) }.drop(1)
            .takeWhile { it.isNotEmpty() }
            .map { line ->
                val (destStart, sourceStart, length) = line.split(" ").map { it.toLong() }
                MappingRange(destStart, sourceStart, length)
            }
            .let { Mapping(mapName, it) }

    fun locateNext(
        locations: Sequence<Long>,
        mapping: Mapping
    ) = locations.map { id ->
        mapping.locate(id)
        // .also { "${mapping.mapName}: $id -> $it".println() }
    }

    fun resolveLocation(
        seeds: Sequence<Long>,
        seedToSoilMapping: Mapping,
        soilToFertilizerMapping: Mapping,
        fertilizerToWaterMapping: Mapping,
        waterToLightMapping: Mapping,
        lightToTemperatureMapping: Mapping,
        temperatureToHumidityMapping: Mapping,
        humidityToLocationMapping: Mapping
    ) =
        locateNext(seeds, seedToSoilMapping)
            .let { locateNext(it, soilToFertilizerMapping) }
            .let { locateNext(it, fertilizerToWaterMapping) }
            .let { locateNext(it, waterToLightMapping) }
            .let { locateNext(it, lightToTemperatureMapping) }
            .let { locateNext(it, temperatureToHumidityMapping) }
            .let { locateNext(it, humidityToLocationMapping) }


    fun part1(input: List<String>): Long {
        val seeds = input.first().split(" ").drop(1).map { it.toLong() }
        val seedToSoilMapping = getMappingFor("seed-to-soil map:", input)
        val soilToFertilizerMapping = getMappingFor("soil-to-fertilizer map:", input)
        val fertilizerToWaterMapping = getMappingFor("fertilizer-to-water map:", input)
        val waterToLightMapping = getMappingFor("water-to-light map:", input)
        val lightToTemperatureMapping = getMappingFor("light-to-temperature map:", input)
        val temperatureToHumidityMapping = getMappingFor("temperature-to-humidity map:", input)
        val humidityToLocationMapping = getMappingFor("humidity-to-location map:", input)

        val result = resolveLocation(
            seeds.asSequence(),
            seedToSoilMapping,
            soilToFertilizerMapping,
            fertilizerToWaterMapping,
            waterToLightMapping,
            lightToTemperatureMapping,
            temperatureToHumidityMapping,
            humidityToLocationMapping
        )

        return result.min()
    }


    fun part2(input: List<String>): Long {
        val seeds = input.first().split(" ").drop(1).map { it.toLong() }
        val seedRanges = seeds.chunked(2)
            .map { (start, length) -> start..<start + length }
        val seedToSoilMapping = getMappingFor("seed-to-soil map:", input)
        val soilToFertilizerMapping = getMappingFor("soil-to-fertilizer map:", input)
        val fertilizerToWaterMapping = getMappingFor("fertilizer-to-water map:", input)
        val waterToLightMapping = getMappingFor("water-to-light map:", input)
        val lightToTemperatureMapping = getMappingFor("light-to-temperature map:", input)
        val temperatureToHumidityMapping = getMappingFor("temperature-to-humidity map:", input)
        val humidityToLocationMapping = getMappingFor("humidity-to-location map:", input)

        var smol = Long.MAX_VALUE
        val result = seedRanges.map {
            resolveLocation(
                it.asSequence(),
                seedToSoilMapping,
                soilToFertilizerMapping,
                fertilizerToWaterMapping,
                waterToLightMapping,
                lightToTemperatureMapping,
                temperatureToHumidityMapping,
                humidityToLocationMapping
            )
                .also { "ready for min".println() }
                .forEach {
                    smol = min(smol, it) // slow
                }
                .also { smol.println() }
        }
        return smol
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
//    check(part1(testInput) == 35L)
//    check(part2(testInput) == 46L)

    val input = readInput("Day05")
//    part1(input).println()
    part2(input).println()
}

