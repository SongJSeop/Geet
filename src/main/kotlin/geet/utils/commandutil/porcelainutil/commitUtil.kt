package geet.utils.commandutil.porcelainutil

import geet.exceptions.NotFound
import geet.utils.GEET_DIR_PATH
import geet.utils.GEET_HEAD_FILE_PATH
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
        refFile.createNewFile()
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