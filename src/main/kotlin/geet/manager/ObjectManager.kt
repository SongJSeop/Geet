package geet.manager

import geet.geetobject.*
import geet.util.toZlib
import java.io.File

class ObjectManager {
    
    val objectDir = File(".geet/objects")
    
    fun saveBlob(blobObject: GeetBlob): Unit {
        val blobDir = File(objectDir, blobObject.hash.substring(0, 2))
        val blobFile = File(blobDir, blobObject.hash.substring(2))

        if (!blobDir.exists()) {
            blobDir.mkdirs()
        }

        blobFile.writeText(blobObject.content.toZlib())
    }

    fun saveTree(treeObject: GeetTree): Unit {
        val treeDir = File(objectDir, treeObject.hash.substring(0, 2))
        val treeFile = File(treeDir, treeObject.hash.substring(2))

        if (!treeDir.exists()) {
            treeDir.mkdirs()
        }

        treeFile.writeText(treeObject.content.toZlib())
    }

    fun saveCommit(commitObject: GeetCommit): Unit {
        val commitDir = File(objectDir, commitObject.hash.substring(0, 2))
        val commitFile = File(commitDir, commitObject.hash.substring(2))

        if (!commitDir.exists()) {
            commitDir.mkdirs()
        }

        commitFile.writeText(commitObject.content.toZlib())
    }
}