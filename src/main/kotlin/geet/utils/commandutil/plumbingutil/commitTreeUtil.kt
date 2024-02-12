package geet.utils.commandutil.plumbingutil

import geet.commands.plumbing.GeetCommitTreeOptions
import geet.exceptions.BadRequest
import geet.objects.GeetCommit
import geet.utils.findObject

fun commitTree(commitTreeOptions: GeetCommitTreeOptions) {
    if (!findObject(type = "tree", sha1 = commitTreeOptions.tree)) {
        throw BadRequest("Tree 객체가 존재하지 않습니다. : ${commitTreeOptions.tree}")
    }

    if (commitTreeOptions.parent != "" && !findObject(type = "commit", sha1 = commitTreeOptions.parent)) {
        throw BadRequest("부모 커밋이 존재하지 않습니다. : ${commitTreeOptions.parent}")
    }

    val commitObject = GeetCommit(
        tree = commitTreeOptions.tree,
        parent = commitTreeOptions.parent,
        message = commitTreeOptions.message
    )
    saveObjectInGeet(commitObject)
    println(commitObject.hashString)
}