package day14

import geometry.*
import readLines

val SOURCE: Point = 500L to 0L
val FALL_ORDER: List<Vector> = listOf(0L to +1L, -1L to +1L, +1L to +1L)

data class Simulation(val source: Point = SOURCE, val sand: MutableSet<Point> = mutableSetOf(), val rock: Set<Point>)

val Simulation.points get() = sand + rock

fun Simulation(paths: List<Path>): Simulation = Simulation(
  rock = paths
    .map { path: Path -> path.windowed(2).map { (start, end) -> start..end }.flatten().distinct() }
    .flatten()
    .toSet()
)

fun Simulation.flow(from: Point) = from
  .let { (x, y) -> FALL_ORDER.asSequence().map { vector -> from + vector } }
  .filterNot { point -> point in this.rock || point.second >= rock.maxY + 2 }

fun format(input: List<String>): List<Path> = input
  .map { line -> line.split("->").map { point -> point.trim().split(",").let { (x, y) -> x.toLong() to y.toLong() } } }

operator fun Simulation.contains(point: Point): Boolean = point.y < rock.maxY

fun main() {
  fun simulate(path: List<Path>, void: Boolean): Int {
    val simulation = Simulation(path)
    var current: Point = simulation.source
    while (simulation.source !in simulation.sand) {
      val next = simulation.flow(current).firstOrNull { it !in simulation.sand }
      if (next == null) simulation.sand += current
      current = next ?: simulation.source
      if (void && current !in simulation) break
    }

    return simulation.sand.size
  }

  fun part1(input: List<String>): Int = simulate(format(input), void = true)
  fun part2(input: List<String>): Int = simulate(format(input), void = false)

  val testInput = readLines("day14/test.txt")
  val input = readLines("day14/input.txt")

  check(part1(testInput) == 24)
  println(part1(input))

  check(part2(testInput) == 93)
  println(part2(input))
}

// For debug
fun println(simulation: Simulation) {
  for (y in simulation.points.minY..simulation.points.maxY) {
    for (x in simulation.points.minX..simulation.points.maxX) {
      val symbol = when (x to y) {
        simulation.source ->  '+'
        in simulation.rock -> '█'
        in simulation.sand -> 'o'
        else -> ' '
      }
      print(symbol)
    }
    println()
  }
}
