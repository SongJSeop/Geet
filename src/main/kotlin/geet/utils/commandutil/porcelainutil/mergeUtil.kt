package geet.utils.commandutil.porcelainutil

import geet.commands.porcelain.GeetMergeOptions
import geet.exceptions.NotFound
import geet.objects.GeetObject
import geet.utils.GEET_OBJECTS_DIR_PATH
import geet.utils.GEET_REFS_HEADS_DIR_PATH
import geet.utils.decompressFromZlib
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

        val commitHistory = getCommitHistory(currentCommitHash)
        commitHistory.forEach { println(it) }

//        val currentObjects = getObjectsFromCommit(currentCommitHash)
//        val branchObjects = getObjectsFromCommit(branchCommitHash)
//
//        branchObjects.forEach { branchObject ->
//            val sameFileObject = currentObjects.find { it.path == branchObject.path }
//            if (sameFileObject != null && sameFileObject.hashString != branchObject.hashString) {
//
//            }
//
//            if (sameFileObject == null) {
//                currentObjects.add(branchObject)
//            }
//        }
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

fun getDiffObjects(oneCommitHash: String, otherCommitHash: String) {

}

fun getCommitHistory(commitHash: String): List<String> {
    val dirName = commitHash.substring(0, 2)
    val fileName = commitHash.substring(2)
    val commitFile = File("${GEET_OBJECTS_DIR_PATH}/${dirName}/${fileName}")
    if (!commitFile.exists()) {
        throw NotFound("존재하지 않는 커밋입니다.: ${commitHash}")
    }

    val contentSplit = decompressFromZlib(commitFile.readText()).split("\n")
    if (contentSplit[1].split(" ")[0] == "parent") {
        return getCommitHistory(contentSplit[1].split(" ")[1]) + listOf<String>(commitHash)
    }

    return listOf<String>(commitHash)
}

data class DiffObjects(
    val oneObjects: DiffObjectStatus,
    val otherObjects: DiffObjectStatus
)

data class DiffObjectStatus(
    val geetObject: GeetObject,
    val status: String
)