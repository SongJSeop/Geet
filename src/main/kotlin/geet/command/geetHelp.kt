package geet.command

import geet.util.const.cyan
import geet.util.const.green
import geet.util.const.resetColor
import geet.util.const.yellow

val supportingCommandsList = mapOf<String, String>(
    "help" to "Geet에 대한 설명 및 지원하는 명령어 목록을 출력합니다.",
    "init" to "새로운 Git 저장소를 초기화합니다."
)

fun geetHelp(): Unit {
    printGeetGreeting()
    println()

    printSupportingCommands()
    println()

    println("----------------------------------------")
    printRecordLink()
}

fun printGeetGreeting(): Unit {
    println("${cyan}** Geet - 직접 만들면서 배우는 Git **${resetColor}")
    println("Git을 사용하면서 무작정 push -f를 하기도 하고, 충돌이 발생하면 당황하기도 합니다.")
    println("그래서 Git을 제대로 학습해보고 싶어서 시도해본 프로젝트 입니다.")
    println("최대한 Git의 내부 로직, 동작 방식과 비슷하게 만들 것입니다.")
}

fun printSupportingCommands(): Unit {
    println("${green}<< 지원하는 명령어 목록 >>${resetColor}")
    supportingCommandsList.forEach { (command, description) ->
        println("- ${yellow}${command}${resetColor} : ${description}")
    }
}

fun printRecordLink(): Unit {
    println("GitHub 주소 - https://github.com/SongJSeop/Geet")
    println("개발 기록 및 학습 기록 - https://abyssinian-cherry-9fc.notion.site/Geet-Git-2442af8184ee48c6ae8eb5990ff7652d?pvs=4")
}