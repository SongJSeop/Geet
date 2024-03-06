package geet.command

import geet.enums.StageObjectStatus.*
import geet.exception.BadRequest
import geet.util.const.*

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

    val staged = getStatusResult()
}

fun getStatusResult(): StatusObject {
    val staged = StatusObject()

    val stageObjects = indexManager.getStageObjects()
    stageObjects.forEach { stageObject ->
        when (stageObject.status) {
            NEW -> staged.newFiles.add(stageObject.blob.filePath)
            MODIFIED -> staged.modifiedFiles.add(stageObject.blob.filePath)
            DELETED -> staged.deletedFiles.add(stageObject.blob.filePath)
            else -> return@forEach
        }
    }

    return staged
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