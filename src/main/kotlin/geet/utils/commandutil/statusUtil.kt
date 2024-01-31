package geet.utils.commandutil

import geet.exceptions.BadRequest
import geet.objects.GeetBlob
import geet.utils.GeetObjectLoacation
import geet.utils.GeetObjectLoacation.*
import geet.utils.getNotIgnoreFiles
import geet.utils.getRelativePath
import geet.utils.indexManager
import java.io.File

data class StagingData(
    val stagedFiles: MutableList<String> = mutableListOf(),
    val unstagedFiles: MutableList<String> = mutableListOf(),
)

data class GeetStatusResult(
    val modifiedFiles: StagingData = StagingData(),
    val newFiles: StagingData = StagingData(),
    val removedFiles: StagingData = StagingData(),
    val untrackedFiles: MutableList<String> = mutableListOf(),
)

fun getGeetStatusResult(notIgnoreFiles: List<File>): GeetStatusResult {
    val geetStatusResult = GeetStatusResult()
    notIgnoreFiles.forEach { file ->
        val relativePath = getRelativePath(file.path)
        val blobObject = GeetBlob(path = relativePath, content = file.readText())

        if (!indexManager.isIn(where = LAST_COMMIT, blobObject)) {
            if (indexManager.isIn(where = STAGING_AREA, blobObject)) {
                geetStatusResult.newFiles.stagedFiles.add(relativePath)

                if (!indexManager.isSameWith(where = STAGING_AREA, blobObject)) {
                    geetStatusResult.newFiles.unstagedFiles.add(relativePath)
                }
            } else {
                geetStatusResult.untrackedFiles.add(relativePath)
            }
        } else {
            if (indexManager.isIn(where = STAGING_AREA, blobObject)) {
                geetStatusResult.modifiedFiles.stagedFiles.add(relativePath)

                if (!indexManager.isSameWith(where = STAGING_AREA, blobObject)) {
                    geetStatusResult.modifiedFiles.unstagedFiles.add(relativePath)
                }
            } else {
                geetStatusResult.modifiedFiles.unstagedFiles.add(relativePath)
            }
        }
    }

    val removedFiles = getRemovedFiles(notIgnoreFiles)
    removedFiles.forEach {
        val relativePath = getRelativePath(it.path)
        val blobObject = GeetBlob(path = relativePath, content = it.readText())

        if (indexManager.isIn(where = STAGING_AREA, blobObject)) {
            geetStatusResult.removedFiles.stagedFiles.add(relativePath)
        } else {
            geetStatusResult.removedFiles.unstagedFiles.add(relativePath)
        }
    }

    return geetStatusResult
}

fun getRemovedFiles(notIgnoreFiles: List<File>): MutableList<File> {
    val removedFiles = mutableListOf<File>()

    val notIgnoreFilesPath = notIgnoreFiles.map { getRelativePath(it.path) }
    val indexFileData = indexManager.getIndexFileData()
    indexFileData.lastCommitObjects.forEach {
        if (getRelativePath(it.path) !in notIgnoreFilesPath) {
            removedFiles.add(File(it.path))
        }
    }

    return removedFiles
}

fun printGeetStatus(geetStatusResult: GeetStatusResult) {
    println("-- 스테이지에 존재하는 변경 사항들 --")
    println("스테이지에 존재하는 변경 사항들은 커밋을 하려면 'geet commit -m \"메시지\"' 명령어를 사용하세요.")
    println("스테이지에 존재하는 변경 사항들은 스테이지에서 제거하려면 \"geet reset HEAD <file>\" 명령어를 사용하세요.\n")
    geetStatusResult.newFiles.stagedFiles.forEach { println("\t\u001B[32m새로 추가됨 : ${it}\u001B[0m") }
    geetStatusResult.modifiedFiles.stagedFiles.forEach { println("\t\u001B[32m수정됨: ${it}\u001B[0m") }
    geetStatusResult.removedFiles.stagedFiles.forEach { println("\t\u001B[32m삭제됨: ${it}\u001B[0m") }

    println("\n\n-- 스테이지에 존재하지 않는 변경 사항들 --")
    println("스테이지에 존재하지 않는 변경 사항들은 스테이지에 추가하려면 \"geet add <file>\" 명령어를 사용하세요.\n")
    geetStatusResult.newFiles.unstagedFiles.forEach { println("\t\u001B[33m새로 추가 후 다시 수정됨: ${it}\u001B[0m") }
    geetStatusResult.modifiedFiles.unstagedFiles.forEach { println("\t\u001B[33m수정됨: ${it}\u001B[0m") }
    geetStatusResult.removedFiles.unstagedFiles.forEach { println("\t\u001B[33m삭제됨: ${it}\u001B[0m") }

    println("\n\n-- 추적하지 않는 파일들 --")
    println("추적하지 않는 파일들은 스테이지에 추가하려면 \"geet add <file>\" 명령어를 사용하세요.\n")
    geetStatusResult.untrackedFiles.forEach { println("\t\u001B[31m${it}\u001B[0m") }
}