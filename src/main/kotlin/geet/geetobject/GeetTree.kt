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
}