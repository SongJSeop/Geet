package geet.utils.commandutil.porcelainutil

import geet.commands.porcelain.GeetRestoreOptions
import geet.objects.GeetBlob
import geet.utils.getObjectsFromCommit
import geet.utils.getRelativePath
import geet.utils.indexManager
import java.io.File

fun isSourceOption(option: String): Boolean {
    val regex = Regex("^--source=HEAD~[0-9]+")
    if (!regex.matches(option)) {
        return false
    }
    return true
}

fun getSourceCommitHash(option: String): String {
    val index = option.indexOf("~")
    val carrotCount = option.substring(index + 1).toInt()
    var commitHash = getCurrentRefCommitHash()
    for (i in 0 until carrotCount) {
        commitHash = getParentCommitFromCommitHash(commitHash)
    }
    return commitHash
}

fun restore(geetRestoreOptions: GeetRestoreOptions) {
    if (geetRestoreOptions.worktree && geetRestoreOptions.staged) {
        restoreToWorktree(geetRestoreOptions.fileName)
        restoreStage(geetRestoreOptions.fileName)
    }

    if (geetRestoreOptions.worktree && !geetRestoreOptions.staged) {
        restoreToWorktree(geetRestoreOptions.fileName)
    }

    if (geetRestoreOptions.staged && !geetRestoreOptions.worktree) {
        restoreStage(geetRestoreOptions.fileName)
    }

    if (geetRestoreOptions.sourceCommitHash != null) {
        restoreToWorktree(geetRestoreOptions.fileName, geetRestoreOptions.sourceCommitHash!!)
    }
}

fun restoreToWorktree(fileName: String, commitHash: String = getCurrentRefCommitHash()) {
    getObjectsFromCommit(commitHash).forEach {
        if (getRelativePath(it.path) == getRelativePath(fileName)) {
            val file = File(it.path)
            file.writeText(it.content)
        }
    }
}

fun restoreStage(fileName: String) {
    indexManager.removeObjectFromStagingArea(GeetBlob(fileName, "removed"))
    indexManager.writeIndexFile()
}