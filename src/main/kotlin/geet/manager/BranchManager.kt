package geet.manager

import geet.util.const.headManager
import java.io.File

class BranchManager {

    val refsDir = File(".geet/refs")

    fun getBranchCommitHash(branchName: String): String {
        val branchFile = File(refsDir, "heads/$branchName")
        return branchFile.readText()
    }

    fun setBranchCommitHash(branchName: String, commitHash: String) {
        val branchFile = File(refsDir, "heads/$branchName")
        branchFile.writeText(commitHash)
    }

    fun createBranch(branchName: String) {
        if (branchName.contains("/")) {
            val branchNameList = branchName.split("/")
            val branchDir = File(refsDir, "heads/${branchNameList[0]}")
            branchDir.mkdirs()
            createBranch(branchNameList.subList(1, branchNameList.size).joinToString("/"))
        } else {
            val branchFile = File(refsDir, "heads/$branchName")
            val headBranchName = headManager.getHeadBranchName()
            val headCommitHash = getBranchCommitHash(headBranchName)
            branchFile.writeText(headCommitHash)
        }
    }
}