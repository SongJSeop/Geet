package geet.command.porcelain

import geet.exception.BadRequest
import geet.geetobject.GeetBlob
import geet.geetobject.StagingObject
import geet.util.*
import geet.util.const.resetColor
import geet.util.const.weekGreen
import geet.util.const.weekRed
import java.io.File

data class AddOptions(
    val file: File
)

fun geetAdd(commandLines: Array<String>): Unit {
    val options = getAddOptions(commandLines)
    val targetFile = options.file
    if (!targetFile.exists()) {
        throw BadRequest("해당 파일은 존재하지 않는 파일입니다.: ${weekRed}${targetFile.name}${resetColor}")
    }

    val ignoredFileName = isIgnored(targetFile)
    if (ignoredFileName != null) {
        throw BadRequest("해당 파일은 .geetignore 안의 파일 중 하나 때문에 무시합니다.: ${weekRed}${ignoredFileName}${resetColor}")
    }

    when {
        options.file.isFile -> {
            val filePath = getRelativePath(toFile = targetFile)
            val geetBlob = GeetBlob(content = targetFile.readText())
            val stagingObject = getSameStagingObject(filePath)?.let { stagingObject ->
                stagingObject.copy(hash = geetBlob.hash)
            } ?: StagingObject(
                fileName = targetFile.name,
                filePath = filePath,
                hash = geetBlob.hash,
                status = "new",  // TODO: 지난 커밋과 비교하여 상태를 결정해야 함
                slot = 0
            )

            addStagingObject(stagingObject)
        }
        else -> {
            // TODO: 디렉토리인 경우 구현
        }
    }
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