package geet.manager

import geet.enums.StageObjectStatus
import geet.enums.StageObjectStatus.*
import geet.geetobject.GeetBlob
import geet.geetobject.GeetObjectWithFile
import geet.geetobject.GeetTree
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
    val lastCommitObjects: List<GeetObjectWithFile>
)

class IndexManager {

    val indexFile = File(".geet/index")
    private val indexData: IndexData

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
            deleted -> status = DELETED
            (searchObjectFromLastCommit(blob.filePath) == null) -> status = NEW
            else -> status = MODIFIED
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

    fun getStageObjects(status: StageObjectStatus? = null): List<StageObject> {
        when (status) {
            NEW -> return indexData.stageObjects.filter { it.status == NEW }
            MODIFIED -> return indexData.stageObjects.filter { it.status == MODIFIED }
            DELETED -> return indexData.stageObjects.filter { it.status == DELETED }
            else -> return indexData.stageObjects
        }
    }

    fun searchObjectFromLastCommit(filePath: String): GeetObjectWithFile? {
        if (filePath.contains(File.separatorChar)) {
            val filePathSplit = filePath.split(File.separatorChar)
            var treeObject: GeetObjectWithFile = indexData.lastCommitObjects.find { it.filePath == filePathSplit[0] }
                ?: return null
            filePathSplit.subList(1, filePathSplit.size - 1).forEach { splitPath ->
                treeObject = (treeObject as GeetTree).tree.find {
                    it.filePath.split(File.separatorChar).last() == splitPath
                } ?: return null
            }
            return (treeObject as GeetTree).tree.find { it.filePath == filePathSplit.last() }
        }

        return indexData.lastCommitObjects.find { it.filePath == filePath }
    }

    fun writeIndex() {
        indexFile.writeText(Json.encodeToString(IndexData.serializer(), indexData).toZlib())
    }
}