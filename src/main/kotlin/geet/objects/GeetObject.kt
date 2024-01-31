package geet.objects

import geet.utils.GeetObjectType
import geet.utils.messageDigest
import kotlinx.serialization.Serializable

@Serializable
open class GeetObject(
    val type: GeetObjectType,
    var path: String,
    var content: String,
) {

    open val hashString: String
        get() {
            val header = "${type.value} ${content.length}\u0000"
            val store = header + content

            val hash = messageDigest.digest(store.toByteArray())
            return hash.joinToString("") {
                String.format("%02x", it)
            }
        }
}