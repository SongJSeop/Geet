package geet.manager

import geet.enums.StageObjectStatus
import geet.geetobject.GeetObjectWithFile
import geet.util.fromZlibToString
import geet.util.toZlib
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class StageObject(
    val hash: String,
    val slot: Int = 0,
    val filePath: String,
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
    val indexData: IndexData
        get() {
            if (!indexFile.exists()) {
                return IndexData(stageObjects = mutableListOf(), lastCommitObjects = emptyList())
            }

            return Json.decodeFromString(IndexData.serializer(), indexFile.readText().fromZlibToString())
        }

    fun writeIndex() {
        indexFile.writeText(Json.encodeToString(IndexData.serializer(), indexData).toZlib())
    }
}