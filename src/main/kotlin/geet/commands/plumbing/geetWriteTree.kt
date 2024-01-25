package geet.commands.plumbing

import geet.objects.GeetTree
import geet.util.commandutil.IndexFileData
import geet.util.commandutil.saveObjectInGeet
import kotlinx.serialization.json.Json
import java.io.File

fun geetWriteTree(commandLines: Array<String>): Unit {
    val indexFile = File(".geet/index")
    if (!indexFile.exists()) {
        println("index 파일이 존재하지 않습니다. 'update-index' 명령어를 통해 Staging Area에 파일을 추가하세요.")
        return
    }

    val indexFileData = Json.decodeFromString(IndexFileData.serializer(), indexFile.readText())
    if (indexFileData.stagingArea.isEmpty()) {
        println("Staging Area에 파일이 존재하지 않습니다. 'update-index' 명령어를 통해 Staging Area에 파일을 추가하세요.")
        return
    }

    val treeObject = GeetTree(objects = indexFileData.stagingArea)
    saveObjectInGeet(treeObject)
    println(treeObject.hashString)
}