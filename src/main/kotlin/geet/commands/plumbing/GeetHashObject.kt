package geet.commands.plumbing

fun geetHashObject(commandLines: Array<String>) {
    println("hash-object")
    commandLines.forEach { println(it) }
}

data class GeetHashObjectOptions(
    var type: String = "blob",
    var write: Boolean = false,
    var path: String? = null
)