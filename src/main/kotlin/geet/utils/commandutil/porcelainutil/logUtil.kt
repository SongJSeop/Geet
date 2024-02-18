package geet.utils.commandutil.porcelainutil

import geet.utils.GEET_OBJECTS_DIR_PATH
import geet.utils.decompressFromZlib
import java.io.File
import java.time.LocalDateTime

data class CommitObjectData(
    val tree: String,
    val parent: String?,
    val datetime: LocalDateTime,
    val message: String
)

fun getCommitObjectData(commitHash: String): CommitObjectData {
    val dirName = commitHash.substring(0, 2)
    val fileName = commitHash.substring(2)
    val file = File("${GEET_OBJECTS_DIR_PATH}/$dirName/$fileName")
    val content = decompressFromZlib(file.readText()).trim()

    val splitContent = content.split("\n")
    val tree = splitContent[0].split(" ")[1]
    var parent: String?
    var datetime: LocalDateTime
    var message: String

    if (splitContent[3] == "") {
        parent = splitContent[1].split(" ")[1]
        datetime = LocalDateTime.parse(splitContent[2].split(" ")[1])
        message = splitContent.subList(4, splitContent.size).joinToString("\n")
    } else {
        parent = null
        datetime = LocalDateTime.parse(splitContent[1].split(" ")[1])
        message = splitContent.subList(3, splitContent.size).joinToString("\n")
    }

    return CommitObjectData(tree, parent, datetime, message)
}