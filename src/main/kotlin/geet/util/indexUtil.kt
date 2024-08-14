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

val indexFile: File
    get() {
        val index = File(getGeetRepoDir(), "index")
        if (!index.exists()) {
            val indexData = IndexData(emptyList())
            index.writeText(Json.encodeToString(IndexData.serializer(), indexData))
        }

        return index
    }

fun getIndexData(): IndexData {
    return Json.decodeFromString(IndexData.serializer(), indexFile.readText())
}

fun saveIndexData(indexData: IndexData) {
    indexFile.writeText(Json.encodeToString(IndexData.serializer(), indexData))
}

fun addStagingObject(stagingObject: StagingObject) {
    val indexData = getIndexData()
    saveIndexData(indexData.copy(
        stagingArea = indexData.stagingArea + stagingObject
    ))
}