package geet.commands.plumbing

import geet.exceptions.BadRequest
import geet.utils.commandutil.createHashObject
import geet.utils.isGeetObjectType

data class GeetHashObjectOptions(
    var type: String = "blob",
    var write: Boolean = false,
    var path: String = ""
)

fun geetHashObject(commandLines: Array<String>) {
    val options = getHashObjectOptions(commandLines)
    createHashObject(options)
}

fun getHashObjectOptions(commandLines: Array<String>): GeetHashObjectOptions {
    val options = GeetHashObjectOptions()

    var index: Int = 1
    while (index < commandLines.size) {
        when (commandLines[index]) {
            "-t" -> {
                if (!isGeetObjectType(commandLines[index + 1])) {
                    throw BadRequest("'-t' 옵션에 대하여 올바른 개체 타입이 지정되지 않았습니다. : ${commandLines[index + 1]}")
                }

                options.type = commandLines[index + 1]
                index += 2
            }
            "-w" -> {
                options.write = true
                index += 1
            }
            else -> {
                if (options.path != "") {
                    throw BadRequest("지원하지 않는 옵션이거나 중복된 파일 경로입니다. : ${commandLines[index]}")
                }

                options.path = commandLines[index]
                index += 1
            }
        }
    }

    if (options.path == "") {
        throw BadRequest("파일 경로가 지정되지 않았습니다.")
    }

    return options
}
