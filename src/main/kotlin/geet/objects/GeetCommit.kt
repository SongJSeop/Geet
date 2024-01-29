package geet.objects

import java.time.LocalDateTime

class GeetCommit(
    val tree: String,
    val parent: String,
    val message: String,
    val datetime: LocalDateTime = LocalDateTime.now()
): GeetObject(type = "commit", path = "", content = "") {

        init {
            content = "tree $tree\n"

            if (parent != "") {
                content += "parent $parent\n"
            }
            content += "createtime ${datetime}\n"

            content += "\n$message\n"
        }
}