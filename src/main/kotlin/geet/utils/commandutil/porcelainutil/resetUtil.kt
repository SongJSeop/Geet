package geet.utils.commandutil.porcelainutil

import geet.exceptions.BadRequest
import geet.utils.GEET_OBJECTS_DIR_PATH
import java.io.File

fun changeToFullHash(commitString: String): String {
    if (startsWithHeadRef(commitString)) {
        var commitHash = getCurrentRefCommitHash()

        val carrotCount = countCarrot(commitString)
        for (i in 0 until carrotCount) {
            commitHash = getParentCommitFromCommitHash(commitHash)
        }

        return commitHash
    }

    if (isHash(commitString)) {
        val dirName = commitString.substring(0, 2)
        val dir = File("${GEET_OBJECTS_DIR_PATH}/$dirName")
        if (!dir.exists()) {
            throw BadRequest("올바르지 않은 커밋 해시 또는 참조입니다.")
        }

        dir.listFiles()?.forEach { file ->
            if (file.name.startsWith(commitString)) {
                return dirName + file.name
            }
        }
    }

    throw BadRequest("올바르지 않은 커밋 해시 또는 참조입니다.")
}

fun startsWithHeadRef(commitString: String): Boolean {
    val pattern = Regex("^HEAD\\^*$")
    return commitString.matches(pattern)
}

fun countCarrot(string: String): Int {
    val pattern = Regex("\\^")
    return pattern.findAll(string).count()
}

fun isHash(hash: String): Boolean {
    val pattern = Regex("^[0-9a-f]{4,40}$")
    return hash.matches(pattern)
}