package geet.commands.plumbing

import geet.util.isObjectType

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

    var index: Int = 1
    while (index < commandLines.size) {
        when (commandLines[index]) {
            "-t" -> {
                if (!isObjectType(commandLines[index + 1])) {
                    println("'-t' 옵션에 대하여 올바른 개체 타입이 지정되지 않았습니다.: ${commandLines[index + 1]}")
                    // TODO: 에러 처리
                }

                options.type = commandLines[index + 1]
                index += 2
            }
            "-w" -> {
                options.write = true
                index += 1
            }
            else -> {
                if (options.path != null) {
                    println("지정할 수 없는 옵션입니다.: ${commandLines[index]}")
                    // TODO: 에러 처리
                }

                options.path = commandLines[index]
                index += 1
            }
        }
    }

    return options
}