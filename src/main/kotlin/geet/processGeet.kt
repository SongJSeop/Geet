package geet

import geet.command.*
import geet.exception.BadRequest
import geet.util.const.red
import geet.util.const.resetColor
import geet.util.isGeetDirectory

fun processGeet(commandLines: Array<String>): Unit {
    if (commandLines.isEmpty() || commandLines[0] == "help") {
        geetHelp()
        return
    }

    if (commandLines[0] != "init" && !isGeetDirectory()) {
        throw BadRequest("현재 디렉토리는 Geet 저장소가 아닙니다.")
    }

    when (commandLines[0]) {
        "init" -> geetInit(commandLines)
        "status" -> geetStatus(commandLines)
        "add" -> geetAdd(commandLines)
        "branch" -> geetBranch(commandLines)
        else -> throw BadRequest("지원하지 않는 명령어입니다.: ${red}${commandLines[0]}${resetColor}")
    }
}