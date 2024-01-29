package geet.objects

class GeetTree(
    name: String = "bak",
    val objects: MutableList<GeetObject>,
): GeetObject(type = "tree", name, content = "") {

    init {
        content = objects.joinToString("") {
            "${it.type} ${it.hashString} ${it.path}\n"
        }
    }
}