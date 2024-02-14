package geet.commands.porcelain

import geet.exceptions.BadRequest

data class GeetResetOptions(
    var option: String = "--mixed",
    var to: String = "HEAD"
)

fun geetReset(commandLines: Array<String>) {
    val geetResetOptions = getGeetResetOptions(commandLines)
    println(geetResetOptions)
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
        return GeetResetOptions(option = option, to = commandLines[2])
    }

    throw BadRequest("옵션이 올바르지 않습니다.")
}