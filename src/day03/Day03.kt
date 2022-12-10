package day03

import readLines

fun main() {

  fun Sequence<Set<Char>>.scored() = this
    .map { it.map { char -> if (char.isLowerCase()) char - 'a' else char - 'A' + 26 } + 1 }
    .flatten()
    .sum()

  fun part1(input: List<String>) = input.asSequence()
    .map { rucksack -> rucksack.chunked(rucksack.length / 2) { it.toSet()} }
    .map { (first, second) -> first intersect second }
    .scored()

  fun part2(input: List<String>) = input.asSequence()
    .chunked(3) { group -> group.map { it.toSet() } }
    .map { (first, second, third) -> first intersect second intersect third }
    .scored()

  // test if implementation meets criteria from the description, like:
  val testInput: List<String> = readLines("day03/test.txt")
  check(part1(testInput) == 157)
  check(part2(testInput) == 70)

  val input: List<String> = readLines("day03/input.txt")
  println(part1(input))
  println(part2(input))
}
