package geet.objects

import java.time.LocalDateTime

class GeetCommit(
    val tree: String,
    val parent: String,
    val message: String,
    val datetime: LocalDateTime = LocalDateTime.now()
): GeetObject(type = "commit", name = "", content = "") {

        init {
            content = "tree $tree\n"

            if (parent != "") {
                content += "parent $parent\n"
            }
            content += "createtime ${datetime}\n"

            content += "\n$message\n"
        }
}