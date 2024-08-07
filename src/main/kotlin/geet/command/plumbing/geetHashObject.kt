package geet.command.plumbing

import geet.enums.GeetObjectType
import geet.exception.BadRequest
import geet.geetobject.GeetBlob
import geet.util.const.resetColor
import geet.util.const.weekRed
import geet.util.saveObject
import java.io.File

data class HashObjectOptions(
    val write: Boolean = false,
    val stdin: Boolean = false,
    val path: String? = null,
    val objectType: GeetObjectType = GeetObjectType.BLOB,
)

fun geetHashObject(commandLines: Array<String>): Unit {
    val options = getHashObjectOptions(commandLines)

    val content = when {
        options.stdin -> getStdinContent()
        options.path != null -> getPathContent(options.path)
        else -> null
    }

    if (content != null) {
        val geetObject = when (options.objectType) {
            GeetObjectType.BLOB -> GeetBlob(content)
            GeetObjectType.TREE -> {
                // TODO
                println("hash-object로 Tree 객체 생성은 구현 중입니다.")
                return
            }
            GeetObjectType.COMMIT -> {
                // TODO
                println("hash-object로 Commit 객체 생성은 구현 중입니다.")
                return
            }
        }

        println(geetObject.hash)

        if (options.write) {
            saveObject(geetObject.hash, content)
        }
    }
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
                    ?: throw BadRequest("지원하지 않는 객체 타입입니다.: ${weekRed}${type}${resetColor}")
                options = options.copy(objectType = objectType)
                i++
            }
            else -> throw BadRequest("지원하지 않는 옵션입니다.: ${weekRed}${commandLines[i]}${resetColor}")
        }
        i++
    }

    if (options.stdin && options.path != null) {
        throw BadRequest("옵션 --stdin과 -p는 함께 사용할 수 없습니다.")
    }

    return options
}

fun getStdinContent(): String {
    val content = generateSequence { readlnOrNull() }.joinToString("\n")
    return content
}

fun getPathContent(path: String): String {
    val file = File(path)

    if (!file.exists() || !file.isFile) {
        throw BadRequest("파일이 아닙니다.: ${weekRed}${path}${resetColor}")
    }

    return file.readText()
}