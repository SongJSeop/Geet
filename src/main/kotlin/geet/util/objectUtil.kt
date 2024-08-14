package geet.util

import geet.enums.GeetObjectType
import geet.util.const.messageDigest
import java.io.File

private val geetObjectRepo
    get() = File(getGeetRepoDir(), "objects")

fun getHash(type: GeetObjectType, content: String): String {
    val header = "${type.value} ${content.length}\u0000"
    val store = header + content

    val hash = messageDigest.digest(store.toByteArray())
    return hash.joinToString("") {
        String.format("%02x", it)
    }
}

fun saveObject(hash: String, content: String) {
    val objectDir = File(geetObjectRepo, hash.substring(0, 2))
    objectDir.mkdirs()

    val objectFile = File(objectDir, hash.substring(2))
    objectFile.writeText(content.toZlib())
}

fun isHash(hash: String): Boolean {
    return hash.length in 4..40 && hash.matches(Regex("[0-9a-fA-F]+"))
}

fun getFullHashIfObjectExists(shortHash: String): String? {
    if (shortHash.length == 40) {
        return if (File(File(geetObjectRepo, shortHash.substring(0, 2)), shortHash.substring(2)).exists()) {
            shortHash
        } else {
            null
        }
    }

    val objectDir = File(geetObjectRepo, shortHash.substring(0, 2))
    if (!objectDir.exists()) {
        return null
    }

    objectDir.listFiles()?.forEach { objectFile ->
        if (objectFile.name.startsWith(shortHash.substring(2))) {
            return objectFile.name
        }
    }

    return null
}

fun getObjectContent(hash: String): String? {
    val fullHash = getFullHashIfObjectExists(hash) ?: return null
    val objectFile = File(File(geetObjectRepo, fullHash.substring(0, 2)), fullHash.substring(2))
    return objectFile.readText().fromZlibToString()
}

fun getObjectType(hash: String): GeetObjectType? {
    val fullHash = if (hash.length == 40) hash else getFullHashIfObjectExists(hash) ?: return null
    val objectContent = getObjectContent(hash) ?: return null
    return GeetObjectType.entries.find { getHash(type = it, content = objectContent) == fullHash }
}
