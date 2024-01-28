package geet.managers

import geet.exceptions.NotModifiedObject
import geet.objects.GeetBlob
import geet.objects.GeetObject
import geet.utils.commandutil.saveObjectInGeet
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class IndexData(
    val stagingArea: MutableList<GeetObject> = mutableListOf(),
    val lastCommitObjects: MutableList<GeetObject> = mutableListOf(),
    val modifiedObjects: MutableSet<String> = mutableSetOf(),
    val addedObjects: MutableSet<String> = mutableSetOf(),
    val removedObjects: MutableSet<String> = mutableSetOf(),
)

class IndexManager {

    private val indexFile: File = File(".geet/index")
    private val indexData: IndexData by lazy {
        if (!indexFile.exists()) {
            indexFile.createNewFile()
        }

        val indexFileContents = indexFile.readText()
        if (indexFileContents == "") {
            IndexData()
        } else {
            Json.decodeFromString(IndexData.serializer(), indexFileContents)
        }
    }

    fun getIndexFileData(): IndexData {
        return indexData
    }

    fun addObjectInStagingArea(blobObject: GeetBlob) {
        val sameFileInStagingArea = indexData.stagingArea.find { it.name == blobObject.name }
        val sameFileInLastCommit = indexData.lastCommitObjects.find { it.name == blobObject.name }

        if (sameFileInStagingArea != null) {
            if (sameFileInStagingArea.hashString == blobObject.hashString) {
                throw NotModifiedObject("Staging Area에 이미 동일한 개체가 존재하여 추가되지 않았습니다.")
            }

            indexData.stagingArea.remove(sameFileInStagingArea)
        }

        if (sameFileInLastCommit == null) {
            indexData.addedObjects.add(blobObject.name)
        } else {
            if (sameFileInLastCommit.hashString == blobObject.hashString) {
                removeObjectFromStagingArea(blobObject)
                writeIndexFile()
                throw NotModifiedObject("최신 커밋과 동일한 상태로 Staging Area에 추가되지 않았습니다.")
            }

            indexData.modifiedObjects.add(blobObject.name)
        }

        saveObjectInGeet(blobObject)
        indexData.stagingArea.add(blobObject)
    }

    fun removeObjectFromStagingArea(blobObject: GeetBlob) {
        indexData.stagingArea.removeIf { it.name == blobObject.name }
        indexData.modifiedObjects.remove(blobObject.name)
        indexData.addedObjects.remove(blobObject.name)
        indexData.removedObjects.remove(blobObject.name)
    }

    fun writeIndexFile() {
        indexFile.writeText(Json.encodeToString(IndexData.serializer(), indexData))
    }
}