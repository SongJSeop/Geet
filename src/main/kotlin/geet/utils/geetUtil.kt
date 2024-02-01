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
    val typeLowerCase = type.uppercase()
    return GeetObjectType.values().any { it.name == typeLowerCase }
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
    val file = File("${GEET_OBJECTS_DIR_PATH}/$dirName/$fileName")
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
        val blobObject = GeetBlob(path = getRelativePath(file.path), content = file.readText())
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

    val treeObject = GeetTree(path = getRelativePath(file.path), objects = objects)
    saveObjectInGeet(treeObject)
    return treeObject
}

fun getIgnoreFiles(): List<String> {
    val ignoreFile = File(GEET_IGNORE_FILE_PATH)
    if (!ignoreFile.exists()) {
        return listOf(getRelativePath(GEET_DIR_PATH))
    }

    val ignoreFiles = ignoreFile.readText().trim().split("\n").map { getRelativePath(it).trim() }
    return ignoreFiles + listOf(getRelativePath(GEET_DIR_PATH))
}

fun getNotIgnoreFiles(startDir: File): List<File> {
    if (!startDir.isDirectory) {
        throw BadRequest("디렉토리가 아닙니다.")
    }

    val files = mutableListOf<File>()
    startDir.listFiles()?.forEach {
        if (getRelativePath(it.path) in getIgnoreFiles()) {
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

fun getRelativePath(path: String): String {
    val rootPath = File(".").canonicalPath
    val filePath = File(path).canonicalPath

    val rootTokens = rootPath.split(File.separator)
    val fileTokens = filePath.split(File.separator)

    val commonPrefixLength = rootTokens.zip(fileTokens).takeWhile { it.first == it.second }.count()

    val relativeTokens = List(rootTokens.size - commonPrefixLength) { ".." } +
            fileTokens.drop(commonPrefixLength)

    when (val relativePath = relativeTokens.joinToString(File.separator)) {
        "" -> return "."
        "." -> return "."
        else -> return relativePath
    }
}

fun getObjectContents(hashString: String): String {
    val dirName = hashString.substring(0, 2)
    val fileName = hashString.substring(2)
    val file = File("${GEET_OBJECTS_DIR_PATH}/$dirName/$fileName")
    if (!file.exists()) {
        throw BadRequest("해당 오브젝트가 존재하지 않습니다.")
    }

    return decompressFromZlib(file.readText())
}

fun getObjectsFromTree(treeHash: String): List<GeetObject> {
    val contents = getObjectContents(treeHash)
    val splitContents = contents.split("\n")

    val objects = mutableListOf<GeetObject>()
    splitContents.forEach { line ->
        if (line == "") {
            return@forEach
        }

        val splitLine = line.split(" ")
        val type = splitLine[0]
        val objectHash = splitLine[1]
        val path = splitLine[2]

        when (type) {
            "blob" -> {
                val blobObject = GeetBlob(path = path, content = getObjectContents(objectHash))
                objects.add(blobObject)
            }
            "tree" -> {
                val treeObject = GeetTree(path = path, objects = getObjectsFromTree(objectHash) as MutableList)
                objects.add(treeObject)
            }
        }
    }

    return objects
}