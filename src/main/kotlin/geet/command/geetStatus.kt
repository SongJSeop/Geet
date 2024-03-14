package geet.command

import geet.enums.StageObjectStatus.*
import geet.exception.BadRequest
import geet.util.const.indexManager
import geet.util.const.resetColor
import geet.util.const.yellow
import geet.util.getAllFilesInDir
import geet.util.getRelativePathFromRoot
import java.io.File

data class StatusResult(
    val stagedNewFiles: MutableSet<String> = mutableSetOf(),
    val stagedModifiedFiles: MutableSet<String> = mutableSetOf(),
    val stagedDeletedFiles: MutableSet<String> = mutableSetOf(),
    val unstagedModifiedFiles: MutableSet<String> = mutableSetOf(),
    val unstagedDeletedFiles: MutableSet<String> = mutableSetOf(),
    val untrackedFiles: MutableSet<String> = mutableSetOf(),
)

fun geetStatus(commandLines: Array<String>): Unit {
    if (commandLines.size != 1) {
        throw BadRequest("status 명령어에 대한 옵션이 올바르지 않습니다. ${yellow}'status'${resetColor} 명령어는 옵션을 필요로 하지 않습니다.")
    }
    val statusResult = StatusResult()

    val files = getAllFilesInDir(File("."))
    val deletedFileNames = indexManager.getDeletedObjects().map { it.filePath }
    files.forEach { file ->
        val filePath = getRelativePathFromRoot(file)
        val samePathObjectInStage = indexManager.searchObjectFromStage(filePath)
        val samePathObjectInLastCommit = indexManager.searchObjectFromLastCommit(filePath)

        if (samePathObjectInStage == null) {
            if (samePathObjectInLastCommit == null) {
                statusResult.untrackedFiles.add(filePath)
                return@forEach
            }

            if (samePathObjectInLastCommit.content != file.readText()) {
                statusResult.unstagedModifiedFiles.add(filePath)
            }

            if (filePath in deletedFileNames) {
                statusResult.unstagedDeletedFiles.add(filePath)
            }
            return@forEach
        }

        when (samePathObjectInStage.status) {
            NEW -> statusResult.stagedNewFiles.add(filePath)
            MODIFIED -> statusResult.stagedModifiedFiles.add(filePath)
            DELETED -> statusResult.stagedDeletedFiles.add(filePath)
        }
    }
}