package geet.utils.commandutil.porcelainutil

import geet.exceptions.BadRequest
import geet.exceptions.NotFound
import geet.utils.GEET_OBJECTS_DIR_PATH
import geet.utils.decompressFromZlib
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

fun getParentCommitFromCommitHash(commitHash: String): String {
    val dirName = commitHash.substring(0, 2)
    val fileName = commitHash.substring(2)
    val commitFile = File("${GEET_OBJECTS_DIR_PATH}/${dirName}/${fileName}")
    if (!commitFile.exists()) {
        throw NotFound("커밋 파일을 찾을 수 없습니다.")
    }

    val commitContents = commitFile.readText()
    val commitContentsDecompressed = decompressFromZlib(commitContents)
    val commitContentsSplit = commitContentsDecompressed.split("\n")
    return commitContentsSplit[1].split(" ")[1]
}