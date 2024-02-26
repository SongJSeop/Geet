package geet.utils.commandutil.porcelainutil

import geet.commands.porcelain.GeetMergeOptions

fun merge(geetMergeOptions: GeetMergeOptions): Unit {
    if (geetMergeOptions.branchName != null) {
        println("병합할 브랜치: ${geetMergeOptions.branchName}")
    }

    when (geetMergeOptions.option) {
        "abort" -> println("병합을 중단합니다.")
        "continue" -> println("병합을 계속합니다.")
    }
}