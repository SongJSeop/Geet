package geet.commands.plumbing

import geet.exceptions.BadRequest
import geet.utils.commandutil.plumbingutil.writeTree

fun geetWriteTree(commandLines: Array<String>): Unit {
    if (commandLines.size > 1) {
        throw BadRequest("write-tree 명령어는 다른 옵션을 지원하지 않습니다. : ${commandLines[1]}")
    }

    writeTree()
}