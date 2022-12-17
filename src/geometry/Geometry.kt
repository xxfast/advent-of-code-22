package geometry

import kotlin.math.abs
import kotlin.math.roundToLong
import kotlin.math.sqrt

typealias Point = Pair<Long, Long>
typealias Vector = Pair<Long, Long>
typealias Path = List<Point>

val Point.x: Long get() = first
val Point.y: Long get() = second

infix fun Point.towards(other: Point): Vector = (other.x - this.x) to (other.y - this.y)

fun Vector.normalised(): Vector {
  val length = sqrt((x * x + y * y).toFloat())
  return (x / length).roundToLong() to (y / length).roundToLong()
}

operator fun Vector.times(magnitude: Long): Vector = (x * magnitude) to (y * magnitude)
operator fun Point.plus(other: Point): Point = (this.x + other.x) to (this.y + other.y)

operator fun Point.rangeTo(other: Point): Path = buildList {
  add(this@rangeTo)
  val direction = this@rangeTo towards other
  val amount = maxOf(abs(direction.x), abs(direction.y))
  (0 until amount).forEach { step ->
    add(this@rangeTo + direction.normalised() * (step + 1))
  }
}

val Set<Point>.minX: Long get() = minOf { it.x }
val Set<Point>.maxX: Long get() = maxOf { it.x }
val Set<Point>.minY: Long get() = minOf { it.y }
val Set<Point>.maxY: Long get() = maxOf { it.y }