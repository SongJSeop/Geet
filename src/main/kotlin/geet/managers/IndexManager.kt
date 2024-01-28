package geet.managers

import geet.exceptions.NotFound
import geet.objects.GeetObject
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class IndexData(
    val stagingArea: MutableList<GeetObject> = mutableListOf(),
    val lastCommitObjects: MutableList<GeetObject> = mutableListOf(),
    val modifiedObjects: Set<String> = setOf(),
    val addedObjects: Set<String> = setOf(),
    val removedObjects: Set<String> = setOf(),
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

    fun getIndexData(): IndexData {
        return indexData
    }

    fun addObjectInStagingArea(geetObject: GeetObject) {
        indexData.stagingArea.add(geetObject)

        val sameFileInLastCommit = indexData.lastCommitObjects.find { it.name == geetObject.name }
        if (sameFileInLastCommit == null) {
            indexData.addedObjects.plus(geetObject.name)
        } else {
            indexData.modifiedObjects.plus(geetObject.name)
        }
    }

    fun removeObjectFromStagingArea(geetObject: GeetObject) {
        if (!indexData.stagingArea.contains(geetObject)) {
            throw NotFound("Staging Area에 존재하지 않는 개체입니다. : ${geetObject.name}")
        }

        indexData.stagingArea.removeIf { it.name == geetObject.name }
        indexData.modifiedObjects.minus(geetObject.name)
        indexData.addedObjects.minus(geetObject.name)
        indexData.removedObjects.minus(geetObject.name)
    }

    fun writeIndexFile() {
        indexFile.writeText(Json.encodeToString(IndexData.serializer(), indexData))
    }
}