package geet.objects

class GeetTree(
    path: String = "bak",
    val objects: MutableList<GeetObject>,
): GeetObject(type = "tree", path = path, content = "") {

    init {
        content = objects.joinToString("") {
            "${it.type} ${it.hashString} ${it.path}\n"
        }
    }
}