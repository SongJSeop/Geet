package geet.commands.porcelain

import geet.exceptions.BadRequest
import geet.utils.commandutil.porcelainutil.switch

data class GeetSwitchOptions(
    val branchName: String,
    val create: Boolean,
)

fun geetSwitch(commandLines: Array<String>) {
    var geetSwitchOptions: GeetSwitchOptions
    if (commandLines.size == 2) {
        geetSwitchOptions = GeetSwitchOptions(commandLines[1], false)
    } else if (commandLines.size == 3 && commandLines[1] == "-c") {
        geetSwitchOptions = GeetSwitchOptions(commandLines[2], true)
    } else throw BadRequest("switch 명령어에 대한 옵션이 잘못되었습니다.: ${commandLines.joinToString(" ")}")

    switch(geetSwitchOptions)
}