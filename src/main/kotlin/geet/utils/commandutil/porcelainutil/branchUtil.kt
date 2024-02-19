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
    val file = File("${GEET_REFS_HEADS_DIR_PATH}/${branchName}")
    if (!file.exists()) {
        throw BadRequest("브랜치가 존재하지 않습니다. : ${branchName}")
    }
    file.delete()
}

fun showBranchList() {
    val headsDir = File(".geet/refs/heads")
    val currentBranchSplit = getCurrentRef().split("/")
    val currentBranchName = currentBranchSplit.subList(2, currentBranchSplit.size).joinToString("/")
    headsDir.listFiles()?.forEach { headFile ->
        getBranchNames(headFile).forEach { branchName ->
            if (branchName == currentBranchName) {
                println("\u001B[33m${branchName} *\u001B[0m")
            } else {
                println(branchName)
            }
        }
    }
}

fun getBranchNames(file: File): List<String> {
    val branchNames = mutableListOf<String>()
    if (file.isDirectory) {
        file.listFiles()?.forEach {
            branchNames.addAll(getBranchNames(it).map { branchName -> "${file.name}/${branchName}" })
        }
    }

    if (!file.isDirectory) {
        branchNames.add(file.name)
    }

    return branchNames
}