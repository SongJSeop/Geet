package geet.commands.plumbing

data class GeetHashObjectOptions(
    var type: String = "blob",
    var write: Boolean = false,
    var path: String? = null
)

fun geetHashObject(commandLines: Array<String>) {
    val options = getHashObjectOptions(commandLines)
    println(options)
}

fun getHashObjectOptions(commandLines: Array<String>): GeetHashObjectOptions {
    val options = GeetHashObjectOptions()

    var index: Int = 0
    while (index < commandLines.size) {
        when (commandLines[index]) {
            "-t" -> {
                if (!(commandLines[index + 1] == "blob" ||
                    commandLines[index + 1] == "tree" ||
                    commandLines[index + 1] == "commit" ||
                    commandLines[index + 1] == "tag")) {
                    println("'-t' 옵션에 대하여 올바른 개체 타입이 지정되지 않았습니다.: ${commandLines[index + 1]}")
                }

                options.type = commandLines[index + 1]
                index += 2
            }
            "-w" -> {}
            else -> {}
        }
    }

    return options
}