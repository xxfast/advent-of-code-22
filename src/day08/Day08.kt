package day08

import readLines

typealias Grid = List<List<Int>>

val List<String>.grid: Grid get() = this.map { line -> line.toList().map { it.digitToInt() } }
val Grid.height: Int get() = size
val Grid.width: Int get() = first().size

fun <T> Grid.map(block: (row: Int, column: Int, i: Int) -> T) =
  (0 until height).map { row -> (0 until width).map { column -> block(row, column, this[row][column]) } }

fun <T> Grid.map(block: (i: Int, top: List<Int>, right: List<Int>, bottom: List<Int>, left: List<Int>) -> T) =
  map { row, column, i -> block(i, top(row, column), right(row, column), bottom(row, column), left(row, column)) }

fun Grid.top(row: Int, column: Int): List<Int> = (row - 1 downTo 0).map { r -> this[r][column] }
fun Grid.right(row: Int, column: Int): List<Int> = (column + 1 until width).map { c -> this[row][c] }
fun Grid.bottom(row: Int, column: Int): List<Int> = (row + 1 until height).map { r -> this[r][column] }
fun Grid.left(row: Int, column: Int): List<Int> = (column - 1 downTo 0).map { c -> this[row][c] }

// TODO: Why is this not on stdlib 🤯
fun <T> List<T>.takeUntil(predicate: (T) -> Boolean): List<T> {
  val take: MutableList<T> = mutableListOf()
  for (i in this) {
    take += i
    if (predicate(i)) break
  }
  return take
}

fun main() {
  fun part1(grid: Grid): Int = grid
    .map { i, top, right, bottom, left ->
      top.all { it < i } || right.all { it < i } || bottom.all { it < i } || left.all { it < i }
    }
    .flatten()
    .count { it }

  fun List<Int>.score(i: Int): Int = takeUntil { it >= i }.count()

  fun part2(grid: Grid): Int = grid
    .map { i, top, right, bottom, left -> top.score(i) * right.score(i) * bottom.score(i) * left.score(i) }
    .flatten()
    .max()

  val testInput: List<String> = readLines("day08/test.txt")
  val input: List<String> = readLines("day08/input.txt")

  check(part1(testInput.grid) == 21)
  check(part1(input.grid) == 1825)
  println(part1(input.grid))

  check(part2(testInput.grid) == 8)
  check(part2(input.grid) == 235200)
  println(part2(input.grid))
}
