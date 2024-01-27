package geet.commands.porcelain

import geet.exception.BadRequest
import java.io.File

fun geetAdd(commandLines: Array<String>): Unit {
    if (commandLines.size < 2) {
        throw BadRequest("파일 경로가 지정되지 않았습니다.")
    }

    if (commandLines.size > 2) {
        throw BadRequest("지원하지 않는 옵션이거나 중복된 파일 경로입니다. : ${commandLines[2]}")
    }

    val file = File(commandLines[1])
    if (!file.exists()) {
        throw BadRequest("파일을 찾을 수 없습니다. : ${commandLines[1]}")
    }
}