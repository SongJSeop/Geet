package geet.utils

import geet.exceptions.BadRequest
import geet.objects.GeetBlob
import geet.objects.GeetObject
import geet.objects.GeetTree
import geet.utils.commandutil.saveObjectInGeet
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Base64
import java.util.zip.Deflater
import java.util.zip.DeflaterOutputStream
import java.util.zip.Inflater
import java.util.zip.InflaterInputStream

fun isGeetObjectType(type: String): Boolean {
    val typeLowerCase = type.lowercase()
    return typeLowerCase == "blob" ||
        typeLowerCase == "tree" ||
        typeLowerCase == "commit" ||
        typeLowerCase == "tag"
}

fun compressToZlib(contents: String): String {
    val inputData = contents.toByteArray()
    val outputStream = ByteArrayOutputStream()
    val deflater = Deflater()
    val deflaterOutputStream = DeflaterOutputStream(outputStream, deflater)

    deflaterOutputStream.write(inputData)
    deflaterOutputStream.close()

    return Base64.getEncoder().encodeToString(outputStream.toByteArray())
}

fun decompressFromZlib(zlibContents: String): String {
    val decodedByteArray = Base64.getDecoder().decode(zlibContents)
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

fun findObject(type: String, sha1: String): Boolean {
    val dirName = sha1.substring(0, 2)
    val fileName = sha1.substring(2)
    val file = File(".geet/objects/$dirName/$fileName")
    if (!file.exists()) {
        return false
    }

    val decompressedContents = decompressFromZlib(file.readText())
    val header = "${type} ${decompressedContents.length}\u0000"
    val store = header + decompressedContents
    val hash = messageDigest.digest(store.toByteArray()).joinToString("") { String.format("%02x", it) }
    return hash == sha1
}

fun createGeetObjectWithFile(file: File): GeetObject {
    if (file.isFile) {
        val blobObject = GeetBlob(path = file.path, content = file.readText())
        saveObjectInGeet(blobObject)
        return blobObject
    }

    val objects = mutableListOf<GeetObject>()

    val files = file.listFiles()
    files?.forEach {
        if (it.path in getIgnoreFiles()) {
            return@forEach
        }

        objects.add(createGeetObjectWithFile(it))
    }

    val treeObject = GeetTree(path = file.path, objects = objects)
    saveObjectInGeet(treeObject)
    return treeObject
}

fun getIgnoreFiles(): List<String> {
    val ignoreFile = File(".geetignore")
    if (!ignoreFile.exists()) {
        return listOf("./.geet")
    }

    val ignoreFiles = ignoreFile.readText().split("\n").map { "./${it}" }
    return ignoreFiles + listOf("./.geet")
}

fun getNotIgnoreFiles(startDir: File): List<File> {
    if (!startDir.isDirectory) {
        throw BadRequest("디렉토리가 아닙니다.")
    }

    val files = mutableListOf<File>()
    startDir.listFiles()?.forEach {
        if (it.path in getIgnoreFiles()) {
            return@forEach
        }

        if (it.isFile) {
            files.add(it)
        } else if (it.isDirectory) {
            getNotIgnoreFiles(it).forEach { file ->
                files.add(file)
            }
        }
    }
    return files
}