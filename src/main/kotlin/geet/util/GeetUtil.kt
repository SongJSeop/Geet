package geet.util

import geet.commands.plumbing.GeetCatFileOptions
import geet.commands.plumbing.GeetHashObjectOptions
import geet.commands.plumbing.GeetUpdateIndexOptions
import geet.exception.BadRequestException
import geet.exception.NotFoundException
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.util.Base64
import java.util.zip.Deflater
import java.util.zip.DeflaterOutputStream
import java.util.zip.Inflater
import java.util.zip.InflaterInputStream
import java.security.MessageDigest

val messageDigest: MessageDigest = MessageDigest.getInstance("SHA-1")

fun isGeetObjectType(type: String): Boolean {
    val typeLowerCase = type.lowercase()
    return typeLowerCase == "blob" ||
        typeLowerCase == "tree" ||
        typeLowerCase == "commit" ||
        typeLowerCase == "tag"
}

fun createHashObject(options: GeetHashObjectOptions) {
    val file = File(options.path)
    if (!file.exists()) {
        throw NotFoundException("파일을 찾을 수 없습니다. : ${options.path}")
    }

    val hashString = getHashString(options, file)
    val directoryName = hashString.substring(0, 2)
    val fileName = hashString.substring(2)
    val compressedContents = compressToZlib(file.readText())

    File(".geet/objects/$directoryName").mkdirs()
    File(".geet/objects/$directoryName/$fileName").writeText(compressedContents)

    println(hashString)
    println("개체가 저장되었습니다. : .geet/objects/$directoryName/$fileName")
}

fun getHashString(options: GeetHashObjectOptions, file: File): String {
    val content = file.readText()
    val header = "${options.type} ${content.length}\u0000"
    val store = header + content

    val hash = messageDigest.digest(store.toByteArray())
    return hash.joinToString("") {
        String.format("%02x", it)
    }
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
    val file = File(updateIndexOptions.path)
    if (!file.exists()) {
        throw NotFoundException("파일을 찾을 수 없습니다. : ${updateIndexOptions.path}")
    }

    val hashString = getHashString(GeetHashObjectOptions(path = updateIndexOptions.path), file)

    val indexFile = File(".geet/index")
    if (!indexFile.exists()) {
        indexFile.createNewFile()
        indexFile.writeText("${file.name} ${hashString}\n")
        return
    }
}