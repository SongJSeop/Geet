package geet.util

import java.io.File

val ignoreFile = File(getGeetRepoDir(), ".geetignore")
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

fun isIgnored(file: File): Boolean {
    val relativePath = getRelativePathFromRoot(file)
    val fileName = file.name
    return ignoreSet.any {
        it == fileName || it == relativePath
    }
}