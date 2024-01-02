package geet.commands

import java.io.File

fun geetInit(): Unit {
    if (File(".geet").exists()) {
        println("이미 Geet 저장소가 초기화되어 있습니다.")
        return
    }

    File(".geet").mkdir()
    println("Geet 저장소를 초기화했습니다.")
}