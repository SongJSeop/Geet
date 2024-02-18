package geet.utils.commandutil.porcelainutil

import geet.commands.porcelain.GeetBranchOptions
import geet.exceptions.BadRequest
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
    val file = File(".geet/refs/heads/${branchName}")
    if (file.exists()) {
        throw BadRequest("브랜치가 이미 존재합니다. : ${branchName}")
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