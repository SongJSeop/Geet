package geet.geetobject

import geet.enums.GeetObjectType.COMMIT
import java.time.LocalDateTime

class GeetCommit(
    val tree: GeetTree,
    val parent: GeetCommit?,
    val message: String
): GeetObject {

    override val type = COMMIT
    val dateTime: LocalDateTime = LocalDateTime.now()
    override val content: String
        get() {
            return "tree ${tree.hash}\n" +
                    (parent?.let { "parent ${it.hash}\n" } ?: "") +
                    "date ${dateTime.toString()}\n" +
                    "\n" +
                    message
        }
}