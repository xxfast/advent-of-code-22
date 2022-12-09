package day09

import day09.Direction.*
import readLines
import kotlin.math.abs

enum class Direction(val dx: Int, val dy: Int) { R(+1, 0), U(0, -1), L(-1, 0), D(0, +1) }
typealias Move = Pair<Direction, Int>

val List<String>.moves: List<Move>
  get() = this.map {
    it.split(" ").let { (direction, amount) -> valueOf(direction) to amount.toInt() }
  }

class Knot(val index: Int, var x: Int, var y: Int, var next: Knot? = null) {
  val visited: MutableSet<Pair<Int, Int>> = mutableSetOf(x to y)
}

typealias Rope = List<Knot>

val Rope.head: Knot get() = first()
val Rope.tail: Knot get() = last()

fun Rope(length: Int, x: Int = 0, y: Int = 0): Rope {
  val rope: MutableList<Knot> = mutableListOf()
  for (i in length - 1 downTo 0) {
    rope += Knot(i, x, y, next = rope.lastOrNull())
  }
  return rope.reversed()
}

fun Knot.move(dx: Int, dy: Int, amount: Int) {
  x += dx
  y += dy
  visited += x to y
  val next: Knot = next ?: return
  val nextDx = next.x - x
  val nextDy = next.y - y
  if (abs(nextDx) <= 1 && abs(nextDy) <= 1) return
  val cx = -nextDx.coerceIn(-1..1)
  val cy = -nextDy.coerceIn(-1..1)
  next.move(cx, cy, amount - 1)
}

fun Rope.move(direction: Direction, amount: Int) {
  repeat(amount) { head.move(direction.dx, direction.dy, amount) }
}

fun Rope.move(moves: List<Move>): Rope = apply {
  moves.forEach { (direction, amount) -> move(direction, amount) }
}

fun main() {
  fun part1(inputs: List<String>): Int = Rope(2).move(inputs.moves).tail.visited.count()
  fun part2(inputs: List<String>): Int = Rope(10).move(inputs.moves).tail.visited.count()

  val testInput: List<String> = readLines("day09/Day09_Test")
  check(part1(testInput) == 13)
  check(part2(testInput) == 1)

  val input: List<String> = readLines("day09/Day09")
  check(part1(input) == 6266)

  val testInput2: List<String> = readLines("day09/Day09_Test2")
  check(part2(testInput2) == 36)
  check(part2(input) == 2369)
}

// For debugging
fun println(rope: Rope, width: Int, height: Int) {
  val startX = rope.head.x
  val startY = rope.head.y
  for (y in -height..height) {
    for (x in -width..width) {
      val knot = rope.find { knot -> knot.x == x && knot.y == y }
      val tail = rope.tail
      when {
        knot == null && y == startY && x == startX -> print("s")
        knot == null && tail.visited.contains(x to y) -> print("#")
        knot == null -> print(".")
        knot.index == 0 -> print("H")
        knot.index == rope.lastIndex -> print("T")
        else -> print(knot.index)
      }
    }
    println()
  }
}
