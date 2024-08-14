package geet.util

import java.io.File

val ignoreFile
    get() = File(getGeetRootDir(), ".geetignore")
val ignoreSet = if (ignoreFile.exists()) {
    val ignoreSet = mutableSetOf(".geet")
    ignoreFile.readLines().toSet()
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .filter { !it.startsWith("#") }
        .forEach { ignoreSet.add(it) }
    ignoreSet
} else {
    setOf(".geet")
}

fun isIgnored(file: File): String? {
    val relativePath = getRelativePath(toFile = file)
    val fileName = file.name
    if (ignoreSet.any {
        it == fileName || it == relativePath
    }) return ignoreSet.first { it == fileName || it == relativePath }

    relativePath.split(File.separatorChar).forEach {
        if (ignoreSet.contains(it)) return it
    }

    return null
}