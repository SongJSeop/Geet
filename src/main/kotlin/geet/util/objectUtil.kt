package geet.util

import geet.enums.GeetObjectType
import geet.util.const.messageDigest
import java.io.File

val objectFile = File(getGeetRepoDir(), "objects")

fun createHash(type: GeetObjectType, content: String): String {
    val header = "${type.value} ${content.length}\u0000"
    val store = header + content

    val hash = messageDigest.digest(store.toByteArray())
    return hash.joinToString("") {
        String.format("%02x", it)
    }
}

fun saveObject(hash: String, content: String) {
    val objectDir = File(objectFile, hash.substring(0, 2))
    objectDir.mkdirs()

    val objectFile = File(objectDir, hash.substring(2))
    objectFile.writeText(content.toZlib())
}

fun isHash(hash: String): Boolean {
    return hash.length in 4..40 && hash.matches(Regex("[0-9a-fA-F]+"))
}