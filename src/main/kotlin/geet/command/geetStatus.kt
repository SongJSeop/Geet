package geet.command

import geet.enums.StageObjectStatus
import geet.exception.BadRequest
import geet.geetobject.GeetBlob
import geet.util.const.*
import geet.util.getRelativePathFromRoot
import java.io.File

data class StatusObject(
    val newFiles: MutableList<String> = mutableListOf(),
    val modifiedFiles: MutableList<String> = mutableListOf(),
    val deletedFiles: MutableList<String> = mutableListOf()
)

data class StatusResult(
    val staged: StatusObject,
    val unstaged: StatusObject,
    val untracked: List<String>
)

fun geetStatus(commandLines: Array<String>): Unit {
    if (commandLines.size != 1) {
        throw BadRequest("status 명령어는 다른 옵션을 가지지 않습니다.: ${red}${commandLines.joinToString(" ")}${resetColor}")
    }

    val statusResult = getStatusResult()
    printStatusResult(statusResult)
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

fun getStatusResult(): StatusResult {
    val statusResult = StatusResult(
        staged = StatusObject(),
        unstaged = StatusObject(),
        untracked = getUntrackedFiles(File("."))
    )

    indexManager.indexData.stageObjects.forEach { stageObject ->
        val file = File(stageObject.blob.filePath)
        val blob = GeetBlob(content = file.readText(), filePath = getRelativePathFromRoot(file))

        when (stageObject.status) {
            StageObjectStatus.NEW -> {
                if (stageObject.blob.hash != blob.hash) {
                    statusResult.unstaged.newFiles.add(stageObject.blob.filePath)
                }

                statusResult.staged.newFiles.add(stageObject.blob.filePath)
            }
            StageObjectStatus.MODIFIED -> {
                if (stageObject.blob.hash != blob.hash) {
                    statusResult.unstaged.modifiedFiles.add(stageObject.blob.filePath)
                }

                statusResult.staged.modifiedFiles.add(stageObject.blob.filePath)
            }
            StageObjectStatus.DELETED -> statusResult.staged.deletedFiles.add(stageObject.blob.filePath)
            else -> return@forEach
        }
    }

    indexManager.indexData.lastCommitObjects.forEach { lastCommitObject ->
        val file = File(lastCommitObject.filePath)
        val objectInStage = indexManager.searchObjectFromStage(lastCommitObject.filePath)

        if (!file.exists() && objectInStage == null) {
            statusResult.unstaged.deletedFiles.add(lastCommitObject.filePath)
        }
    }

    return statusResult
}

fun printStatusResult(statusResult: StatusResult): Unit {
    println("** 스테이지된 변경사항 **")
    statusResult.staged.newFiles.forEach { println("${green}  새로운 파일: $it${resetColor}") }
    statusResult.staged.modifiedFiles.forEach { println("${green}  수정된 파일: $it${resetColor}") }
    statusResult.staged.deletedFiles.forEach { println("${green}  삭제된 파일: $it${resetColor}") }
    println()

    println("** 스테이지되지 않은 변경사항 **")
    statusResult.unstaged.newFiles.forEach { println("${yellow}  새로운 파일: $it${resetColor}") }
    statusResult.unstaged.modifiedFiles.forEach { println("${yellow}  수정된 파일: $it${resetColor}") }
    statusResult.unstaged.deletedFiles.forEach { println("${yellow}  삭제된 파일: $it${resetColor}") }
    println()

    println("** 추적되지 않는 파일 **")
    statusResult.untracked.forEach { println("${red}  $it${resetColor}") }
    println()
}