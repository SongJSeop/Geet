package geet.objects

class GeetCommit(
    val tree: String,
    val parent: String,
    val message: String,
): GeetObject(type = "commit", name = "", content = "") {

        init {
            content = "tree $tree\n"

            if (parent != "") {
                content += "parent $parent\n"
            }

            content += "\n$message"
        }
}