import java.io.File
import java.math.BigInteger
import java.security.MessageDigest


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
