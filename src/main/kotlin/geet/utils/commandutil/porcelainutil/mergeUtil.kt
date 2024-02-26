package geet.utils.commandutil.porcelainutil

import geet.commands.porcelain.GeetMergeOptions
import geet.exceptions.NotFound
import geet.utils.GEET_REFS_HEADS_DIR_PATH
import java.io.File

fun merge(geetMergeOptions: GeetMergeOptions): Unit {
    if (geetMergeOptions.branchName != null) {
        val currentCommitHash = getCurrentRefCommitHash()
        val branchCommitHash = getBranchCommitHash(geetMergeOptions.branchName)
        println("병합 대상 브랜치의 커밋 해시: ${branchCommitHash}")
        println("현재 브랜치의 커밋 해시: ${currentCommitHash}")
    }

    when (geetMergeOptions.option) {
        "abort" -> println("병합을 중단합니다.")
        "continue" -> println("병합을 계속합니다.")
    }
}

fun getBranchCommitHash(branchName: String): String {
    val refFile = File("${GEET_REFS_HEADS_DIR_PATH}/${branchName}")
    if (!refFile.exists()) {
        throw NotFound("존재하지 않는 브랜치입니다.: ${branchName}")
    }

    return refFile.readText()
}