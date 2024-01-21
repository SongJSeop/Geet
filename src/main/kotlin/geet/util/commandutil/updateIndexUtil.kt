package geet.util.commandutil

import geet.commands.plumbing.GeetUpdateIndexOptions
import geet.exception.NotFoundException
import java.io.File


fun updateIndex(updateIndexOptions: GeetUpdateIndexOptions) {
    val file = File(updateIndexOptions.path)
    if (!file.exists()) {
        throw NotFoundException("파일을 찾을 수 없습니다. : ${updateIndexOptions.path}")
    }

    val indexFile = File(".geet/index")
    if (!indexFile.exists()) {
        indexFile.createNewFile()
    }

    when (updateIndexOptions.option) {
        "--add" -> addObjectToIndex(file)
        "--remove" -> removeObjectFromIndex(file)
        "--refresh" -> refreshIndex()
    }
}

fun addObjectToIndex(file: File) {}

fun removeObjectFromIndex(file: File) {}

fun refreshIndex() {}