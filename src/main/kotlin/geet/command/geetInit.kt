package geet.command

import geet.exception.BadRequest
import geet.util.const.red
import geet.util.const.resetColor
import java.io.File

fun geetInit(commandLines: Array<String>): Unit {
    if (commandLines.size != 1) {
        throw BadRequest(
            "init 명령어는 다른 옵션을 받지 않습니다.: " +
                    "${red}${commandLines.toList().subList(1, commandLines.size).joinToString(" ")}${resetColor}"
        )
    }

    val geetFile = File(".geet")
    if (geetFile.exists()) {
        throw BadRequest("이미 초기화된 Git 저장소 입니다.")
    }

    initGeetRepository(geetFile)
    println("현재 디렉토리를 Git 저장소로 초기화합니다.")

}

fun initGeetRepository(geetFile: File): Unit {
    geetFile.mkdir()
    File(geetFile, "objects").mkdir()
    File(geetFile, "objects/info").mkdir()
    File(geetFile, "objects/pack").mkdir()
    File(geetFile, "refs").mkdir()
    File(geetFile, "refs/heads").mkdir()
    File(geetFile, "refs/tags").mkdir()
    File(geetFile, "HEAD").writeText("ref: refs/heads/master")
    File(geetFile, "config").writeText(
        """
        [core]
            repositoryformatversion = 0
            filemode = true
            bare = false
            logallrefupdates = true
        """.trimIndent()
    )
    File(geetFile, "description").writeText("Unnamed repository; edit this file 'description' to name the repository.")
    File(geetFile, "hooks").mkdir()
    File(geetFile, "info").mkdir()
    File(geetFile, "info/exclude").writeText("# *.[oa]\n# *~")
}