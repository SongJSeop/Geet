package geet.commands.porcelain

import geet.exceptions.BadRequest
import geet.utils.commandutil.porcelainutil.merge

data class GeetMergeOptions(
    val branchName: String? = null,
    val option: String? = null
)

fun geetMerge(commandLines: Array<String>): Unit {
    val geetMergeOptions = getGeetMergeOptions(commandLines)
    merge(geetMergeOptions)
}

fun getGeetMergeOptions(commandLines: Array<String>): GeetMergeOptions {
    if (commandLines.size == 1) {
        throw BadRequest("병합할 브랜치 이름을 함께 입력하세요.")
    }

    if (commandLines.size > 3) {
        throw BadRequest("옵션을 잘못 입력하셨습니다.")
    }

    return when (val command = commandLines[1]) {
        "--abort", "--continue" -> GeetMergeOptions(option = command.removePrefix("--"))
        else -> GeetMergeOptions(branchName = command)
    }
}