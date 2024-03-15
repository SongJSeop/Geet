package geet.geetobject

import geet.enums.GeetObjectType.BLOB
import kotlinx.serialization.Serializable

@Serializable
class GeetBlob(
    override val content: String,
    override val filePath: String,
): GeetObjectWithFile {

    override val type = BLOB
    override val fileName: String
        get() = filePath.split("/").last()

    override fun toString(): String {
        return "GeetBlob(filePath='$filePath', content='$content')"
    }
}