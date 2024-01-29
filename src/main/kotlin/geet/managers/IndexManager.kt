package geet.managers

import geet.objects.GeetBlob
import geet.objects.GeetObject
import geet.objects.GeetTree
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

    fun addBlobInStagingArea(blobObject: GeetBlob) {
        val sameFileInStagingArea = indexData.stagingArea.find { it.path == blobObject.path }
        val sameFileInLastCommit = indexData.lastCommitObjects.find { it.path == blobObject.path }

        if (sameFileInStagingArea != null) {
            if (sameFileInStagingArea.hashString == blobObject.hashString) {
                return
            }

            indexData.stagingArea.remove(sameFileInStagingArea)
        }

        if (sameFileInLastCommit == null) {
            indexData.addedObjects.add(blobObject.path)
        } else {
            if (sameFileInLastCommit.hashString == blobObject.hashString) {
                removeObjectFromStagingArea(blobObject)
                writeIndexFile()
                return
            }

            indexData.modifiedObjects.add(blobObject.path)
        }

        saveObjectInGeet(blobObject)
        indexData.stagingArea.add(blobObject)
    }

    fun addTreeInStagingArea(treeObject: GeetTree) {
        treeObject.objects.forEach {
            when (it) {
                is GeetBlob -> addBlobInStagingArea(it)
                is GeetTree -> addTreeInStagingArea(it)
            }
        }
    }

    fun removeObjectFromStagingArea(blobObject: GeetBlob) {
        indexData.stagingArea.removeIf { it.path == blobObject.path }
        indexData.modifiedObjects.remove(blobObject.path)
        indexData.addedObjects.remove(blobObject.path)
        indexData.removedObjects.remove(blobObject.path)
    }

    fun writeIndexFile() {
        indexFile.writeText(Json.encodeToString(IndexData.serializer(), indexData))
    }
}