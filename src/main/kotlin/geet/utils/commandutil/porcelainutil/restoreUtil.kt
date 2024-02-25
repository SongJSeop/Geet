package geet.utils.commandutil.porcelainutil

fun isSourceOption(option: String): Boolean {
    val regex = Regex("^--source=HEAD~[0-9]+")
    if (!regex.matches(option)) {
        return false
    }
    return true
}

fun getSourceCommitHash(option: String): String {
    val index = option.indexOf("~")
    val carrotCount = option.substring(index + 1).toInt()
    var commitHash = getCurrentRefCommitHash()
    for (i in 0 until carrotCount) {
        commitHash = getParentCommitFromCommitHash(commitHash)
    }
    return commitHash
}