package geet.objects

import geet.util.messageDigest

open class GeetObject(
    val type: String,
    var content: String,
) {

    open val hashString: String
        get() {
            val header = "${type} ${content.length}\u0000"
            val store = header + content

            val hash = messageDigest.digest(store.toByteArray())
            return hash.joinToString("") {
                String.format("%02x", it)
            }
        }
}