package geet.geetobject

class GeetTree(
    override val filePath: String,
    val tree: List<GeetObjectWithFile>
): GeetObjectWithFile {

    override val type: String = "tree"
    override val content: String
        get() = tree.joinToString("") {
            "${it.type} ${it.hashString} ${it.filePath}\n"
        }
}