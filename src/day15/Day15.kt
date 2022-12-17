package day15

import geometry.*
import readLines
import kotlin.math.abs

data class Record(val sensor: Point, val beacon: Point, val distance: Long = distance(sensor, beacon) + 1)

val regex = Regex("""-?\d{1,7}""")

fun format(inputs: List<String>): Set<Record> = inputs
  .map { line ->
    regex.findAll(line).toList()
      .map { it.value }
      .let { (x1, y1, x2, y2) -> (x1.toLong() to y1.toLong()) to (x2.toLong() to y2.toLong()) }
      .let { (sensor, beacon) -> Record(sensor, beacon) }
  }
  .toSet()

fun distance(first: Point, second: Point): Long = abs(second.x - first.x) + abs(second.y - first.y)

val Record.border: Sequence<Point> get() = sequence {
  val left = sensor.x - distance
  val top = sensor.y - distance
  val right = sensor.x + distance
  val bottom = sensor.y + distance
  ((left to sensor.y)..(sensor.x to top)).forEach { point -> yield(point) }
  ((sensor.x to top)..(right to sensor.y)).forEach { point -> yield(point) }
  ((right to sensor.y)..(sensor.x to bottom)).forEach { point -> yield(point) }
  ((sensor.x to bottom)..(left to sensor.y)).forEach { point -> yield(point) }
}

fun main() {
  fun part1(inputs: List<String>, y: Long): Int {
    val records = format(inputs)
    val minX = records.minOf { (sensor, beacon, distance) -> minOf(sensor.x, beacon.x, sensor.x - distance) }
    val maxX = records.maxOf { (sensor, beacon, distance) -> maxOf(sensor.x, beacon.x, sensor.x + distance) }
    return (minX..maxX)
      .map { x -> x to y }
      .count { point ->
        records.any { (sensor, beacon, distance) -> beacon != point && distance(sensor, point) < distance }
      }
  }

  fun part2(inputs: List<String>, range: LongRange): Long {
    val records = format(inputs)
    val beacon =  records.flatMap { it.border }
      .filter { (x, y) -> x in range && y in range }
      .first { point ->
        records.none { (sensor, beacon, distance) -> distance(sensor, point) < distance }
      }
    return (beacon.x * 4000000) + beacon.y
  }

  val testInput = readLines("day15/test.txt")
  val input = readLines("day15/input.txt")

  check(part1(testInput, y = 10) == 26)
  println(part1(input, y = 2000000))
  check(part2(testInput, 0..20L) == 56000011L)
  println(part2(input, 0..4000000L))
}

@Deprecated("Debug only! - very very expensive to run")
fun println(records: Set<Record>, range: LongRange? = null) {
  fun Record.empty() = buildSet {
    (sensor.x - distance..sensor.x + distance).forEach { x ->
      (sensor.y - distance..sensor.y + distance).forEach { y ->
        val point: Point = x to y
        val localDistance = distance(point, sensor)
        if (localDistance < distance && point !in listOf(beacon, sensor)) add(point)
      }
    }
  }

  val sensors: Set<Point> = records.map { it.sensor }.toSet()
  val beacons: Set<Point>  = records.map { it.beacon }.toSet()
  val borders: Set<Point>  = records.flatMap { it.border }.toSet()

  fun Set<Record>.empty() = flatMap { record -> record.empty() }.toSet()

  val empty = records.empty()
  val points = sensors + beacons + empty

  val minX = points.minOf { it.x }
  val maxX = points.maxOf { it.x }
  val minY = points.minOf { it.y }
  val maxY = points.maxOf { it.y }

  for (y in minY..maxY) {
    for (x in minX..maxX) {
      val symbol = when (x to y) {
        in sensors -> 'S'
        in beacons -> 'B'
        in empty -> "#"
        in borders -> '@'
        else -> '.'
      }
      if (range == null || (x in range && y in range)) print(symbol)
    }
    if (range == null || (y in range)) {
      print(" $y")
      println()
    }
  }
}
