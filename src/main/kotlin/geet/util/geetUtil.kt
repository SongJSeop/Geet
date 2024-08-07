package geet.util

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import java.util.zip.Deflater
import java.util.zip.DeflaterOutputStream
import java.util.zip.Inflater
import java.util.zip.InflaterInputStream

fun getGeetRootDirectory(): File? {
    var currentDir = File(".").absoluteFile

    while (true) {
        val geetDir = File(currentDir, ".geet")
        if (geetDir.exists()) {
            return geetDir
        }

        currentDir = currentDir.parentFile ?: return null
    }
}

fun isGeetRepository(): Boolean {
    val geetRootDir = getGeetRootDirectory() ?: return false

    val geetObjectDir = File(geetRootDir, "objects")
    val geetRefsDir = File(geetRootDir, "refs")
    val geetHeadFile = File(geetRootDir, "HEAD")
    val geetConfigFile = File(geetRootDir, "config")
    val geetDescriptionFile = File(geetRootDir, "description")
    val geetHooksDir = File(geetRootDir, "hooks")
    val geetInfoDir = File(geetRootDir, "info")
    val geetLogsDir = File(geetRootDir, "logs")

    return geetObjectDir.exists() && geetRefsDir.exists() && geetHeadFile.exists() &&
            geetConfigFile.exists() && geetDescriptionFile.exists() &&
            geetHooksDir.exists() && geetInfoDir.exists() && geetLogsDir.exists()
}

fun getRelativePathFromRoot(file: File): String {
    val rootPath = getGeetRootDirectory()?.canonicalFile
    val filePath = file.canonicalFile

    return try {
        filePath.relativeTo(rootPath!!).path
    } catch (e: IllegalArgumentException) {
        filePath.absolutePath
    }
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
