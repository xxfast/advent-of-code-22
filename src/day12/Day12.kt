package day12

import readLines
import kotlin.collections.ArrayDeque

typealias Grid<T> = List<List<T>>
operator fun <T> Grid<T>.get(x: Int, y: Int): T? = getOrNull(y)?.getOrNull(x)

fun <T> Grid<T>.firstByDepth(
  from: T,
  next: Grid<T>.(current: T) -> List<T>,
  evaluate: Grid<T>.(current: T, next: T) -> Boolean,
  until: (List<T>) -> Boolean,
): List<T> = sequence {
  val visited = mutableSetOf(from)
  val queue = ArrayDeque(listOf(listOf(from)))
  while (queue.isNotEmpty()) {
    val current = queue.removeFirst()
    yield(current)
    next(current.last())
      .filter { it !in visited && evaluate(current.last(), it) }
      .forEach {
        visited.add(it)
        queue.add(current.plus(it))
      }
  }
}.first { until(it) }

data class Cell(val x: Int, val y: Int, val value: Char)

val Cell.elevation: Int
  get() = when (value) {
    'S' -> 0
    'E' -> 26
    else -> value - 'a'
  }

val Grid<Cell>.start: Cell get() = flatten().first { it.value == 'S' }
val Grid<Cell>.end: Cell get() = flatten().first { it.value == 'E' }
fun Grid<Cell>.neighbours(x: Int, y: Int): List<Cell> =
  listOfNotNull(this[x, y - 1], this[x + 1, y], this[x, y + 1], this[x - 1, y])

fun Grid<Cell>.neighbours(cell: Cell): List<Cell> = neighbours(cell.x, cell.y)

fun Grid(input: List<String>): Grid<Cell> =
  input.mapIndexed { y, line -> line.toCharArray().toList().mapIndexed { x, c -> Cell(x, y, c) } }

fun Grid<Cell>.firstByDepth(
  from: Cell,
  to: Cell,
  next: Grid<Cell>.(current: Cell) -> List<Cell> = { current -> neighbours(current) },
  evaluate: Grid<Cell>.(current: Cell, next: Cell) -> Boolean =
    { current, next -> current.elevation <= next.elevation + 1 },
) = firstByDepth(from, next, evaluate) { it.contains(to) }

fun Grid<Cell>.firstByDepth(
  from: Cell,
  until: (List<Cell>) -> Boolean,
  next: Grid<Cell>.(current: Cell) -> List<Cell> = { current -> neighbours(current) },
  evaluate: Grid<Cell>.(current: Cell, next: Cell) -> Boolean =
    { current, next -> current.elevation <= next.elevation + 1 },
) = firstByDepth(from, next, evaluate, until)

fun main() {
  fun part1(input: List<String>): Int {
    val grid = Grid(input)
    val solution = grid.firstByDepth(from = grid.end, to = grid.start)
    return solution.size - 1
  }

  fun part2(input: List<String>): Int {
    val grid = Grid(input)
    val solution = grid.firstByDepth(
      from = grid.end,
      until = { it.any { cell -> cell.value == 'a' } }
    )
    return solution.size - 1
  }

  val testInput = readLines("day12/test.txt")
  val input = readLines("day12/input.txt")

  check(part1(testInput) == 31)
  println(part1(input))

  check(part2(testInput) == 29)
  println(part2(input))
}
