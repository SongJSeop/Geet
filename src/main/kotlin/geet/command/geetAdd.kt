package geet.command

import geet.exception.BadRequest
import geet.util.const.indexManager
import geet.util.const.resetColor
import geet.util.const.yellow
import geet.util.getRelativePathFromRoot
import java.io.File

fun geetAdd(commandLines: Array<String>): Unit {
    if (commandLines.size != 2) {
        throw BadRequest("add 명령어에 대한 옵션이 올바르지 않습니다. ${yellow}'add <file-path>'${resetColor} 형식으로 입력해주세요.")
    }

    val commandFile = File(commandLines[1])
    val filePath = getRelativePathFromRoot(commandFile)

    if (commandFile.exists()) {
        if (commandFile.isFile) {
            addFileToStage(commandFile)
        } else {
            addAllFilesInDirectory(commandFile)
        }
    }

    val objectInLastCommit = indexManager.searchObjectFromLastCommit(filePath)
        ?: throw BadRequest("파일이 존재하지 않습니다.: ${yellow}${filePath}${resetColor}")
    indexManager.addToStage(objectInLastCommit, deleted = true)
}

fun addFileToStage(file: File) {
    // TODO: 구현
}

fun addAllFilesInDirectory(directory: File) {
    // TODO: 구현
}