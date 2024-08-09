package geet.geetobject

import geet.enums.GeetObjectType
import geet.util.createHash

interface GeetObject {

    val type: GeetObjectType
    val content: String
    val hash: String
        get() = createHash(type, content)
}