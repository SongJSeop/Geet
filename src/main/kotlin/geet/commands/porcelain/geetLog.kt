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
        println("-------------------------------------")
        val commitObjectData = getCommitObjectData(commitHash)
        println("\u001B[32mcommit \u001B[0m${commitHash}")
        if (commitObjectData.parent != null) println("\u001B[34mMerge To: \u001B[0m${commitObjectData.parent}")
        println("\u001B[33mDate: \u001B[0m${commitObjectData.datetime}")
        println()
        println("\t${commitObjectData.message}")
        commitHash = commitObjectData.parent ?: break
    }
}