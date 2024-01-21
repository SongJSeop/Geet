package geet.objects

class GeetTree(
    type: String = "tree",
    name: String,
    val objects: List<GeetObject>,
): GeetObject(type, name, content = "") {

    init {
        content = objects.joinToString("") {
            "${it.type} ${it.hashString} ${it.name}\u0000"
        }
    }
}