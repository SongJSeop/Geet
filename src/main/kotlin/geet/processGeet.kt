package geet

import geet.commands.porcelain.*
import geet.commands.plumbing.*
import geet.exceptions.BadRequest
import java.io.File

fun processGeet(commandLines: Array<String>): Unit {
    if (commandLines.isEmpty() || commandLines[0] == "help") {
        guideGeet()
        return
    }

    if (commandLines[0] == "init") {
        geetInit()
        return
    }

    if (!File(".geet").exists()) {
        throw BadRequest("Geet 저장소가 초기화되지 않았습니다.\nGeet 저장소를 초기화하려면 'init'을 입력하세요.")
    }

    when (commandLines[0]) {
        "add" -> geetAdd(commandLines)
        "hash-object" -> geetHashObject(commandLines)
        "cat-file" -> geetCatFile(commandLines)
        "update-index" -> geetUpdateIndex(commandLines)
        "write-tree" -> geetWriteTree(commandLines)
        "commit-tree" -> geetCommitTree(commandLines)
        else -> throw BadRequest("지원하지 않는 명령어입니다.: ${commandLines[0]}\nGeet 명령어 목록을 확인하려면 'help'를 입력하세요.")
    }
}

fun guideGeet(): Unit {
    println()
    println("<<<Geet - Git 따라 만들면서 파악하기>>>")
    println()

    println("| 현재 사용 가능한 명령어 목록 |")
    println("|  help  |  사용 가능한 Geet 명령어 목록을 출력합니다.  |")
    println("<<< porcelain 명령어 >>>")
    println("|  init  |  현재 디렉토리에 새로운 Geet 저장소를 초기화합니다.  |")
    println("|  add  |  파일을 Staging Area에 추가합니다.  |")
    println("<<< plumbing 명령어 >>>")
    println("|  hash-object  |  파일을 해시하여 Geet 저장소에 저장합니다.  |")
    println("|  cat-file  |  Geet 저장소에 저장된 파일을 출력합니다.  |")
    println("|  update-index  |  Staging Area에 파일을 추가합니다.  |")
    println("|  write-tree  |  Staging Area에 저장된 파일을 Tree 객체로 저장합니다.  |")
    println("|  commit-tree  |  Tree 객체를 기반으로 커밋을 생성합니다.  |")
    println()

    println("----------------------------------------")
    println("깃헙 저장소 링크 - https://github.com/SongJSeop/Geet")
    println("학습 및 개발 기록 링크 - https://abyssinian-cherry-9fc.notion.site/Geet-Git-2442af8184ee48c6ae8eb5990ff7652d")
    println()
}