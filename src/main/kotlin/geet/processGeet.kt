package geet

import geet.command.porcelain.*
import geet.command.plumbing.*
import geet.exception.BadRequest
import geet.util.const.*
import geet.util.isGeetRepo

fun processGeet(commandLines: Array<String>): Unit {
    if (commandLines.isEmpty() || commandLines[0] == "help") {
        geetHelp()
        return
    }

    if (commandLines[0] != "init" && !isGeetRepo()) {
        throw BadRequest("현재 디렉토리는 Geet 저장소가 아닙니다.")
    }

    when (commandLines[0]) {
        "init" -> geetInit(commandLines)

        "hash-object" -> geetHashObject(commandLines)

        else -> throw BadRequest("지원하지 않는 명령어입니다.: ${weekRed}${commandLines[0]}${resetColor}")
    }
}