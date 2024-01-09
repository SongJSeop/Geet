package geet

import geet.commands.porcelain.geetInit

fun processGeet(parseData: Map<String, String>): Unit {
    when (parseData["command"]) {
        "init" -> geetInit()
        null -> guideGeet()
        else -> println("'geet ${parseData["command"]}'은 지원하는 명령어가 아닙니다.")
    }
}

fun guideGeet(): Unit {
    println("<<<Geet - Git 따라 만들면서 파악하기>>>")
    println()

    println("현재 사용 가능한 명령어 목록")
    println("   init     현재 디렉토리에 새로운 Geet 저장소를 초기화합니다.")
    println()

    println("----------------------------------------")
    println("깃헙 저장소 링크 - https://github.com/SongJSeop/Geet")
    println("학습 및 개발 기록 링크 - https://abyssinian-cherry-9fc.notion.site/Geet-Git-2442af8184ee48c6ae8eb5990ff7652d")
}