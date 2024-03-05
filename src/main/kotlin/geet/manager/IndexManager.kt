package geet.manager

import geet.enums.StageObjectStatus
import geet.geetobject.GeetBlob
import geet.util.fromZlibToString
import geet.util.toZlib
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import java.time.LocalDateTime

@Serializable
data class StageObject(
    val blob: GeetBlob,
    val slot: Int,
    val status: StageObjectStatus,
    val lastUpdateTime: String
)

@Serializable
data class IndexData(
    val stageObjects: MutableList<StageObject>,
    val lastCommitObjects: List<GeetBlob>
)

class IndexManager {

    val indexFile = File(".geet/index")
    val indexData: IndexData

    init {
        if (indexFile.exists()) {
            indexData = Json.decodeFromString(IndexData.serializer(), indexFile.readText().fromZlibToString())
        } else {
            indexData = IndexData(mutableListOf(), listOf())
        }
    }

    fun addToStage(blob: GeetBlob, deleted: Boolean = false, slot: Int = 0) {
        var status: StageObjectStatus
        when (true) {
            deleted -> status = StageObjectStatus.DELETED
            (searchObjectFromLastCommit(blob.filePath) == null) -> status = StageObjectStatus.NEW
            else -> status = StageObjectStatus.MODIFIED
        }

        val stageObject = StageObject(
            blob = blob,
            slot = slot,
            status = status,
            lastUpdateTime = LocalDateTime.now().toString()
        )
        indexData.stageObjects.add(stageObject)
    }

    fun removeFromStage(filePath: String) {
        indexData.stageObjects.removeIf { it.blob.filePath == filePath }
    }

    fun searchObjectFromStage(filePath: String): StageObject? {
        return indexData.stageObjects.find { it.blob.filePath == filePath }
    }

    fun searchObjectFromLastCommit(filePath: String): GeetBlob? {
        return indexData.lastCommitObjects.find { it.filePath == filePath }
    }

    fun writeIndex() {
        indexFile.writeText(Json.encodeToString(IndexData.serializer(), indexData).toZlib())
    }
}