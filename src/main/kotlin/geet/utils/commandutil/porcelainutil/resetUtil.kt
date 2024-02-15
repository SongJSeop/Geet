package geet.utils.commandutil.porcelainutil

import geet.commands.porcelain.GeetResetOptions
import geet.exceptions.BadRequest
import geet.exceptions.NotFound
import geet.utils.GEET_OBJECTS_DIR_PATH
import geet.utils.decompressFromZlib
import java.io.File

fun reset(geetResetOptions: GeetResetOptions) {
    when (geetResetOptions.option) {
        "--soft" -> softReset(geetResetOptions.commitHash)
        "--mixed" -> mixedReset(geetResetOptions.commitHash)
        "--hard" -> hardReset(geetResetOptions.commitHash)
    }
}

fun softReset(commitHash: String) {
    println("soft Reset")
}

fun mixedReset(commitHash: String) {
    println("mixed Reset")
}

fun hardReset(commitHash: String) {
    println("hard Reset")
}

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
    if (commitContentsSplit[1].split(" ")[0] == "parent") {
        return commitContentsSplit[1].split(" ")[1]
    }

    throw BadRequest("부모 커밋을 찾을 수 없습니다.")
}