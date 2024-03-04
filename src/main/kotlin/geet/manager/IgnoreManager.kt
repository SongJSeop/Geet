package geet.manager

import geet.util.getRelativePathFromRoot
import java.io.File

class IgnoreManager {

    val ignoreFile = File(".geetignore")
    val ignoreSet: Set<String>
        get() = if (ignoreFile.exists()) {
            val ignoreSet = mutableSetOf<String>()
            ignoreFile.readLines().forEach {
                if (it.isNotBlank() && !it.startsWith("#")) {
                    ignoreSet.add(getRelativePathFromRoot(File(it)))
                }
            }
            ignoreSet.add(".geet")
            ignoreSet
        } else {
            setOf(".geet")
        }

    fun isIgnored(file: File): Boolean {
        val relativePath = getRelativePathFromRoot(file)
        return ignoreSet.any { it == relativePath }
    }
}