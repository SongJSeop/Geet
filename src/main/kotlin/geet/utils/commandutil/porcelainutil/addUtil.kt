package geet.utils.commandutil.porcelainutil

import geet.managers.StageObjectData
import geet.objects.GeetBlob
import geet.utils.ObjectStatus
import geet.utils.getNotIgnoreFiles
import geet.utils.getRelativePath
import geet.utils.indexManager
import java.io.File

fun addRemovedFileDirectly(file: File): Boolean {
    getRemovedFiles(getNotIgnoreFiles(File("."))).forEach {
        if (getRelativePath(it) == getRelativePath(file.path)) {
            indexManager.getIndexFileData().stagingArea.add(
                StageObjectData(
                    blobObject = GeetBlob(
                        path = getRelativePath(it),
                        content = "removed"
                    ),
                    status = ObjectStatus.REMOVED
                )
            )
            indexManager.writeIndexFile()
            return true
        }
    }

    return false
}