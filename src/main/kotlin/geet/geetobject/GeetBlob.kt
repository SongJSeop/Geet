package geet.geetobject

import geet.enums.GeetObjectType

class GeetBlob(
    override val content: String
) : GeetObject {

    override val type: GeetObjectType = GeetObjectType.BLOB
}