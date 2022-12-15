package day14

import readLines
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sqrt

typealias Point = Pair<Int, Int>
typealias Vector = Pair<Int, Int>
typealias Path = List<Point>

val Point.x: Int get() = first
val Point.y: Int get() = second

infix fun Point.towards(other: Point): Vector = (other.x - this.x) to (other.y - this.y)

fun Vector.normalised(): Vector {
  val length = sqrt((x * x + y * y).toFloat())
  return (x / length).roundToInt() to (y / length).roundToInt()
}

operator fun Vector.times(magnitude: Int): Vector = (x * magnitude) to (y * magnitude)
operator fun Point.plus(other: Point): Point = (this.x + other.x) to (this.y + other.y)

operator fun Point.rangeTo(other: Point): Path = buildList {
  add(this@rangeTo)
  val direction = this@rangeTo towards other
  repeat(maxOf(abs(direction.x), abs(direction.y))) { step ->
    add(this@rangeTo + direction.normalised() * (step + 1))
  }
}

val Set<Point>.minX: Int get() = minOf { it.x }
val Set<Point>.maxX: Int get() = maxOf { it.x }
val Set<Point>.minY: Int get() = minOf { it.y }
val Set<Point>.maxY: Int get() = maxOf { it.y }

val SOURCE: Point = 500 to 0
val FALL_ORDER: List<Vector> = listOf(0 to +1, -1 to +1, +1 to +1)

data class Simulation(val source: Point = SOURCE, val sand: MutableSet<Point> = mutableSetOf(), val rock: Set<Point>)
val Simulation.points: Set<Pair<Int, Int>> get() = sand + rock

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
  .map { line -> line.split("->").map { point -> point.trim().split(",").let { (x, y) -> x.toInt() to y.toInt() } } }

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
