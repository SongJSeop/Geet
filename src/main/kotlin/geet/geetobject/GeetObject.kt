package geet.geetobject

import geet.enums.GeetObjectType
import geet.util.const.messageDigest

interface GeetObject {

    val type: GeetObjectType
    val content: String
    val hash: String
        get() {
            val header = "${type.value} ${content.length}\u0000"
            val store = header + content

            val hash = messageDigest.digest(store.toByteArray())
            return hash.joinToString("") {
                String.format("%02x", it)
            }
        }
}