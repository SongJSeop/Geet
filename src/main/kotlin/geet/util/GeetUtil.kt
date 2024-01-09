package geet.util

fun isGeetObjectType(type: String): Boolean {
    val typeLowerCase = type.lowercase()
    return typeLowerCase == "blob" ||
        typeLowerCase == "tree" ||
        typeLowerCase == "commit" ||
        typeLowerCase == "tag"
}