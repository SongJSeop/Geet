package geet.utils

enum class GeetObjectLoacation {
    STAGING_AREA_OBJECTS,
    LAST_COMMIT_OBJECTS,
}

enum class GeetObjectType {
    BLOB,
    TREE,
    COMMIT,
    TAG
}