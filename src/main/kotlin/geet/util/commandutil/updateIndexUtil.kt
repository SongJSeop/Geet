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
    }

    when (updateIndexOptions.option) {
        "--add" -> {
            addObjectToIndex(file)
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

fun addObjectToIndex(file: File) {}

fun removeObjectFromIndex(file: File) {}

fun refreshIndex() {}