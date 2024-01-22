package geet.util.commandutil

import geet.commands.plumbing.GeetUpdateIndexOptions
import geet.exception.NotFoundException
import geet.objects.GeetBlob
import geet.objects.GeetObject
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class IndexFileData(
    val stagingArea: MutableList<GeetObject>,
    val lastCommitObjects: MutableList<GeetObject>,
    val modifiedObjects: MutableList<GeetObject>,
    val addedObjects: MutableList<GeetObject>,
    val removedObjects: MutableList<GeetObject>,
)

fun updateIndex(updateIndexOptions: GeetUpdateIndexOptions) {
    val file = File(updateIndexOptions.path)
    if (!file.exists()) {
        throw NotFoundException("파일을 찾을 수 없습니다. : ${updateIndexOptions.path}")
    }

    if (file.isDirectory) {
        throw NotFoundException("update-index 명령어는 디렉토리를 지원하지 않습니다. : ${updateIndexOptions.path}")
    }

    val indexFile = File(".geet/index")
    if (!indexFile.exists()) {
        createNewIndexFile(indexFile, file)
        return
    }

    when (updateIndexOptions.option) {
        "--add" -> {
            addObjectToIndex(indexFile, file)
            println("개체를 Staging Area에 추가했습니다.")
        }
        "--remove" -> removeObjectFromIndex(file)
        "--refresh" -> refreshIndex()
    }
}

fun createNewIndexFile(indexFile: File, file: File) {
    val blobObject = GeetBlob(name = file.name, content = file.readText())
    saveObjectInGeet(blobObject)
    val indexFileData = IndexFileData(
        stagingArea = mutableListOf(blobObject),
        lastCommitObjects = mutableListOf(),
        modifiedObjects = mutableListOf(),
        addedObjects = mutableListOf(blobObject),
        removedObjects = mutableListOf(),
    )
    indexFile.writeText(Json.encodeToString(IndexFileData.serializer(), indexFileData))
    println("새로운 인덱스 파일을 생성했습니다.")
    return
}

fun addObjectToIndex(indexFile: File, file: File) {
    val indexFileData = Json.decodeFromString(IndexFileData.serializer(), indexFile.readText())
    val blobObject = GeetBlob(name = file.name, content = file.readText())
    saveObjectInGeet(blobObject)
    val sameNameObject = indexFileData.stagingArea.find { it.name == blobObject.name }

    if (sameNameObject != null) {
        if (sameNameObject.hashString == blobObject.hashString) {
            println("같은 내용의 개체가 이미 Staging Area에 존재합니다.")
            return
        }

        indexFileData.stagingArea.remove(sameNameObject)
        indexFileData.modifiedObjects.add(sameNameObject)
    }

    if (sameNameObject == null) {
        indexFileData.addedObjects.add(blobObject)
    }

    indexFileData.stagingArea.add(blobObject)
    indexFile.writeText(Json.encodeToString(IndexFileData.serializer(), indexFileData))
}

fun removeObjectFromIndex(file: File) {}

fun refreshIndex() {}