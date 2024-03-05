package geet.command

import geet.exception.BadRequest
import geet.util.const.ignoreManager
import geet.util.const.indexManager
import geet.util.const.red
import geet.util.const.resetColor
import geet.util.getRelativePathFromRoot
import java.io.File

fun geetStatus(commandLines: Array<String>): Unit {
    if (commandLines.size != 1) {
        throw BadRequest("status 명령어는 다른 옵션을 가지지 않습니다.: ${red}${commandLines.joinToString(" ")}${resetColor}")
    }

    val untrackedFiles = getUntrackedFiles(File("."))
    println(untrackedFiles)
}

fun getUntrackedFiles(dir: File): List<String> {
    val untrackedFiles = mutableListOf<String>()
    dir.listFiles()?.forEach {
        if (ignoreManager.isIgnored(it)) {
            return@forEach
        }

        if (it.isDirectory) {
            untrackedFiles.addAll(getUntrackedFiles(it))
        } else {
            val filePath = getRelativePathFromRoot(it)
            val objectInStage = indexManager.searchObjectFromStage(filePath)
            val objectInLastCommit = indexManager.searchObjectFromLastCommit(filePath)

            if (objectInStage == null && objectInLastCommit == null) {
                untrackedFiles.add(filePath)
            }
        }
    }
    return untrackedFiles
}