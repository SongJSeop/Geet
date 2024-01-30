package geet.utils.commandutil

import geet.commands.plumbing.GeetHashObjectOptions
import geet.exceptions.NotFound
import geet.objects.GeetBlob
import geet.objects.GeetObject
import geet.utils.compressToZlib
import geet.utils.getIgnoreFiles
import java.io.File

fun createHashObject(options: GeetHashObjectOptions) {
    val file = File(options.path)
    if (!file.exists()) {
        throw NotFound("파일을 찾을 수 없습니다. : ${options.path}")
    }

    when (options.type) {
        "blob" -> {
            val blobObject = GeetBlob(path = file.path, content = file.readText())
            println(blobObject.hashString)
            if (options.write) {
                saveObjectInGeet(blobObject)
                println("개체가 저장되었습니다.")
            }
        }
    }
}

fun saveObjectInGeet(geetObject: GeetObject) {
    if (geetObject.path in getIgnoreFiles()) {
        return
    }

    val dirName = geetObject.hashString.substring(0, 2)
    val fileName = geetObject.hashString.substring(2)
    val compressedContents = compressToZlib(geetObject.content)

    File(".geet/objects/$dirName").mkdirs()
    File(".geet/objects/$dirName/$fileName").writeText(compressedContents)
}