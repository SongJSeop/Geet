package geet.managers

import geet.objects.GeetObject
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class IndexFileData(
    val stagingArea: MutableList<GeetObject> = mutableListOf(),
    val lastCommitObjects: MutableList<GeetObject> = mutableListOf(),
    val modifiedObjects: Set<String> = setOf(),
    val addedObjects: Set<String> = setOf(),
    val removedObjects: Set<String> = setOf(),
)

class IndexFileManager {

    val indexFile: File = File(".geet/index")
    val indexFileData: IndexFileData by lazy {
        if (!indexFile.exists()) {
            indexFile.createNewFile()
        }

        val indexFileContents = indexFile.readText()
        if (indexFileContents == "") {
            IndexFileData()
        } else {
            Json.decodeFromString(IndexFileData.serializer(), indexFileContents)
        }
    }
}