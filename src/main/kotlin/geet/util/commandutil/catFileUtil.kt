package geet.util.commandutil

import geet.commands.plumbing.GeetCatFileOptions
import geet.exception.NotFoundException
import geet.util.decompressFromZlib
import java.io.File

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