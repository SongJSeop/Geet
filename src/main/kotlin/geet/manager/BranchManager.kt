package geet.manager

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
}