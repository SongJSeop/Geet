package geet.manager

import java.io.File

class HeadManager {

    val headFile = File(".geet/HEAD")

    fun getHeadBranchName(): String {
        return headFile.readText().substring(16)
    }

    fun setHead(branchName: String): Unit {
        headFile.writeText("ref: refs/heads/${branchName}")
    }
}