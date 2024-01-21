package geet.util

import geet.commands.plumbing.GeetCatFileOptions
import geet.commands.plumbing.GeetUpdateIndexOptions
import geet.exception.NotFoundException
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

fun catGeetObject(catFileOptions: GeetCatFileOptions) {
    val dirPath = ".geet/objects/${catFileOptions.objectPath.substring(0, 2)}"
    val fileName = catFileOptions.objectPath.substring(2)

    val file = File("$dirPath/$fileName")
    if (!file.exists()) {
        throw NotFoundException("개체를 찾을 수 없습니다. : ${catFileOptions.objectPath}")
    }

    if (catFileOptions.option == "-p") {
        print(decompressFromZlib(file.readText()))
    }
}

fun updateIndex(updateIndexOptions: GeetUpdateIndexOptions) {
//    val file = File(updateIndexOptions.path)
//    if (!file.exists()) {
//        throw NotFoundException("파일을 찾을 수 없습니다. : ${updateIndexOptions.path}")
//    }
//
//    val hashString = getHashString(GeetHashObjectOptions(path = updateIndexOptions.path), file)
//
//    val indexFile = File(".geet/index")
//    if (!indexFile.exists()) {
//        writeNewIndexFile(indexFile, file, hashString)
//        return
//    }
}

fun writeNewIndexFile(indexFile: File, file: File, hashString: String) {
    indexFile.createNewFile()
    indexFile.writeText("[STAGE]\n")
    indexFile.writeText("${file.name} ${hashString}\n")
    indexFile.writeText("\n")
    indexFile.writeText("[ADDED]\n")
    indexFile.writeText("${file.name} ${hashString}\n")
    indexFile.writeText("[MODIFIED]\n")
    indexFile.writeText("\n")
    indexFile.writeText("[DELETED]\n")
}