package geet.objects

import geet.utils.GeetObjectType
import geet.utils.GeetObjectType.*
import java.time.LocalDateTime

class GeetCommit(
    val tree: String,
    val parent: String?,
    val message: String,
    val datetime: LocalDateTime = LocalDateTime.now()
): GeetObject(type = COMMIT, path = "", content = "") {

        init {
            content = "tree $tree\n"

            if (parent != null) {
                content += "parent $parent\n"
            }
            content += "createtime ${datetime}\n"

            content += "\n$message\n"
        }
}