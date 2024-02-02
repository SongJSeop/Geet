package geet.commands.porcelain

import geet.exceptions.BadRequest
import geet.objects.GeetCommit
import geet.objects.GeetTree
import geet.utils.commandutil.plumbingutil.saveObjectInGeet
import geet.utils.indexManager

fun geetCommit(commandLines: Array<String>): Unit {
    if (commandLines.size != 3 && commandLines[1] != "-m") {
        throw BadRequest("\'commit -m \"커밋 메시지\"\'와 같은 형식으로 입력해주세요.")
    }

    val indexData = indexManager.getIndexFileData()

    val stageObjects = indexData.stagingArea.map { it.blobObject }
    val treeObject = GeetTree(objects = stageObjects as MutableList)
    saveObjectInGeet(treeObject)

    val commitObject = GeetCommit(
        tree = treeObject.hashString,
        parent = null,
        message = commandLines[2],
    )
    saveObjectInGeet(commitObject)

     indexData.lastCommitTreeHash = treeObject.hashString
    indexData.stagingArea.clear()
    indexManager.writeIndexFile()
}