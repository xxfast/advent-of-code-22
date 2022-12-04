fun main() {
  fun format(input: List<String>): List<List<IntRange>> =
    input.map { line -> line.split(",").map { it.split("-").let { (start, end) -> start.toInt()..end.toInt() } } }

  fun part1(input: List<String>): Int =
    format(input).count { (first, second) -> (first - second).isEmpty() || (second - first).isEmpty() }

  fun part2(input: List<String>): Int = format(input).count { (first, second) -> first.intersect(second).isNotEmpty() }

  // test if implementation meets criteria from the description, like:
  val testInput: List<String> = readLines("Day04_test")
  check(part1(testInput) == 2)
  check(part2(testInput) == 4)

  val input: List<String> = readLines("Day04")
  println(part1(input))
  println(part2(input))
}
