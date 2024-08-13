package geet.command.porcelain

import geet.exception.BadRequest
import geet.util.const.resetColor
import geet.util.const.weekGreen
import java.io.File

data class AddOptions(
    val file: File
)

fun geetAdd(commandLines: Array<String>): Unit {
    val options = getAddOptions(commandLines)
    println(options)
}

fun getAddOptions(commandLines: Array<String>): AddOptions {
    if (commandLines.size != 2) {
        throw BadRequest("add 명령어는 파일명만을 인자로 받습니다. 사용법: ${weekGreen}geet add <file>${resetColor}")
    }

    val file = File(commandLines[1])
    if (!file.exists()) {
        throw BadRequest("해당 이름으로 된 파일이 존재하지 않습니다.: ${commandLines[1]}")
    }

    return AddOptions(file)
}