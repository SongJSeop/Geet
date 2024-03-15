package geet.manager

import geet.geetobject.*
import geet.util.getRelativePathFromRoot
import geet.util.toZlib
import java.io.File

class ObjectManager {
    
    val objectDir = File(".geet/objects")
    
    fun saveBlob(file: File): GeetBlob {
        val blobObject = GeetBlob(content = file.readText(), filePath = getRelativePathFromRoot(file))
        
        val blobDir = File(objectDir, blobObject.hash.substring(0, 2))
        val blobFile = File(blobDir, blobObject.hash.substring(2))

        if (!blobDir.exists()) {
            blobDir.mkdirs()
        }

        blobFile.writeText(blobObject.content.toZlib())
        return blobObject
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

    fun saveCommit(tree: GeetTree, parent: GeetCommit? = null, message: String): GeetCommit {
        val commitObject = GeetCommit(tree = tree, parent = parent, message = message)

        val commitDir = File(objectDir, commitObject.hash.substring(0, 2))
        val commitFile = File(commitDir, commitObject.hash.substring(2))

        if (!commitDir.exists()) {
            commitDir.mkdirs()
        }

        commitFile.writeText(commitObject.content.toZlib())
        return commitObject
    }
}