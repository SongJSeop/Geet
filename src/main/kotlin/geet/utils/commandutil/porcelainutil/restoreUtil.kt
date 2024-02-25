package geet.utils.commandutil.porcelainutil

import geet.commands.porcelain.GeetRestoreOptions

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
    if (geetRestoreOptions.worktree) {
        restoreToWorktree(geetRestoreOptions.fileName)
    }

    if (geetRestoreOptions.staged) {
        restoreStage(geetRestoreOptions.fileName)
    }

    if (geetRestoreOptions.sourceCommitHash != null) {
        restoreToWorktree(geetRestoreOptions.fileName, geetRestoreOptions.sourceCommitHash!!)
    }
}

fun restoreToWorktree(fileName: String, commitHash: String = getCurrentRefCommitHash()) {

}

fun restoreStage(fileName: String) {

}