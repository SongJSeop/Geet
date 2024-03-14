package geet.geetobject

import geet.enums.GeetObjectType.TREE

class GeetTree(
    override val filePath: String,
    val tree: List<GeetObjectWithFile>
): GeetObjectWithFile {

    override val type = TREE
    override val content: String
        get() = tree.joinToString("") {
            "${it.type} ${it.hash} ${it.filePath}\n"
        }

    fun getAllBlobObjectsOfTree(): List<GeetBlob> {
        val blobs = mutableListOf<GeetBlob>()
        tree.forEach {
            if (it is GeetBlob) {
                blobs.add(it)
            } else {
                blobs.addAll((it as GeetTree).getAllBlobObjectsOfTree())
            }
        }
        return blobs
    }
}