package geet.utils.commandutil.porcelainutil

import geet.commands.porcelain.GeetMergeOptions
import geet.exceptions.NotFound
import geet.utils.GEET_REFS_HEADS_DIR_PATH
import geet.utils.getObjectsFromCommit
import java.io.File

fun merge(geetMergeOptions: GeetMergeOptions): Unit {
    if (geetMergeOptions.branchName != null) {
        val currentCommitHash = getCurrentRefCommitHash()
        val branchCommitHash = getBranchCommitHash(geetMergeOptions.branchName)

        if (currentCommitHash == branchCommitHash) {
            println("현재 브랜치와 병합할 브랜치의 내용이 같습니다.")
            return
        }

        val currentObjects = getObjectsFromCommit(currentCommitHash)
        val branchObjects = getObjectsFromCommit(branchCommitHash)

        branchObjects.forEach { branchObject ->
            val sameFileObject = currentObjects.find { it.path == branchObject.path }
            if (sameFileObject != null && sameFileObject.hashString != branchObject.hashString) {
                println("내용이 수정된 파일, 병합 필요: ${branchObject.path}")
            }

            if (sameFileObject == null) {
                println("새로운 파일, 병합 필요: ${branchObject.path}")
            }
        }
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