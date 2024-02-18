package geet.utils.commandutil.porcelainutil

import geet.commands.porcelain.GeetBranchOptions
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
    val file = File(".geet/refs/heads/$branchName")
    if (file.exists()) {
        println("브랜치가 이미 존재합니다. : $branchName")
        return
    }
    file.createNewFile()
    file.writeText(getCurrentRefCommitHash())
}

fun deleteBranch(branchName: String) {
    println("브랜치를 삭제했습니다. : $branchName")
}

fun showBranchList() {
    println("브랜치 목록을 출력했습니다.")
}