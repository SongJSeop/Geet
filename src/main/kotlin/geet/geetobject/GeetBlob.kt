package geet.geetobject

import geet.enums.GeetObjectType.BLOB
import kotlinx.serialization.Serializable
import java.io.File

@Serializable
class GeetBlob(
    override val content: String,
    override val filePath: String,
): GeetObjectWithFile {

    override val type = BLOB
    override val fileName: String
        get() = filePath.split("/").last()

    override fun toString(): String {
        return "GeetBlob(filePath='$filePath', content='$content')"
    }

    fun toTree(): GeetTree {
        var rootTree = GeetTree(filePath = "", tree = mutableListOf<GeetObjectWithFile>())
        if (filePath.contains(File.separatorChar)) {
            val splitPath = filePath.split(File.separatorChar)
            var parentTree: GeetTree = GeetTree(filePath = splitPath[0], tree = mutableListOf<GeetObjectWithFile>())
            var nowPath = splitPath[0]
            splitPath.subList(1, splitPath.size - 1).forEach { path ->
                nowPath += (File.separatorChar + path)
                val newTree = GeetTree(filePath = nowPath, tree = mutableListOf<GeetObjectWithFile>())
                parentTree.tree.add(newTree)
            }

            parentTree.tree.add(this)
            rootTree.tree.add(parentTree)
            return rootTree
        }

        rootTree.tree.add(this)
        return rootTree
    }
}