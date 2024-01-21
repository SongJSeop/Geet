package geet.commands.plumbing

import geet.exception.BadRequestException
import geet.util.commandutil.catGeetObject

data class GeetCatFileOptions(
    var option: String = "",
    var objectPath: String = ""
)

fun geetCatFile(commandLines: Array<String>): Unit {
    val options: GeetCatFileOptions = getCatFileOptions(commandLines)
    catGeetObject(options)
}

fun getCatFileOptions(commandLines: Array<String>): GeetCatFileOptions {
    val options = GeetCatFileOptions()

    var index: Int = 1
    while (index < commandLines.size) {
        when (commandLines[index]) {
            "-p", "-t", "-s" -> {
                if (options.option != "") {
                    throw BadRequestException("이미 '${options.option}' 옵션이 지정되었습니다. 지정 오류 : ${commandLines[index]}")
                }

                options.option = commandLines[index]
            }
            else -> {
                if (options.objectPath != "") {
                    throw BadRequestException("지원하지 않는 옵션이거나 중복된 객체 SHA-1 값입니다. : ${commandLines[index]}")
                }

                options.objectPath = commandLines[index]
            }
        }
        index += 1
    }

    if (options.objectPath == "") {
        throw BadRequestException("객체 SHA-1 값이 지정되지 않았습니다.")
    }

    if (options.option == "") {
        throw BadRequestException("'-p', '-t', '-s' 옵션 중 하나 이상이 지정되지 않았습니다.")
    }

    return options
}