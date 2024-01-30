package geet.utils.commandutil

import geet.objects.GeetBlob
import geet.utils.GeetObjectLoacation
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
        val blobObject = GeetBlob(path = file.path, content = file.readText())

        if (!indexManager.isIn(where = GeetObjectLoacation.LAST_COMMIT, blobObject)) {
            if (indexManager.isIn(where = GeetObjectLoacation.STAGING_AREA, blobObject)) {
                geetStatusResult.newFiles.stagedFiles.add(file.path)

                if (!indexManager.isSameWith(where = GeetObjectLoacation.STAGING_AREA, blobObject)) {
                    geetStatusResult.newFiles.unstagedFiles.add(file.path)
                }
            } else {
                geetStatusResult.untrackedFiles.add(file.path)
            }
        } else {
            if (indexManager.isIn(where = GeetObjectLoacation.STAGING_AREA, blobObject)) {
                if (!indexManager.isSameWith(where = GeetObjectLoacation.LAST_COMMIT, blobObject)) {
                    geetStatusResult.modifiedFiles.stagedFiles.add(file.path)

                    if (!indexManager.isSameWith(where = GeetObjectLoacation.STAGING_AREA, blobObject)) {
                        geetStatusResult.modifiedFiles.unstagedFiles.add(file.path)
                    }
                }
            } else {
                geetStatusResult.modifiedFiles.unstagedFiles.add(file.path)
            }
        }
    }

    return GeetStatusResult()
}