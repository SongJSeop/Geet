package geet.commands.porcelain

import geet.exceptions.BadRequest
import geet.utils.commandutil.porcelainutil.changeToFullHash
import geet.utils.commandutil.porcelainutil.getCurrentRefCommitHash
import geet.utils.commandutil.porcelainutil.reset

data class GeetResetOptions(
    var option: String = "--mixed",
    var commitHash: String = getCurrentRefCommitHash()
)

fun geetReset(commandLines: Array<String>): Unit {
    val geetResetOptions = getGeetResetOptions(commandLines)
    reset(geetResetOptions)
}

fun getGeetResetOptions(commandLines: Array<String>): GeetResetOptions {
    if (commandLines.size == 1) {
        return GeetResetOptions()
    }

    val option = commandLines[1]
    if (option != "--soft" && option != "--mixed" && option != "--hard") {
        throw BadRequest("옵션이 올바르지 않습니다.")
    }

    if (commandLines.size == 2) {
        return GeetResetOptions(option = option)
    }

    if (commandLines.size == 3) {
        val commitHash = changeToFullHash(commandLines[2])
        return GeetResetOptions(option = option, commitHash = commitHash)
    }

    throw BadRequest("옵션이 올바르지 않습니다.")
}