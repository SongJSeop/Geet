package geet.utils.commandutil.porcelainutil

fun changeToFullHash(commitString: String) {
    if (startsWithHeadRef(commitString)) {
        println(commitString + "은(는) HEAD를 가리키는 상대 참조입니다.")
    }
}

fun startsWithHeadRef(commitString: String): Boolean {
    val pattern = Regex("^HEAD\\^*$")
    return commitString.matches(pattern)
}