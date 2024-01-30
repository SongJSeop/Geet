package geet.commands.porcelain

import geet.utils.commandutil.getGeetStatusResult
import geet.utils.getNotIgnoreFiles
import java.io.File

fun geetStatus(commandLines: Array<String>): Unit {
    val notIgnoreFiles = getNotIgnoreFiles(startDir = File("."))
    println(getGeetStatusResult(notIgnoreFiles = notIgnoreFiles))
}