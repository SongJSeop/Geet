package geet.commands.porcelain

import geet.exceptions.BadRequest

data class GeetBranchOptions(
    val branchName: String? = null,
    val delete: Boolean = false
)

fun geetBranch(commandLines: Array<String>) {
    val geetBranchOptions = getGeetBranchOptions(commandLines)
    println(geetBranchOptions)
}

fun getGeetBranchOptions(commandLines: Array<String>): GeetBranchOptions {
    if (commandLines.size == 1) {
        return GeetBranchOptions()
    }

    if (commandLines.size == 2) {
        return GeetBranchOptions(branchName = commandLines[1])
    }

    if (commandLines.size == 3 && commandLines[1] == "-d") {
        return GeetBranchOptions(branchName = commandLines[2], delete = true)
    }

    throw BadRequest("branch 명령어에 대하여 올바르지 않은 옵션입니다. : ${commandLines.joinToString(" ")}")
}