package geet.manager

import geet.exception.BadRequest
import geet.exception.NotFound
import geet.util.const.headManager
import geet.util.const.red
import geet.util.const.resetColor
import java.io.File

class BranchManager {

    val refsDir = File(".geet/refs")

    fun getAllBranchNames(): List<String> {
        val branchDir = File(refsDir, "heads")

        return branchDir.walkTopDown()
            .filter { it.isFile }
            .map { it.relativeTo(branchDir).path }
            .toList()
    }

    fun getBranchCommitHash(branchName: String): String {
        val branchFile = File(refsDir, "heads/$branchName")
        if (!branchFile.exists()) {
            throw NotFound("브랜치가 존재하지 않습니다.: ${red}${branchName}${resetColor}")
        }

        return branchFile.readText()
    }

    fun setBranchCommitHash(branchName: String, commitHash: String) {
        val branchFile = File(refsDir, "heads/$branchName")
        if (!branchFile.exists()) {
            throw NotFound("브랜치가 존재하지 않습니다.: ${red}${branchName}${resetColor}")
        }

        branchFile.writeText(commitHash)
    }

    fun createBranch(branchName: String) {
        if (File(refsDir, "heads/$branchName").exists()) {
            throw BadRequest("브랜치가 이미 존재합니다.: ${red}${branchName}${resetColor}")
        }

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

    fun switchBranch(branchName: String) {
        if (!File(refsDir, "heads/$branchName").exists()) {
            throw NotFound("브랜치가 존재하지 않습니다.: ${red}${branchName}${resetColor}")
        }

        headManager.setHead(branchName)
    }
}