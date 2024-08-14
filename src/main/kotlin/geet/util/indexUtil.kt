package geet.util

import geet.geetobject.StagingObject
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class IndexData(
    val stagingArea: List<StagingObject>,
    val lastCommitHash: String? = null
)

private val indexFile: File
    get() {
        val index = File(getGeetRepoDir(), "index")
        if (!index.exists()) {
            val indexData = IndexData(emptyList())
            index.writeText(Json.encodeToString(IndexData.serializer(), indexData).toZlib())
        }

        return index
    }

fun getIndexData(): IndexData {
    return Json.decodeFromString(IndexData.serializer(), indexFile.readText().fromZlibToString())
}

fun addStagingObject(stagingObject: StagingObject) {
    val indexData = getIndexData()
    val updatedIndexData = indexData.copy(
        stagingArea = indexData.stagingArea + stagingObject
    )
    indexFile.writeText(Json.encodeToString(IndexData.serializer(), updatedIndexData).toZlib())
}

fun getSameStagingObject(filePath: String): StagingObject? {
    return getIndexData().stagingArea.find { it.filePath == filePath }
}