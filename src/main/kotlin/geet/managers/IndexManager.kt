package geet.managers

import geet.utils.GeetObjectLoacation.*
import geet.objects.GeetBlob
import geet.objects.GeetObject
import geet.objects.GeetTree
import geet.utils.*
import geet.utils.ObjectStatus.*
import geet.utils.commandutil.saveObjectInGeet
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class StageObjectData(
    val blobObject: GeetObject,
    val status: ObjectStatus,
)

@Serializable
data class IndexData(
    val stagingArea: MutableList<StageObjectData> = mutableListOf(),
    val lastCommitObjects: MutableList<GeetObject> = mutableListOf(),
)

class IndexManager {

    private val indexFile: File = File(GEET_INDEX_FILE_PATH)
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
        if (getRelativePath(blobObject.path) in getIgnoreFiles()) {
            return
        }

        if (isIn(where = STAGING_AREA, blobObject)) {
            if (isSameWith(where = STAGING_AREA, blobObject)) {
                return
            }

            indexData.stagingArea.removeIf { it.blobObject.path == blobObject.path }
        }

        if (!isIn(where = LAST_COMMIT, blobObject)) {
            indexData.stagingArea.add(StageObjectData(blobObject, status = NEW))
        } else {
            if (isSameWith(where = LAST_COMMIT, blobObject)) {
                removeObjectFromStagingArea(blobObject)
                writeIndexFile()
                return
            }

            indexData.stagingArea.add(StageObjectData(blobObject, status = MODIFIED))
        }

        saveObjectInGeet(blobObject)
    }

    fun addTreeInStagingArea(treeObject: GeetTree) {
        if (getRelativePath(treeObject.path) in getIgnoreFiles()) {
            return
        }

        treeObject.objects.forEach {
            when (it) {
                is GeetBlob -> addBlobInStagingArea(it)
                is GeetTree -> addTreeInStagingArea(it)
            }
        }
    }

    fun removeObjectFromStagingArea(blobObject: GeetBlob) {
        indexData.stagingArea.removeIf { it.blobObject.path == blobObject.path }
    }

    fun isIn(where: GeetObjectLoacation, blobObject: GeetBlob): Boolean {
        return when (where) {
            STAGING_AREA -> {
                indexData.stagingArea.find { getRelativePath(it.blobObject.path) == blobObject.path } != null
            }
            LAST_COMMIT -> {
                indexData.lastCommitObjects.find { getRelativePath(it.path) == blobObject.path } != null
            }
        }
    }

    fun isSameWith(where: GeetObjectLoacation, blobObject: GeetBlob): Boolean {
        return when (where) {
            STAGING_AREA -> {
                val sameFileInStagingArea = indexData.stagingArea.find { getRelativePath(it.blobObject.path) == blobObject.path }
                sameFileInStagingArea?.blobObject?.hashString == blobObject.hashString
            }
            LAST_COMMIT -> {
                val sameFileInLastCommit = indexData.lastCommitObjects.find { getRelativePath(it.path) == blobObject.path }
                sameFileInLastCommit?.hashString == blobObject.hashString
            }
        }
    }

    fun writeIndexFile() {
        indexFile.writeText(Json.encodeToString(IndexData.serializer(), indexData))
    }
}