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
    val stagingArea: List<GeetObject>,
    val lastCommitObjects: List<GeetObject>,
    val modifiedObjects: List<GeetObject>,
    val addedObjects: List<GeetObject>,
    val removedObjects: List<GeetObject>,
)

fun updateIndex(updateIndexOptions: GeetUpdateIndexOptions) {
    val file = File(updateIndexOptions.path)
    if (!file.exists()) {
        throw NotFoundException("파일을 찾을 수 없습니다. : ${updateIndexOptions.path}")
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
    if (file.isFile) {
        val blobObject = GeetBlob(name = file.name, content = file.readText())
        saveObjectInGeet(blobObject)
        val indexFileData = IndexFileData(
            stagingArea = listOf(blobObject),
            lastCommitObjects = listOf(),
            modifiedObjects = listOf(),
            addedObjects = listOf(blobObject),
            removedObjects = listOf(),
        )
        indexFile.writeText(Json.encodeToString(IndexFileData.serializer(), indexFileData))
        println("새로운 인덱스 파일을 생성했습니다.")
        return
    }
}

fun addObjectToIndex(file: File) {}

fun removeObjectFromIndex(file: File) {}

fun refreshIndex() {}