package geet.geetobject

import geet.util.const.messageDigest

interface GeetObject {

    val type: String
    val content: String
    val hashString: String
        get() {
            val header = "${type} ${content.length}\u0000"
            val store = header + content

            val hash = messageDigest.digest(store.toByteArray())
            return hash.joinToString("") {
                String.format("%02x", it)
            }
        }
}