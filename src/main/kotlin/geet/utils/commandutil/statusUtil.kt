package geet.utils.commandutil

import geet.objects.GeetBlob
import geet.utils.GeetObjectLoacation
import geet.utils.GeetObjectLoacation.*
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
                if (!indexManager.isSameWith(where = LAST_COMMIT, blobObject)) {
                    geetStatusResult.modifiedFiles.stagedFiles.add(relativePath)

                    if (!indexManager.isSameWith(where = STAGING_AREA, blobObject)) {
                        geetStatusResult.modifiedFiles.unstagedFiles.add(relativePath)
                    }
                }
            } else {
                geetStatusResult.modifiedFiles.unstagedFiles.add(relativePath)
            }
        }
    }

    return geetStatusResult
}