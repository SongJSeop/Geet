package geet.utils.commandutil.porcelainutil

import geet.commands.porcelain.GeetBranchOptions

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
    println("브랜치를 생성했습니다. : $branchName")
}

fun deleteBranch(branchName: String) {
    println("브랜치를 삭제했습니다. : $branchName")
}

fun showBranchList() {
    println("브랜치 목록을 출력했습니다.")
}