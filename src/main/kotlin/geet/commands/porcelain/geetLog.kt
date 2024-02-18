package geet.commands.porcelain

import geet.exceptions.BadRequest
import geet.utils.commandutil.porcelainutil.getCommitObjectData
import geet.utils.commandutil.porcelainutil.getCurrentRefCommitHash

fun geetLog(commandLines: Array<String>): Unit {
    if (commandLines.size != 1) {
        throw BadRequest("log 명령어는 다른 옵션을 받지 않습니다.")
    }

    var commitHash = getCurrentRefCommitHash()
    while (true) {
        val commitObjectData = getCommitObjectData(commitHash)
        println("commit ${commitHash}")
        println("Date: ${commitObjectData.datetime}")
        println()
        println("\t${commitObjectData.message}")
        println()
        commitHash = commitObjectData.parent ?: break
    }
}