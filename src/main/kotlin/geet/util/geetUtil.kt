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
    var currentDir = File(".").absoluteFile

    while (true) {
        val geetDir = File(currentDir, ".geet")
        if (geetDir.exists()) {
            val geetObjectDir = File(geetDir, "objects")
            val geetRefsDir = File(geetDir, "refs")
            val geetHeadFile = File(geetDir, "HEAD")
            val geetConfigFile = File(geetDir, "config")
            val geetDescriptionFile = File(geetDir, "description")
            val geetHooksDir = File(geetDir, "hooks")
            val geetInfoDir = File(geetDir, "info")
            val geetLogsDir = File(geetDir, "logs")

            return geetObjectDir.exists() && geetRefsDir.exists() && geetHeadFile.exists() &&
                    geetConfigFile.exists() && geetDescriptionFile.exists() &&
                    geetHooksDir.exists() && geetInfoDir.exists() && geetLogsDir.exists()
        }

        val parentDir = currentDir.parentFile ?: return false
        if (parentDir == currentDir) return false // 루트 디렉토리에 도달
        currentDir = parentDir
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

fun getRelativePathFromRoot(file: File): String {
    val rootPath = File(".").canonicalFile
    val filePath = file.canonicalFile

    return try {
        filePath.relativeTo(rootPath).path
    } catch (e: IllegalArgumentException) {
        filePath.absolutePath
    }
}
