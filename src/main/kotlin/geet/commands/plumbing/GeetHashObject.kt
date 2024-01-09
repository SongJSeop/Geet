package geet.commands.plumbing

import geet.util.isGeetObjectType
import java.io.File
import java.security.MessageDigest

val messageDigest: MessageDigest = MessageDigest.getInstance("SHA-1")

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
                if (options.path != "") {
                    println("지정할 수 없는 옵션입니다.: ${commandLines[index]}")
                    // TODO: 에러 처리
                }

                options.path = commandLines[index]
                index += 1
            }
        }
    }

    if (options.path == "") {
        println("파일 경로가 지정되지 않았습니다.")
        // TODO: 에러 처리
    }

    return options
}

fun createHashObject(options: GeetHashObjectOptions) {
    val file = File(options.path)
    if (!file.exists()) {
        println("파일이 존재하지 않습니다.: ${options.path}")
    }

    val content = file.readText()
    val header = "${options.type} ${content.length}\u0000"
    val store = header + content

    val hash = messageDigest.digest(store.toByteArray())
    val hashString = hash.joinToString("") {
        String.format("%02x", it)
    }
    println(hashString)
}