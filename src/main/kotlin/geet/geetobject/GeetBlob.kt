package geet.geetobject

class GeetBlob(
    override val content: String,
    override val filePath: String
): GeetObjectWithFile {

    override val type: String = "blob"
}