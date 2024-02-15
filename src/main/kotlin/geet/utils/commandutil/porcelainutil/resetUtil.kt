package geet.utils.commandutil.porcelainutil

fun changeToFullHash(commitString: String): String {
    if (startsWithHeadRef(commitString)) {
        var commitHash = getCurrentRefCommitHash()

        val carrotCount = countCarrot(commitString)
        for (i in 0 until carrotCount) {
            commitHash = getParentCommitFromCommitHash(commitHash)
        }

        return commitHash
    }
}

fun startsWithHeadRef(commitString: String): Boolean {
    val pattern = Regex("^HEAD\\^*$")
    return commitString.matches(pattern)
}

fun countCarrot(string: String): Int {
    val pattern = Regex("\\^")
    return pattern.findAll(string).count()
}