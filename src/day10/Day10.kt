package day10

import readLines

typealias Screen = String

val EMPTY_SCREEN: Screen = """
  ........................................
  ........................................
  ........................................
  ........................................
  ........................................
  ........................................
""".trimIndent()

val TEST_SCREEN: String = """
  ██..██..██..██..██..██..██..██..██..██..
  ███...███...███...███...███...███...███.
  ████....████....████....████....████....
  █████.....█████.....█████.....█████.....
  ██████......██████......██████......████
  ███████.......███████.......███████.....
""".trimIndent()

fun Screen.draw(cycle: Int): String {
  val stream = this.lines().flatMap { it.toList() }.toMutableList()
  stream[cycle] = '█'
  return stream.chunked(40).joinToString("\n") { it.joinToString("") }
}

fun main() {
  fun format(inputs: List<String>): List<Int> = inputs
    .flatMap { line ->
      if (line[0] == 'a') listOf(0, line.split(" ").last().toInt())
      else listOf(0)
    }
    .scan(1) { x, cycle -> x + cycle }

  fun part1(inputs: List<String>): Int = format(inputs)
    .mapIndexed { cycle, x -> (cycle + 1) * x }
    .filterIndexed { cycle, _ -> (cycle + 1) in (20..220 step 40) }
    .sum()

  fun part2(inputs: List<String>): Screen = format(inputs)
    .drop(0)
    .foldIndexed(EMPTY_SCREEN) { cycle, screen, x ->
      val inSprite = (cycle % 40) in x - 1..x + 1
      if (inSprite) screen.draw(cycle)
      else screen
    }

  val testInput: List<String> = readLines("day10/test.txt")
  val input: List<String> = readLines("day10/input.txt")

  check(part1(testInput) == 13140)
  println(part1(input))

  check(part2(testInput) == TEST_SCREEN)
  println(part2(input))
}
