package day11

import readText

data class Monkey(
  var items: MutableList<Long>, val operation: (Long) -> Long, val test: Long,
  val ifTrue: Int, val ifFalse: Int, var inspected: Int = 0
)

fun format(input: String): List<Monkey> = input.split("\n\n")
  .map { description ->
    val descriptions = description.split("\n")
    Monkey(
      items = descriptions[1].substringAfter(":")
        .trim().split(",").map { it.trim().toLong() }.toMutableList(),
      operation = descriptions[2].let { line ->
        val lastWord = line.split(" ").last().trim()
        val number = if (lastWord == "old") null else lastWord.trim().toLong()
        if (line.contains("*")) { worry -> worry * (number ?: worry) }
        else { worry -> worry + (number ?: worry) }
      },
      test = descriptions[3].split(" ").last().trim().toLong(),
      ifTrue = descriptions[4].split(" ").last().trim().toInt(),
      ifFalse = descriptions[5].split(" ").last().trim().toInt(),
    )
  }

fun List<Monkey>.score(): Long = this
  .sortedByDescending { it.inspected }.take(2)
  .let { (top, second) -> top.inspected.toLong() * second.inspected.toLong() }

fun List<Monkey>.game(round: Int, manage: List<Monkey>.(worry: Long) -> Long) = apply {
  repeat(round) {
    forEach { monkey ->
      monkey.items.toList().forEach { worry ->
        monkey.items.remove(worry)
        val inspection: Long = manage(monkey.operation(worry))
        val test: Boolean = inspection % monkey.test == 0L
        val next: Int = if (test) monkey.ifTrue else monkey.ifFalse
        get(next).items += inspection
        monkey.inspected++
      }
    }
  }
}

val List<Monkey>.commonTest: Long
  get() =
    map { it.test }.reduce { divider, test -> divider * test }

fun main() {
  fun part1(input: String): Long = format(input)
    .game(20) { worry -> worry / 3 }
    .score()

  fun part2(input: String): Long = format(input)
    .game(10000) { worry -> worry % commonTest }
    .score()

  val testInput = readText("day11/test.txt")
  val input = readText("day11/input.txt")

  check(part1(testInput) == 10605L)
  println(part1(input))

  check(part2(testInput) == 2713310158L)
  println(part2(input))
}
