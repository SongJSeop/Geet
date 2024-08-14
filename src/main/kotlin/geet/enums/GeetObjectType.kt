package geet.enums

enum class GeetObjectType(val value: String) {
    BLOB("blob"),
    TREE("tree"),
    COMMIT("commit");

    override fun toString(): String {
        return value
    }
}