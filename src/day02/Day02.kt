package day02

import day02.Hand.*
import day02.Outcome.*
import readLines

enum class Hand { Rock, Paper, Scissors; }
enum class Outcome { Loss, Draw, Win }

val Hand.score: Int get() = ordinal + 1
val Outcome.score: Int get() = ordinal * 3

val String.hand: Hand get() = when (this) {
  "A", "X" -> Rock
  "B", "Y" -> Paper
  "C", "Z" -> Scissors
  else -> error("Invalid hand")
}

val String.outcome: Outcome get() = when (this) {
  "X" -> Loss
  "Y" -> Draw
  "Z" -> Win
  else -> error("Invalid outcome")
}

infix fun Hand.against(elf: Hand): Outcome = when {
  this == elf -> Draw
  this == elf.winner -> Win
  else -> Loss
}

val Hand.winner: Hand get() = when (this) {
  Rock -> Paper
  Paper -> Scissors
  Scissors -> Rock
}

val Hand.loser: Hand get() = when (this) {
  Rock -> Scissors
  Paper -> Rock
  Scissors -> Paper
}

fun handFor(outcome: Outcome, with: Hand): Hand = when (outcome) {
  Loss -> with.loser
  Win -> with.winner
  Draw -> with
}

fun main() {
  fun format(input: List<String>): List<List<String>> = input
    .map { row -> row.split(" ") }

  fun part1(input: List<String>): Int = format(input)
    .map { (elf, you) -> elf.hand to you.hand }
    .sumOf { (elf, you) -> you.score + (you against elf).score }

  fun part2(input: List<String>): Int = format(input)
    .map { (elf, game) -> elf.hand to game.outcome }
    .sumOf { (elf, outcome) -> handFor(outcome, with = elf).score + outcome.score }

  // test if implementation meets criteria from the description, like:
  val testInput: List<String> = readLines("day02/test.txt")
  check(part1(testInput) == 15)
  check(part2(testInput) == 12)

  val input: List<String> = readLines("day02/input.txt")

  check(part1(input) == 14069)
  check(part2(input) == 12411)
}
