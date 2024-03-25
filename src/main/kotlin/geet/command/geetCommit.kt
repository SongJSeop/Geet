package geet.command

import geet.enums.StageObjectStatus
import geet.exception.BadRequest
import geet.geetobject.GeetBlob
import geet.geetobject.GeetTree
import geet.util.const.indexManager
import geet.util.const.resetColor
import geet.util.const.yellow
import java.io.File

fun geetCommit(commandLines: Array<String>): Unit {
    if (commandLines.size != 3 || commandLines[1] != "-m") {
        throw BadRequest("commit 명령어에 대한 옵션이 올바르지 않습니다. ${yellow}'commit -m \"커밋메시지\"'${resetColor} 형식으로 입력해주세요.")
    }

    val commitTree = indexManager.getLastCommitTree()
    indexManager.getStageObjects(status = StageObjectStatus.NEW).forEach { newObject ->
        println("newObject: $newObject")
    }

    indexManager.getStageObjects(status = StageObjectStatus.MODIFIED).forEach { modifiedObject ->
        println("modifiedObject: $modifiedObject")
    }

    indexManager.getStageObjects(status = StageObjectStatus.DELETED).forEach { deletedObject ->
        println("deletedObject: $deletedObject")
    }
}