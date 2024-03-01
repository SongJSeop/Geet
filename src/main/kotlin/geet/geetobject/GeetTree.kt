package geet.geetobject

class GeetTree(
    override val filePath: String = "bak",
    val tree: List<GeetObjectWithFile>
): GeetObjectWithFile {

    override val type: String = "tree"
    override val content: String
        get() = tree.joinToString("\n") {
            "${it.type} ${it.hashString} ${it.filePath}\u0000"
        }
}