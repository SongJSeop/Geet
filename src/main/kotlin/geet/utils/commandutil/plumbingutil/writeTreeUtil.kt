package geet.utils.commandutil.plumbingutil

import geet.managers.IndexData
import geet.objects.GeetObject
import geet.objects.GeetTree
import kotlinx.serialization.json.Json
import java.io.File

fun writeTree() {
    val indexFile = File(".geet/index")
    if (!indexFile.exists()) {
        println("index 파일이 존재하지 않습니다. 'update-index' 명령어를 통해 Staging Area에 파일을 추가하세요.")
        return
    }

    val indexFileData = Json.decodeFromString(IndexData.serializer(), indexFile.readText())
    if (indexFileData.stagingArea.isEmpty()) {
        println("Staging Area에 파일이 존재하지 않습니다. 'update-index' 명령어를 통해 Staging Area에 파일을 추가하세요.")
        return
    }

    val objects = mutableListOf<GeetObject>()
    indexFileData.stagingArea.forEach { objects.add(it.blobObject) }
    val treeObject = GeetTree(objects = objects)
    saveObjectInGeet(treeObject)
    println(treeObject.hashString)
}