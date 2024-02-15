package geet.utils.commandutil.porcelainutil

fun changeToFullHash(commitString: String) {
    if (startsWithHeadRef(commitString)) {
        val carrotCount = countCarrot(commitString)
        for (i in 0 until carrotCount) {
            println("carrot")
        }
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