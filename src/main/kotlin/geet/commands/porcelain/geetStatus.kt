package geet.commands.porcelain

import geet.utils.getNotIgnoreFiles
import java.io.File

data class GeetStatusResult(
    val modifiedFiles: MutableList<File> = mutableListOf(),
    val newFiles: MutableList<File> = mutableListOf(),
    val removedFiles: MutableList<File> = mutableListOf(),
    val untrackedFiles: MutableList<File> = mutableListOf(),
)

fun geetStatus(commandLines: Array<String>): Unit {
    val notIgnoreFiles = getNotIgnoreFiles(startDir = File("."))
    println(notIgnoreFiles)
}

fun getGeetStatusResult(notIgnoreFiles: List<File>): GeetStatusResult {
    return GeetStatusResult()
}