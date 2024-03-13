package geet.manager

import geet.geetobject.GeetBlob
import geet.geetobject.GeetObject
import geet.geetobject.GeetObjectWithFile
import geet.geetobject.GeetTree
import geet.util.getRelativePathFromRoot
import geet.util.toZlib
import java.io.File

class ObjectManager {
    
    val objectDir = File(".geet/objects")
    
    fun saveBlob(file: File): GeetBlob {
        val blob = GeetBlob(content = file.readText(), filePath = getRelativePathFromRoot(file))
        
        val blobDir = File(objectDir, blob.hash.substring(0, 2))
        val blobFile = File(blobDir, blob.hash.substring(2))

        if (!blobDir.exists()) {
            blobDir.mkdirs()
        }

        blobFile.writeText(blob.content.toZlib())
        return blob
    }

    fun saveTree(file: File): GeetTree {
        val tree = mutableListOf<GeetObjectWithFile>()
        file.listFiles()?.forEach { it ->
            if (it.isDirectory && !it.listFiles().isNullOrEmpty()) {
                tree.add(saveTree(it))
            } else {
                tree.add(saveBlob(it))
            }
        }

        val treeObject = GeetTree(filePath = getRelativePathFromRoot(file), tree = tree)

        val treeDir = File(objectDir, treeObject.hash.substring(0, 2))
        val treeFile = File(treeDir, treeObject.hash.substring(2))

        if (!treeDir.exists()) {
            treeDir.mkdirs()
        }

        treeFile.writeText(treeObject.content.toZlib())
        return treeObject
    }

    fun isDeletedObject(geetObject: GeetObject): Boolean {
        val dirName = geetObject.hash.substring(0, 2)
        val fileName = geetObject.hash.substring(2)
        val objectFile = File(File(objectDir, dirName), fileName)
        return !objectFile.exists()
    }
}