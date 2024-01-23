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
    val modifiedObjects: MutableList<String>,
    val addedObjects: MutableList<String>,
    val removedObjects: MutableList<String>,
)

fun updateIndex(updateIndexOptions: GeetUpdateIndexOptions) {
    val file = File(updateIndexOptions.path)
    if (!file.exists()) {
        throw NotFoundException("파일을 찾을 수 없습니다. : ${updateIndexOptions.path}")
    }

    if (file.isDirectory) {
        throw NotFoundException("update-index 명령어는 디렉토리를 지원하지 않습니다. : ${updateIndexOptions.path}")
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
        modifiedObjects = mutableListOf(),
        addedObjects = mutableListOf(blobObject.name),
        removedObjects = mutableListOf(),
    )
    indexFile.writeText(Json.encodeToString(IndexFileData.serializer(), indexFileData))
    println("새로운 인덱스 파일을 생성했습니다.")
    return
}

fun addObjectToIndex(blobObject: GeetObject) {
    val indexFile = File(".geet/index")
    if (!indexFile.exists()) {
        createNewIndexFile(blobObject)
        return
    }

    val indexFileData = Json.decodeFromString(IndexFileData.serializer(), indexFile.readText())

    val sameNameObjectInStagingArea = indexFileData.stagingArea.find { it.name == blobObject.name }
    val sameNameObjectInLastCommit = indexFileData.lastCommitObjects.find { it.name == blobObject.name }

    if (sameNameObjectInStagingArea != null) {
        if (sameNameObjectInStagingArea.hashString == blobObject.hashString) {
            return
        }

        indexFileData.stagingArea.remove(sameNameObjectInStagingArea)
    }

    if (sameNameObjectInLastCommit != null) {
        if (sameNameObjectInLastCommit.hashString == blobObject.hashString) {
            return
        }

        indexFileData.modifiedObjects.add(blobObject.name)
    }

    if (sameNameObjectInLastCommit == null) {
        indexFileData.addedObjects.add(blobObject.name)
    }

    saveObjectInGeet(blobObject)
    indexFileData.stagingArea.add(blobObject)
    indexFile.writeText(Json.encodeToString(IndexFileData.serializer(), indexFileData))
}

fun removeObjectFromIndex(blobObject: GeetObject) {
    val indexFile = File(".geet/index")
    if (!indexFile.exists()) {
        throw NotFoundException("인덱스 파일을 찾을 수 없습니다.\nupdate-index --add 명령어로 우선 개체를 Staging Area에 올려주세요. : ${indexFile.absolutePath}")
    }

    val indexFileData = Json.decodeFromString(IndexFileData.serializer(), indexFile.readText())

    if (indexFileData.stagingArea.find { it.name == blobObject.name } == null) {
        throw NotFoundException("Staging Area에 존재하지 않는 개체입니다. : ${blobObject.name}")
    }

    indexFileData.stagingArea.removeIf { it.name == blobObject.name }
    indexFileData.modifiedObjects.removeIf { it == blobObject.name }
    indexFileData.addedObjects.removeIf { it == blobObject.name }

    indexFile.writeText(Json.encodeToString(IndexFileData.serializer(), indexFileData))
}

// TODO: refreshIndex 구현
fun refreshIndex() {
    println("개발 중인 기능입니다.")
}