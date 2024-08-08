package geet.command.plumbing

import geet.enums.GeetObjectType
import geet.exception.BadRequest

data class HashObjectOptions(
    val write: Boolean = false,
    val stdin: Boolean = false,
    val path: String? = null,
    val objectType: GeetObjectType = GeetObjectType.BLOB,
)

fun geetHashObject(commandLines: Array<String>): Unit {
    val options = getHashObjectOptions(commandLines)
    println(options)
}

fun getHashObjectOptions(commandLines: Array<String>): HashObjectOptions {
    var options = HashObjectOptions()
    var i = 1
    while (i < commandLines.size) {
        when (commandLines[i]) {
            "-w" -> options = options.copy(write = true)
            "--stdin" -> options = options.copy(stdin = true)
            "-p" -> {
                val path = commandLines.getOrNull(i + 1) ?: throw BadRequest("옵션 -p는 파일 경로를 인자로 받습니다.")
                options = options.copy(path = path)
                i++
            }
            "-t" -> {
                val type = commandLines.getOrNull(i + 1) ?: throw BadRequest("옵션 -t는 객체 타입을 인자로 받습니다.")
                val objectType = GeetObjectType.entries.find { it.value == type }
                    ?: throw BadRequest("지원하지 않는 객체 타입입니다.: $type")
                options = options.copy(objectType = objectType)
                i++
            }
            else -> throw BadRequest("지원하지 않는 옵션입니다.: ${commandLines[i]}")
        }
        i++
    }

    if (options.stdin && options.path != null) {
        throw BadRequest("옵션 --stdin과 -p는 함께 사용할 수 없습니다.")
    }

    return options
}