package geet.commands.porcelain

import geet.exceptions.BadRequest
import geet.managers.StageObjectData
import geet.objects.GeetBlob
import geet.objects.GeetTree
import geet.utils.*
import geet.utils.ObjectStatus.*
import geet.utils.commandutil.porcelainutil.addRemovedFileDirectly
import geet.utils.commandutil.porcelainutil.getRemovedFiles
import java.io.File

fun geetAdd(commandLines: Array<String>): Unit {
    if (commandLines.size < 2) {
        throw BadRequest("파일 경로가 지정되지 않았습니다.")
    }

    if (commandLines.size > 2) {
        throw BadRequest("지원하지 않는 옵션이거나 중복된 파일 경로입니다. : ${commandLines[2]}")
    }

    val file = File(commandLines[1])
    if (!file.exists()) {
        if (addRemovedFileDirectly(file)) return

        throw BadRequest("존재하지 않는 파일입니다. : ${commandLines[1]}")
    }

    if (file.exists() && file.isDirectory) {
        indexManager.addRemovedFilesInStagingArea(getNotIgnoreFiles(file))
    }

    when (val geetObject = createGeetObjectWithFile(file)) {
        is GeetBlob -> indexManager.addBlobInStagingArea(geetObject)
        is GeetTree -> indexManager.addTreeInStagingArea(geetObject)
    }
    indexManager.writeIndexFile()
}