package geet.command

import geet.exception.BadRequest
import geet.exception.NotFound
import geet.util.const.*

data class BranchCommandOptions(
    val branchName: String? = null,
    val delete: Boolean = false,
)

fun geetBranch(commandLines: Array<String>): Unit {
    val options = getBranchCommandOptions(commandLines)

    if (options.branchName == null && options.delete) {
        throw BadRequest("삭제하기 위한 브랜치 이름이 없습니다.")
    }

    if (options.branchName == null) {
        printBranchList()
        return
    }

    if (options.delete) {
        branchManager.deleteBranch(options.branchName)
        return
    }

    try {
        branchManager.createBranch(options.branchName)
    } catch (exception: NotFound) {
        throw BadRequest("첫 커밋을 하기 전에는 브랜치를 생성할 수 없습니다.")
    }
}

fun printBranchList(): Unit {
    val headBranchName = headManager.getHeadBranchName()
    branchManager.getAllBranchNames().forEach {
        if (it == headBranchName) {
            println("${yellow}${it} *${resetColor}")
        } else {
            println(it)
        }
    }
}

fun getBranchCommandOptions(commandLines: Array<String>): BranchCommandOptions {
    if (commandLines.size == 1) {
        return BranchCommandOptions()
    }

    if (commandLines.size == 2) {
        return BranchCommandOptions(branchName = commandLines[1])
    }

    if (commandLines.size == 3 && (commandLines[1] == "-d" || commandLines[2] == "-d")) {
        val branchName = if (commandLines[1] == "-d") commandLines[2] else commandLines[1]
        return BranchCommandOptions(branchName = branchName, delete = true)
    }

    throw BadRequest("branch 명령에 대하여 잘못된 입력입니다.: ${red}${commandLines.joinToString(" ")}${resetColor}")
}