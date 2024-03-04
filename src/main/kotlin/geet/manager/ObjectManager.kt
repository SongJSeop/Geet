package geet.manager

import geet.exception.BadRequest
import geet.exception.NotFound
import geet.geetobject.GeetBlob
import geet.geetobject.GeetObjectWithFile
import geet.geetobject.GeetTree
import geet.util.toZlib
import java.io.File

class ObjectManager {
    
    val objectDir = File(".geet/objects")
    
    fun saveBlob(file: File): GeetBlob {
        val blob = GeetBlob(content = file.readText(), filePath = file.name)
        
        val blobDir = File(objectDir, blob.hashString.substring(0, 2))
        val blobFile = File(blobDir, blob.hashString.substring(2))

        if (!blobDir.exists()) {
            blobDir.mkdirs()
        }

        blobFile.writeText(blob.content.toZlib())
        return blob
    }

    fun saveTree(file: File): GeetTree {
        val tree = mutableListOf<GeetObjectWithFile>()
        file.listFiles()?.forEach { it ->
            if (it.isDirectory) {
                tree.add(saveTree(it))
            } else {
                tree.add(saveBlob(it))
            }
        }

        val treeObject = GeetTree(filePath = file.path, tree = tree)

        val treeDir = File(objectDir, treeObject.hashString.substring(0, 2))
        val treeFile = File(treeDir, treeObject.hashString.substring(2))

        if (!treeDir.exists()) {
            treeDir.mkdirs()
        }

        treeFile.writeText(treeObject.content.toZlib())
        return treeObject
    }
}