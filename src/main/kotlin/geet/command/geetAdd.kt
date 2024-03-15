package geet.command

import geet.enums.StageObjectStatus.*
import geet.exception.BadRequest
import geet.exception.NotFound
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
        ?: throw NotFound("파일이 존재하지 않습니다.: ${red}${filePath}${resetColor}")) {
        is GeetBlob -> indexManager.addToStage(objectInLastCommit, status = DELETED)
        is GeetTree -> objectInLastCommit.getAllBlobObjectsOfTree().forEach {
            indexManager.addToStage(it, status = DELETED)
        }
    }
    indexManager.writeIndex()
}

fun addFileToStage(file: File) {
    if (ignoreManager.isIgnored(file)) {
        return
    }

    val filePath = getRelativePathFromRoot(file)
    val samePathObjectInStage = indexManager.searchObjectFromStage(filePath)
    val samePathObjectInLastCommit = indexManager.searchObjectFromLastCommit(filePath)

    if (samePathObjectInStage == null) {
        if (samePathObjectInLastCommit == null) {
            val blob = GeetBlob(filePath = filePath, content = file.readText())
            objectManager.saveBlob(blob)
            indexManager.addToStage(blob, status = NEW)
        } else if (samePathObjectInLastCommit.content != file.readText()) {
            val blob = GeetBlob(filePath = filePath, content = file.readText())
            objectManager.saveBlob(blob)
            indexManager.addToStage(blob, status = MODIFIED)
        }
        return
    }

    if (samePathObjectInStage.blob.content != file.readText()) {
        indexManager.removeFromStage(samePathObjectInStage.blob.filePath)
        val blob = GeetBlob(filePath = filePath, content = file.readText())
        objectManager.saveBlob(blob)
        indexManager.addToStage(blob, status = samePathObjectInStage.status)
    }
}

fun addAllFilesInDirectory(directory: File) {
    if (ignoreManager.isIgnored(directory)) {
        return
    }

    indexManager.addDeletedFilesInDir(getRelativePathFromRoot(directory))
    directory.listFiles()?.forEach { file ->
        if (file.isDirectory) {
            addAllFilesInDirectory(file)
        } else {
            addFileToStage(file)
        }
    }
}