package geet.utils.commandutil.porcelainutil

import geet.utils.GEET_DIR_PATH
import geet.utils.GEET_HEAD_FILE_PATH
import geet.utils.compressToZlib
import java.io.File

fun getCurrentRef(): String {
    val headFile = File(GEET_HEAD_FILE_PATH)
    if (!headFile.exists()) {

    }

    return headFile.readText().trim().split(" ")[1]
}

fun editCurrentRefContent(content: String) {
    val refFile = File("${GEET_DIR_PATH}/${getCurrentRef()}")
    if (!refFile.exists()) {
        refFile.createNewFile()
    }

    refFile.writeText(content)
}