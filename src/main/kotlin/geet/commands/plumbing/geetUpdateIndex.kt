package geet.commands.plumbing

import geet.exceptions.BadRequest
import geet.utils.commandutil.updateIndex

data class GeetUpdateIndexOptions(
    var option: String = "",
    var path: String = "",
)

fun geetUpdateIndex(commandLines: Array<String>) {
    val options = getUpdateIndexOptions(commandLines)
    updateIndex(options)
}

fun getUpdateIndexOptions(commandLines: Array<String>): GeetUpdateIndexOptions {
    val options = GeetUpdateIndexOptions()

    var index: Int = 1
    while (index < commandLines.size) {
        when (commandLines[index]) {
            "--add", "--remove", "--refresh" -> {
                if (options.option != "") {
                    throw BadRequest("이미 '${options.option}' 옵션이 지정되었습니다. 지정 오류 : ${commandLines[index]}")
                }

                options.option = commandLines[index]
            }
            else -> {
                if (options.path != "") {
                    throw BadRequest("지원하지 않는 옵션이거나 중복된 파일 경로입니다. : ${commandLines[index]}")
                }

                options.path = commandLines[index]
            }
        }
        index += 1
    }

    if (options.option == "") {
        throw BadRequest("옵션이 지정되지 않았습니다.")
    }

    if (options.path == "") {
        throw BadRequest("파일 경로가 지정되지 않았습니다.")
    }

    return options
}