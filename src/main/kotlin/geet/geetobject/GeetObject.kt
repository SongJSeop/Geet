package geet.geetobject

import geet.enums.GeetObjectType
import geet.util.getHash

interface GeetObject {

    val type: GeetObjectType
    val content: String
    val hash: String
        get() = getHash(type, content)
}