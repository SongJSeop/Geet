package geet.commands.plumbing

import geet.exception.BadRequestException

data class GeetCatFileOptions(
    var pretty: Boolean = false,
    var printType: Boolean = false,
    var printSize: Boolean = false,
    var objectPath: String = ""
)

fun geetCatFile(commandLines: Array<String>): Unit {
    val options = getCatFileOptions(commandLines)
    println(options)
}

fun getCatFileOptions(commandLines: Array<String>): GeetCatFileOptions {
    val options = GeetCatFileOptions()

    var index: Int = 1
    while (index < commandLines.size) {
        when (commandLines[index]) {
            "-p" -> {
                options.pretty = true
                index += 1
            }
            "-t" -> {
                options.printType = true
                index += 1
            }
            "-s" -> {
                options.printSize = true
                index += 1
            }

            else -> {
                if (options.objectPath != "") {
                    throw BadRequestException("지원하지 않는 옵션이거나 중복된 객체 SHA-1 값입니다. : ${commandLines[index]}")
                }

                options.objectPath = commandLines[index]
                index += 1
            }
        }
    }

    return options
}