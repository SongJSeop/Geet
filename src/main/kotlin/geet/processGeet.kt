package geet

import geet.command.*
import geet.exception.BadRequest
import geet.util.const.red
import geet.util.const.resetColor

fun processGeet(commandLines: Array<String>): Unit {
    if (commandLines.isEmpty() || commandLines[0] == "help") {
        geetHelp()
        return
    }

    when (commandLines[0]) {
        "init" -> geetInit(commandLines)
        else -> throw BadRequest("지원하지 않는 명령어입니다.: ${red}${commandLines[0]}${resetColor}")
    }
}