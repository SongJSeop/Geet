package geet.manager

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
            throw NotFound("이미 존재하는 브랜치입니다.: ${red}${branchName}${resetColor}")
        }


        if (branchName.contains("/")) {
            branchName.split("/").subList(0, branchName.split("/").size - 1).forEachIndexed { index, _ ->
                val dirName = branchName.split("/").subList(0, index + 1).joinToString("/")
                File(refsDir, "heads/${dirName}").mkdir()
            }
        }

        val headBranchName = headManager.getHeadBranchName()
        val headCommitHash = getBranchCommitHash(headBranchName)
        File(refsDir, "heads/$branchName").createNewFile()
        setBranchCommitHash(branchName, headCommitHash)
    }

    fun switchBranch(branchName: String) {
        if (!File(refsDir, "heads/$branchName").exists()) {
            throw NotFound("브랜치가 존재하지 않습니다.: ${red}${branchName}${resetColor}")
        }

        headManager.setHead(branchName)
    }

    fun deleteBranch(branchName: String) {
        val branchFile = File(refsDir, "heads/$branchName")
        if (!branchFile.exists()) {
            throw NotFound("이미 존재하지 않는 브랜치입니다.: ${red}${branchName}${resetColor}")
        }

        branchFile.delete()
    }
}