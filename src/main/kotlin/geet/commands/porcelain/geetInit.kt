package geet.commands.porcelain

import geet.exceptions.BadRequest
import geet.utils.*
import java.io.File

fun geetInit(): Unit {
    if (File(GEET_DIR_PATH).exists()) {
        throw BadRequest("현재 디렉토리는 이미 Geet 저장소로 초기화되었습니다.")
    }

    File(GEET_DIR_PATH).mkdir()
    File(GEET_HEAD_FILE_PATH).writeText("ref: refs/heads/master")
    File(GEET_CONFIG_FILE_PATH).writeText("""
        [core]
            repositoryformatversion = 0
            filemode = true
            bare = false
            logallrefupdates = true
            ignorecase = true
            precomposeunicode = true
    """.trimIndent())
    File(GEET_DESCRIPTION_FILE_PATH).writeText("Unnamed geet repository; edit this file 'description' to name the repository.")
    File(GEET_HOOKS_DIR_PATH).mkdir()
    File(GEET_INFO_DIR_PATH).mkdir()
    File(GEET_OBJECTS_DIR_PATH).mkdir()
    File(GEET_OBJECTS_INFO_DIR_PATH).mkdir()
    File(GEET_OBJECTS_PACK_DIR_PATH).mkdir()
    File(GEET_REFS_DIR_PATH).mkdir()
    File(GEET_REFS_HEADS_DIR_PATH).mkdir()
    File(GEET_REFS_TAGS_DIR_PATH).mkdir()
    println("Geet 저장소를 초기화했습니다.")
}