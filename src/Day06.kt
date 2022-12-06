fun main() {
  fun part(inputs: List<String>, size: Int): List<Int> = inputs
    .map { input -> input.windowed(size).indexOfFirst { it.toSet().size == size } + size }

  val testInput: List<String> = readLines("Day06_test")
  val input: List<String> = readLines("Day06")

  check(part(testInput, 4) == listOf(7, 5, 6, 10, 11))
  println(part(input, 4))

  check(part(testInput, 14) == listOf(19, 23, 23, 29, 26))
  println(part(input, 14))
}
