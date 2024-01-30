package geet.utils

enum class GeetObjectLoacation {
    STAGING_AREA_OBJECTS,
    LAST_COMMIT_OBJECTS,
}

enum class GeetObjectType(val value: String) {
    BLOB("blob"),
    TREE("tree"),
    COMMIT("commit"),
    TAG("tag"),
}