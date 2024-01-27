package geet.util.commandutil

import geet.commands.plumbing.GeetUpdateIndexOptions
import geet.exception.NotFound
import geet.exception.NotModifiedObject
import geet.objects.GeetBlob
import geet.objects.GeetObject
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class IndexFileData(
    val stagingArea: MutableList<GeetObject>,
    val lastCommitObjects: MutableList<GeetObject>,
    val modifiedObjects: Set<String>,
    val addedObjects: Set<String>,
    val removedObjects: Set<String>,
)

fun updateIndex(updateIndexOptions: GeetUpdateIndexOptions) {
    val file = File(updateIndexOptions.path)
    if (!file.exists()) {
        throw NotFound("파일을 찾을 수 없습니다. : ${updateIndexOptions.path}")
    }

    if (file.isDirectory) {
        throw NotFound("update-index 명령어는 디렉토리를 지원하지 않습니다. : ${updateIndexOptions.path}")
    }
    val blobObject = GeetBlob(name = file.name, content = file.readText())

    when (updateIndexOptions.option) {
        "--add" -> {
            addObjectToIndex(blobObject)
            println("개체를 Staging Area에 추가했습니다.")
        }
        "--remove" -> {
            removeObjectFromIndex(blobObject)
            println("개체를 Staging Area에서 제거했습니다.")
        }
        "--refresh" -> refreshIndex()
    }
}

fun createNewIndexFile(blobObject: GeetObject) {
    saveObjectInGeet(blobObject)

    val indexFile = File(".geet/index")
    val indexFileData = IndexFileData(
        stagingArea = mutableListOf(blobObject),
        lastCommitObjects = mutableListOf(),
        modifiedObjects = setOf(),
        addedObjects = setOf(blobObject.name),
        removedObjects = setOf(),
    )
    indexFile.writeText(Json.encodeToString(IndexFileData.serializer(), indexFileData))
    println("새로운 인덱스 파일을 생성했습니다.")
    return
}

fun addObjectToIndex(geetObject: GeetObject) {
    val indexFile = File(".geet/index")
    if (!indexFile.exists()) {
        createNewIndexFile(geetObject)
        return
    }

    val indexFileData = Json.decodeFromString(IndexFileData.serializer(), indexFile.readText())

    val sameNameObjectInStagingArea = indexFileData.stagingArea.find { it.name == geetObject.name }
    val sameNameObjectInLastCommit = indexFileData.lastCommitObjects.find { it.name == geetObject.name }

    if (sameNameObjectInStagingArea != null) {
        if (sameNameObjectInStagingArea.hashString == geetObject.hashString) {
            throw NotModifiedObject("Staging Area에 이미 동일한 개체가 존재하여 추가되지 않았습니다.")
        }

        indexFileData.stagingArea.remove(sameNameObjectInStagingArea)
    }

    if (sameNameObjectInLastCommit != null) {
        if (sameNameObjectInLastCommit.hashString == geetObject.hashString) {
            indexFileData.modifiedObjects.minus(geetObject.name)
            indexFileData.removedObjects.minus(geetObject.name)
            indexFileData.addedObjects.minus(geetObject.name)
            indexFile.writeText(Json.encodeToString(IndexFileData.serializer(), indexFileData))
            throw NotModifiedObject("최신 커밋과 동일한 상태로 Staging Area에 추가되지 않았습니다.")
        }

        indexFileData.modifiedObjects.plus(geetObject.name)
    }

    if (sameNameObjectInLastCommit == null) {
        indexFileData.addedObjects.plus(geetObject.name)
    }

    saveObjectInGeet(geetObject)
    indexFileData.stagingArea.add(geetObject)
    indexFile.writeText(Json.encodeToString(IndexFileData.serializer(), indexFileData))
}

fun removeObjectFromIndex(blobObject: GeetObject) {
    val indexFile = File(".geet/index")
    if (!indexFile.exists()) {
        throw NotFound("인덱스 파일을 찾을 수 없습니다.\nupdate-index --add 명령어로 우선 개체를 Staging Area에 올려주세요. : ${indexFile.absolutePath}")
    }

    val indexFileData = Json.decodeFromString(IndexFileData.serializer(), indexFile.readText())

    if (indexFileData.stagingArea.find { it.name == blobObject.name } == null) {
        throw NotFound("Staging Area에 존재하지 않는 개체입니다. : ${blobObject.name}")
    }

    indexFileData.stagingArea.removeIf { it.name == blobObject.name }
    indexFileData.modifiedObjects.minus(blobObject.name)
    indexFileData.addedObjects.minus(blobObject.name)
    indexFileData.removedObjects.minus(blobObject.name)

    indexFile.writeText(Json.encodeToString(IndexFileData.serializer(), indexFileData))
}

// TODO: refreshIndex 구현
fun refreshIndex() {
    println("개발 중인 기능입니다.")
}