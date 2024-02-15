package geet.utils.commandutil.porcelainutil

import geet.exceptions.NotFound
import geet.utils.GEET_DIR_PATH
import geet.utils.GEET_HEAD_FILE_PATH
import geet.utils.GEET_OBJECTS_DIR_PATH
import geet.utils.compressToZlib
import java.io.File

fun getCurrentRef(): String {
    val headFile = File(GEET_HEAD_FILE_PATH)
    if (!headFile.exists()) {
        throw NotFound("HEAD 파일이 존재하지 않습니다.\n저장소가 초기화가 되었는지 확인해 주세요.")
    }

    return headFile.readText().trim().split(" ")[1]
}

fun getCurrentRefCommitHash(): String {
    val refFile = File("${GEET_DIR_PATH}/${getCurrentRef()}")
    if (!refFile.exists()) {
        throw NotFound("참조 파일을 찾을 수 없습니다.")
    }

    return refFile.readText().trim()
}

fun editCurrentRefContent(content: String) {
    val refFile = File("${GEET_DIR_PATH}/${getCurrentRef()}")
    if (!refFile.exists()) {
        refFile.createNewFile()
    }

    refFile.writeText(content)
}

fun getParentCommitFromCommitHash(commitHash: String): String {
    val dirName = commitHash.substring(0, 2)
    val fileName = commitHash.substring(2)
    val commitFile = File("${GEET_OBJECTS_DIR_PATH}/${dirName}/${fileName}")
    if (!commitFile.exists()) {
        throw NotFound("커밋 파일을 찾을 수 없습니다.")
    }

    val commitContents = commitFile.readText()
    val commitContentsDecompressed = compressToZlib(commitContents)
    val commitContentsSplit = commitContentsDecompressed.split("\n")
    return commitContentsSplit[1].split(" ")[1]
}