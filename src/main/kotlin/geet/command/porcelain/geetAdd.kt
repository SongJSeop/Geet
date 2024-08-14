package geet.command.porcelain

import geet.exception.BadRequest
import geet.util.const.resetColor
import geet.util.const.weekGreen
import geet.util.const.weekRed
import geet.util.isIgnored
import java.io.File

data class AddOptions(
    val file: File
)

fun geetAdd(commandLines: Array<String>): Unit {
    val options = getAddOptions(commandLines)
    if (!options.file.exists()) {
        throw BadRequest("해당 파일은 존재하지 않는 파일입니다.: ${weekRed}${options.file}${resetColor}")
    }

    val ignoredFileName = isIgnored(options.file)
    if (ignoredFileName != null) {
        throw BadRequest("해당 파일은 .geetignore 안의 파일 중 하나 때문에 무시합니다.: ${weekRed}${ignoredFileName}${resetColor}")
    }

    println("파일 추가 완료: ${weekGreen}${options.file}${resetColor}")
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