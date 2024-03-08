package geet.command

import geet.exception.BadRequest
import geet.geetobject.GeetBlob
import geet.geetobject.GeetTree
import geet.util.const.*
import geet.util.getRelativePathFromRoot
import java.io.File

fun geetAdd(commandLines: Array<String>): Unit {
    if (commandLines.size != 2) {
        throw BadRequest("add 명령어에 대한 옵션이 올바르지 않습니다. ${yellow}'add <file-path>'${resetColor} 형식으로 입력해주세요.")
    }

    val commandFile = File(commandLines[1])
    val filePath = getRelativePathFromRoot(commandFile)

    if (commandFile.exists()) {
        if (ignoreManager.isIgnored(commandFile)) {
            throw BadRequest(".geetignore에 의해 무시되는 파일입니다.: ${red}${filePath}${resetColor}")
        }

        if (commandFile.isFile) {
            addFileToStage(commandFile)
        } else {
            addAllFilesInDirectory(commandFile)
        }

        indexManager.writeIndex()
        return
    }

    when (val objectInLastCommit = indexManager.searchObjectFromLastCommit(filePath)
        ?: throw BadRequest("파일이 존재하지 않습니다.: ${red}${filePath}${resetColor}")) {
        is GeetBlob -> indexManager.addToStage(objectInLastCommit, deleted = true)
        is GeetTree -> objectInLastCommit.getAllBlobObjectsOfTree().forEach {
            indexManager.addToStage(it, deleted = true)
        }
    }
    indexManager.writeIndex()
}

fun addFileToStage(file: File) {
    if (ignoreManager.isIgnored(file)) {
        return
    }

    val filePath = getRelativePathFromRoot(file)
    val blob = objectManager.saveBlob(file)

    val samePathObjectInStage = indexManager.searchObjectFromStage(filePath)
    if (samePathObjectInStage != null) {
        indexManager.removeFromStage(filePath)
    }

    indexManager.addToStage(blob)
}

fun addAllFilesInDirectory(directory: File) {
    if (ignoreManager.isIgnored(directory)) {
        return
    }

    directory.listFiles()?.forEach { file ->
        if (file.isDirectory) {
            addAllFilesInDirectory(file)
        } else {
            addFileToStage(file)
        }
    }
}