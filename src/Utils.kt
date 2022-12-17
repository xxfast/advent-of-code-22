import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.abs


/**
 * Reads lines from the given input txt file.
 */
fun readText(fileName: String) = File("src", fileName)
    .readText()

/**
 * Reads lines from the given input txt file.
 */
fun readLines(fileName: String) = File("src", fileName)
    .readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

val Long.factors: Set<Long>
    get() {
        val positives = (0..abs(this)).filter { it != 0L && abs(it) != 1L && this % it == 0L }.toSet()
        val negatives = positives.map { -it }.toSet().takeIf { this < 0 }.orEmpty()
        return negatives + positives
    }

val List<Long>.hcf: Long get() = map { number -> number.factors }.reduce { acc, longs -> acc intersect longs }.max()
