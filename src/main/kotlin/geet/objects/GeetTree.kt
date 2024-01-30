package geet.objects

import geet.utils.GeetObjectType.*

class GeetTree(
    path: String = "bak",
    val objects: MutableList<GeetObject>,
): GeetObject(type = TREE, path = path, content = "") {

    init {
        content = objects.joinToString("") {
            "${it.type.value} ${it.hashString} ${it.path}\n"
        }
    }
}