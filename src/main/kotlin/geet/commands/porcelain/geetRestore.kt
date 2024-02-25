package geet.commands.porcelain

import geet.exceptions.BadRequest
import geet.utils.commandutil.porcelainutil.getSourceCommitHash
import geet.utils.commandutil.porcelainutil.isSourceOption
import geet.utils.commandutil.porcelainutil.restore

data class GeetRestoreOptions(
    var worktree: Boolean = true,
    var staged: Boolean = false,
    var sourceCommitHash: String? = null,
    val fileName: String,
)

fun geetRestore(commandLines: Array<String>): Unit {
    val geetRestoreOptions = getGeetRestoreOptions(commandLines)
    restore(geetRestoreOptions)
}

fun getGeetRestoreOptions(commandLines: Array<String>): GeetRestoreOptions {
    if (commandLines.size == 1) {
        throw BadRequest("파일 명이 입력되지 않았습니다.")
    }

    if (commandLines.size == 2) {
        return GeetRestoreOptions(fileName = commandLines[1])
    }

    if (commandLines.size == 3) {
        val fileName = commandLines[2]

        val option = commandLines[1]

        if (isSourceOption(option)) {
            return GeetRestoreOptions(worktree = false, sourceCommitHash = getSourceCommitHash(option), fileName = fileName)
        }

        when (option) {
            "--worktree" -> return GeetRestoreOptions(fileName = fileName)
            "--staged" -> return GeetRestoreOptions(worktree = false, staged = true, fileName = fileName)
            else -> throw BadRequest("옵션이 올바르지 않습니다.: ${commandLines[1]}")
        }
    }

    if (commandLines.size == 4) {
        if ((commandLines[1] == "--worktree" && commandLines[2] == "--staged") ||
            (commandLines[1] == "--staged" && commandLines[2] == "--worktree")) {
            return GeetRestoreOptions(worktree = true, staged = true, fileName = commandLines[3])
        }
    }

    throw BadRequest("옵션이 올바르지 않습니다.")
}