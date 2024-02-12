package geet.commands.porcelain

import geet.utils.commandutil.porcelainutil.getGeetStatusResult
import geet.utils.commandutil.porcelainutil.printGeetStatus
import geet.utils.getNotIgnoreFiles
import java.io.File

fun geetStatus(commandLines: Array<String>): Unit {
    if (commandLines.size != 1) {
        throw Exception("status 명령어는 옵션을 지원하지 않습니다.")
    }

    val notIgnoreFiles = getNotIgnoreFiles(startDir = File("."))
    val geetStatusResult = getGeetStatusResult(notIgnoreFiles = notIgnoreFiles)
    printGeetStatus(geetStatusResult)
}