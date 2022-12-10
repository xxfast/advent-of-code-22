package day05

import readText

typealias Crate = Char
typealias Stack = MutableList<Crate>

data class Instruction(val count: Int, val fromIndex: Int, val toIndex: Int)
data class Crane(val stacks: List<Stack>, val instructions: List<Instruction>)

// Parsing is long 🤮
fun Crane(input: String): Crane {
  val lines: List<String> = input.lines()
  fun String.filterSpaces(): String = filter { it != ' ' }
  val stackDescriptionIndex: Int = lines.indexOfFirst { line -> line.filterSpaces().all { it.isDigit() } }
  val stackDescription: String = lines[stackDescriptionIndex]
  val stackIndexes: Map<Int, Int> = stackDescription
    .mapIndexed { index, char -> char to index }
    .filter { (char, _) -> char.isDigit() }
    .associate { (char, index) -> char.digitToInt() to index }

  val width: Int = stackDescription.filterSpaces().map { char -> char.digitToInt() }.max()
  val height: Int = stackDescriptionIndex

  val stackDescriptions: List<String> = lines.subList(0, height)
  val stacks: MutableList<MutableList<Char>> = MutableList(width) { mutableListOf() }

  for (rowIndex in height - 1 downTo 0) {
    val row = stackDescriptions[rowIndex]
    for (columnIndex in 0 until width) {
      val charIndex = stackIndexes[columnIndex + 1]!!
      val char = row.getOrNull(charIndex)
      if (char != null && char.isLetter()) {
        stacks[columnIndex].add(char)
      }
    }
  }

  val instructions: List<Instruction> = lines
    .drop(stackDescriptionIndex + 2)
    .dropLast(1)
    .map { line ->
      val (count, from, to) = line
        .split(" ")
        .filter { word -> word.all { it.isDigit() } }
        .map { number -> number.toInt() }

      Instruction(count, from, to)
    }

  return Crane(stacks, instructions)
}

fun Crane.stackTops(): List<Crate> = stacks.map { it.last() }

fun Crane.move(operation: (count: Int, from: Stack, to: Stack) -> Unit): Crane = apply {
  instructions.forEach { (count, fromIndex, toIndex) ->
    val from: Stack = stacks[fromIndex - 1]
    val to: Stack = stacks[toIndex - 1]
    operation(count, from, to)
  }
}

// TODO: Why is this not on stdlib 🤯
fun <E> MutableList<E>.removeLast(count: Int): List<E> {
  val last: List<E> = takeLast(count)
  repeat(count) { removeLast() }
  return last
}

fun main() {
  fun part1(input: String): String = Crane(input)
    .move { count, from, to ->
      repeat(count) {
        val crate: Crate = from.removeLast()
        to.add(crate)
      }
    }
    .stackTops()
    .joinToString("")

  fun part2(input: String): String = Crane(input)
    .move { count, from, to ->
      val crates: List<Crate> = from.removeLast(count)
      to.addAll(crates)
    }
    .stackTops()
    .joinToString("")

  // test if implementation meets criteria from the description, like:
  val testInput: String = readText("day05/test.txt")
  val input: String = readText("day05/input.txt")
  check(part1(testInput) == "CMZ")
  check(part2(testInput) == "MCD")

  println(part1(input))
  println(part2(input))
}
