package geet.command

import geet.exception.BadRequest
import geet.geetobject.GeetObjectWithFile
import geet.util.const.indexManager
import geet.util.const.resetColor
import geet.util.const.yellow

fun geetCommit(commandLines: Array<String>): Unit {
    if (commandLines.size != 3 || commandLines[1] != "-m") {
        throw BadRequest("commit 명령어에 대한 옵션이 올바르지 않습니다. ${yellow}'commit -m \"커밋메시지\"'${resetColor} 형식으로 입력해주세요.")
    }

    val tree = mutableListOf<GeetObjectWithFile>()
    indexManager.getStageObjects().forEach {
        println("add to tree: $it")
    }
}