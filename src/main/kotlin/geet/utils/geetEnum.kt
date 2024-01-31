package geet.utils

enum class GeetObjectLoacation {
    STAGING_AREA,
    LAST_COMMIT,
}

enum class GeetObjectType(val value: String) {
    BLOB("blob"),
    TREE("tree"),
    COMMIT("commit"),
    TAG("tag"),
}