package day01

import readText

fun main() {
  fun format(text: String): List<Int> = text
    .split("\n\n")
    .map { elf -> elf.split("\n").sumOf { snack -> snack.toInt() } }

  fun part1(input: String): Int = format(input).max()
  fun part2(input: String): Int = format(input).sortedDescending().take(3).sum()

  // test if implementation meets criteria from the description, like:
  val testInput: String = readText("day01/test.txt")
  check(part1(testInput) == 24000)

  val input: String = readText("day01/input.text")
  println(part1(input))
  println(part2(input))
}
