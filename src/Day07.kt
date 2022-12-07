const val MAX_SIZE = 100000L
const val TOTAL_AVAILABLE_SIZE = 70000000L
const val UPDATE_SIZE = 30000000L

typealias Indexes = MutableList<Index>

val empty: Indexes get() = mutableListOf()

sealed interface Index { val name: String; val parent: Dir?; val size: Long }
class File(override val name: String, override val parent: Dir, override val size: Long) : Index
data class Dir(override val name: String, override val parent: Dir?, val indexes: Indexes) : Index {
  override val size: Long get() = indexes.sumOf { index -> index.size }
}

class Cursor(val root: Dir, var head: Dir = root)

@DslMarker annotation class CursorDsl
@CursorDsl val Root: Dir get() = Dir(name = "/", parent = null, indexes = empty)
@CursorDsl val Dir.dirs: List<Dir> get() = indexes.filterIsInstance<Dir>()
@CursorDsl val Dir.flatDirs: List<Dir> get() = dirs.map { listOf(it) + it.flatDirs }.flatten()
@CursorDsl val Dir.spaceAfterUpdate: Long get() = UPDATE_SIZE - (TOTAL_AVAILABLE_SIZE - size)
@CursorDsl val String.isCommand: Boolean get() = startsWith("$")
@CursorDsl val String.isOutput: Boolean get() = !isCommand
@CursorDsl val String.param: String get() = split(" ").last()
@CursorDsl val String.isCd: Boolean get() = startsWith("$ cd")
@CursorDsl val String.size: Long get() = split(" ").first().toLong()
@CursorDsl val String.name: String get() = split(" ").last()
@CursorDsl val String.isDir: Boolean get() = split(" ").first() == "dir"
@CursorDsl val String.isBack: Boolean get() = endsWith("..")
@CursorDsl val String.isRoot: Boolean get() = endsWith("/")

fun Cursor(input: List<String>, root: Dir = Root): Cursor = Cursor(root).apply {
  input.forEach { line ->
    when {
      line.isCommand && line.isCd -> head = when {
        line.param.isBack -> head.parent!!
        line.param.isRoot -> root
        else -> head.dirs.first { dir -> dir.name == line.param }
      }

      line.isOutput -> head.indexes += when {
        line.isDir -> Dir(line.name, head, empty)
        else -> File(line.name, head, line.size)
      }
    }
  }
}

fun main() {
  fun part1(inputs: List<String>): Long = Cursor(inputs)
    .root.flatDirs
    .filter { index: Index -> index.size <= MAX_SIZE }
    .sumOf { it.size }

  fun part2(inputs: List<String>, root: Dir = Root): Long = Cursor(inputs, root)
    .root.flatDirs
    .filter { it.size > root.spaceAfterUpdate }
    .minBy { it.size }
    .size

  val testInput: List<String> = readLines("Day07_test")
  val input: List<String> = readLines("Day07")

  check(part1(testInput) == 95437L)
  println(part1(input))

  check(part2(testInput) == 24933642L)
  println(part2(input))
}
