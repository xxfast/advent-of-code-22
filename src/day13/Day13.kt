package day13

import readText

// Parsing like an animal 🤯
fun format(line: String): List<Any> =
  buildList {
    var index = 0
    var number = ""
    while (true) {
      if (index > line.lastIndex) break
      var char = line[index++]
      when {
        char == ',' && number.isNotBlank() -> {
          add(number.toInt())
          number = ""
        }

        char.isDigit() -> number += char

        char == '[' -> {
          val start = index
          var depth = 1

          while (depth != 0) {
            char = line[index++]
            if (char == '[') depth++
            if (char == ']') depth--
          }

          val end = index - 1
          add(format(line.substring(start until end)).toList())
        }
      }
    }

    if (number.isNotBlank()) add(number.toInt())
  }
  .also { check("[${line}]" == it.toString().replace(" ", "")) { "Failed to parse $line" } }

// Why is this not in stdlib 🤯
fun <T, R> Iterable<T>.zipWithNull(other: Iterable<R>): List<Pair<T?, R?>> = buildList {
  val first = this@zipWithNull.iterator()
  val second = other.iterator()
  while (first.hasNext() || second.hasNext()) {
    val left = if (first.hasNext()) first.next() else null
    val right = if (second.hasNext()) second.next() else null
    add(left to right)
  }
}

operator fun <T> List<T>.compareTo(other: List<T>): Int {
  val list = zipWithNull(other)
  var order: Int
  for ((left, right) in list) {
    order = when {
      left is Int && right is Int -> -left.compareTo(right)
      left is Int && right is List<*> -> listOf(left).compareTo(right)
      left is List<*> && right is Int -> left.compareTo(listOf(right))
      left is List<*> && right is List<*> -> left.compareTo(right)
      left == null && (right is List<*> || right is Int) -> 1 // left ran out of numbers
      (left is List<*> || left is Int) && right == null -> -1 // right ran out of numbers
      else -> 0
    }

    if (order != 0) return order
  }
  return 0
}

val Dividers = listOf(listOf(listOf(2)), listOf(listOf(6)))

fun main() {
  fun part1(input: String): Int = input.split("\n\n")
    .map { pair -> pair.split("\n") }
    .mapIndexed { index, (left, right) -> index to format(left).compareTo(format(right)) }
    .filter { (_, order) -> order > 0 }
    .sumOf { (index, _) -> index + 1 }

  fun part2(input: String): Int = input
    .split("\n").filter { it.isNotBlank() }
    .map { format(it) }
    .plus(Dividers)
    .sortedWith { first, second -> second.compareTo(first) }
    .let { sorted ->
      val (start, end) = Dividers
      val startIndex = sorted.indexOf(start) + 1
      val endIndex = sorted.indexOf(end) + 1
      startIndex * endIndex
    }

  val testInput = readText("day13/test.txt")
  val input = readText("day13/input.txt")

  check(part1(testInput) == 13)
  println(part1(input))

  check(part2(testInput) == 140)
  println(part2(input))
}
