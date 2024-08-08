package geet.util

import java.io.File

val objectFile = File(getGeetRepoDir(), "objects")

fun saveObject(hash: String, content: String) {
    val objectDir = File(objectFile, hash.substring(0, 2))
    objectDir.mkdirs()

    val objectFile = File(objectDir, hash.substring(2))
    objectFile.writeText(content.toZlib())
}