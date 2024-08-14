package geet.geetobject

import geet.enums.GeetObjectType

class GeetTree(
    val children: Map<String, GeetObject>
): GeetObject {

    override val type: GeetObjectType = GeetObjectType.TREE
    override val content: String
        get() = children.entries.joinToString("\n") { "${it.value.type} ${it.value.hash} ${it.key}" }
}