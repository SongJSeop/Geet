package geet.geetobject

import java.time.LocalDateTime

class GeetCommit(
    val tree: GeetTree,
    val parent: GeetCommit?,
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val message: String
): GeetObject {

    override val type: String = "commit"
    override val content: String
        get() {
            return "tree ${tree.hashString}\n" +
                    (parent?.let { "parent ${it.hashString}\n" } ?: "") +
                    "date ${dateTime.toString()}\n" +
                    "\n" +
                    message
        }
}