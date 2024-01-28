package geet.commands.porcelain

import geet.exceptions.BadRequest
import java.io.File

fun geetInit(): Unit {
    if (File(".geet").exists()) {
        throw BadRequest("현재 디렉토리는 이미 Geet 저장소로 초기화되었습니다.")
    }

    File(".geet").mkdir()
    File(".geet/HEAD").writeText("ref: refs/heads/master")
    File(".geet/config").writeText("""
        [core]
            repositoryformatversion = 0
            filemode = true
            bare = false
            logallrefupdates = true
            ignorecase = true
            precomposeunicode = true
    """.trimIndent())
    File(".geet/description").writeText("Unnamed geet repository; edit this file 'description' to name the repository.")
    File(".geet/hooks").mkdir()
    File(".geet/info").mkdir()
    File(".geet/objects").mkdir()
    File(".geet/objects/info").mkdir()
    File(".geet/objects/pack").mkdir()
    File(".geet/refs").mkdir()
    File(".geet/refs/heads").mkdir()
    File(".geet/refs/tags").mkdir()
    println("Geet 저장소를 초기화했습니다.")
}