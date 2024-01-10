package geet.util

import geet.commands.plumbing.GeetHashObjectOptions
import geet.commands.plumbing.messageDigest
import java.io.File

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
        println("파일이 존재하지 않습니다.: ${options.path}")
    }

    val content = file.readText()
    val header = "${options.type} ${content.length}\u0000"
    val store = header + content

    val hash = messageDigest.digest(store.toByteArray())
    val hashString = hash.joinToString("") {
        String.format("%02x", it)
    }
    println(hashString)
}