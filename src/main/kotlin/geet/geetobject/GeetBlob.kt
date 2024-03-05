package geet.geetobject

import geet.enums.GeetObjectType.BLOB

class GeetBlob(
    override val content: String,
    override val filePath: String
): GeetObjectWithFile {

    override val type = BLOB
}