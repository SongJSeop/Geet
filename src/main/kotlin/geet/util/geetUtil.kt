package geet.util

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import java.util.zip.Deflater
import java.util.zip.DeflaterOutputStream
import java.util.zip.Inflater
import java.util.zip.InflaterInputStream

fun isGeetDirectory(): Boolean {
    val geetDir = File(".geet")
    val geetObjectDir = File(geetDir, "objects")
    val geetRefsDir = File(geetDir, "refs")
    val geetHeadFile = File(geetDir, "HEAD")
    return geetDir.exists() && geetObjectDir.exists() && geetRefsDir.exists() && geetHeadFile.exists()
}

fun String.toZlib(): String {
    val inputData = this.toByteArray()
    val outputStream = ByteArrayOutputStream()
    val deflater = Deflater()
    val deflaterOutputStream = DeflaterOutputStream(outputStream, deflater)

    deflaterOutputStream.write(inputData)
    deflaterOutputStream.close()

    return Base64.getEncoder().encodeToString(outputStream.toByteArray())
}

fun String.fromZlibToString(): String {
    val decodedByteArray = Base64.getDecoder().decode(this)
    val inputStream = ByteArrayInputStream(decodedByteArray)
    val inflater = Inflater()
    val inflaterInputStream = InflaterInputStream(inputStream, inflater)
    val outputStream = ByteArrayOutputStream()

    val buffer = ByteArray(1024)
    var length: Int
    while (inflaterInputStream.read(buffer).also { length = it } > 0) {
        outputStream.write(buffer, 0, length)
    }

    return outputStream.toString()
}

fun getRelativePathFromRoot(file: File): String {
    val rootPath = File(".").canonicalPath
    val filePath = file.canonicalPath

    val relativePath = filePath.removePrefix(rootPath).trimStart(File.separatorChar)

    return if (relativePath.isEmpty()) "." else relativePath
}