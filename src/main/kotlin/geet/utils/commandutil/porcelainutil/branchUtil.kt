package geet.utils.commandutil.porcelainutil

import geet.commands.porcelain.GeetBranchOptions
import geet.exceptions.BadRequest
import geet.utils.GEET_REFS_HEADS_DIR_PATH
import java.io.File

fun branch(geetBranchOptions: GeetBranchOptions) {
    if (geetBranchOptions.delete) {
        deleteBranch(geetBranchOptions.branchName!!)
        return
    }

    if (geetBranchOptions.branchName != null) {
        createBranch(geetBranchOptions.branchName)
        return
    }

    showBranchList()
}

fun createBranch(branchName: String) {
    var file = File("${GEET_REFS_HEADS_DIR_PATH}/${branchName}")
    if (file.exists()) {
        throw BadRequest("브랜치가 이미 존재합니다. : ${branchName}")
    }

    if ("/" in branchName) {
        var branchDir = ""
        val splitBranchName = branchName.trim().split("/")
        splitBranchName.subList(0, splitBranchName.size - 1).forEach {
            branchDir += "/${it}"
            val dir = File("${GEET_REFS_HEADS_DIR_PATH}${branchDir}")
            if (!dir.exists()) {
                dir.mkdir()
            }
        }
        file = File("${GEET_REFS_HEADS_DIR_PATH}${branchDir}/${splitBranchName.last()}")
    }
    file.createNewFile()
    file.writeText(getCurrentRefCommitHash())
}

fun deleteBranch(branchName: String) {
    println("브랜치를 삭제했습니다. : ${branchName}")
}

fun showBranchList() {
    val headsDir = File(".geet/refs/heads")
    headsDir.listFiles()?.forEach { println(it.name) }
}