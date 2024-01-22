package geet.util.commandutil

import geet.commands.plumbing.GeetHashObjectOptions
import geet.exception.NotFoundException
import geet.objects.GeetBlob
import geet.util.compressToZlib
import java.io.File

fun createHashObject(options: GeetHashObjectOptions) {
    val file = File(options.path)
    if (!file.exists()) {
        throw NotFoundException("파일을 찾을 수 없습니다. : ${options.path}")
    }

    when (options.type) {
        "blob" -> {
            val blobObject = GeetBlob(name = file.name, content = file.readText())
            createBlobObject(options.write, blobObject)
        }
    }
}

fun createBlobObject(write: Boolean, blobObject: GeetBlob) {
    println(blobObject.hashString)

    if (write) {
        val dirName = blobObject.hashString.substring(0, 2)
        val fileName = blobObject.hashString.substring(2)
        val compressedContents = compressToZlib(blobObject.content)

        File(".geet/objects/$dirName").mkdirs()
        File(".geet/objects/$dirName/$fileName").writeText(compressedContents)
        println("개체가 저장되었습니다.")
    }
}