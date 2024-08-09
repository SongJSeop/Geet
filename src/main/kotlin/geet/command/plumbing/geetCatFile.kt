package geet.command.plumbing

import geet.exception.BadRequest
import geet.util.const.resetColor
import geet.util.const.weekRed
import geet.util.getObjectContent
import geet.util.getObjectType
import geet.util.isHash

data class CatFileOptions(
    val type: Boolean = false,
    val pretty: Boolean = false,
    val objectHash: String
)

fun geetCatFile(commandLines: Array<String>): Unit {
    val options = getCatFileOptions(commandLines)

    if (options.type) {
        println(getObjectType(options.objectHash))
        return
    }

    if (options.pretty) {
        println(getObjectContent(options.objectHash))
        return
    }
}

fun getCatFileOptions(commandLines: Array<String>): CatFileOptions {
    var options = CatFileOptions(objectHash = "")
    var i = 1
    while (i < commandLines.size) {
        when (commandLines[i]) {
            "-t" -> options = options.copy(type = true)
            "-p" -> options = options.copy(pretty = true)
            else -> {
                if (isHash(commandLines[i])) {
                    if (options.objectHash.isNotEmpty()) {
                        throw BadRequest("해시값은 하나만 입력해주세요.")
                    }

                    options = options.copy(objectHash = commandLines[i])
                } else {
                    throw BadRequest("잘못된 입력입니다.: ${weekRed}${commandLines[i]}${resetColor}")
                }
            }
        }
        i++
    }

    if (options.objectHash.isEmpty()) {
        throw BadRequest("개체의 해시값을 입력해주세요.")
    }

    if (!options.type && !options.pretty) {
        throw BadRequest("-p 또는 -t 옵션을 입력해주세요.")
    }

    if (options.type && options.pretty) {
        throw BadRequest("-p와 -t 옵션은 같이 사용할 수 없습니다.")
    }

    return options
}