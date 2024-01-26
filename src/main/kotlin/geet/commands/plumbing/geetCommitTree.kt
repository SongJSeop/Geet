package geet.commands.plumbing

import geet.exception.BadRequest

data class GeetCommitTreeOptions(
    var tree: String = "",
    var parent: String = "",
    var message: String = ""
)

fun geetCommitTree(commandLines: Array<String>): Unit {
    val options = getCommitTreeOptions(commandLines)
    println(options)
}

fun getCommitTreeOptions(commandLines: Array<String>): GeetCommitTreeOptions {
    val options = GeetCommitTreeOptions()

    var index: Int = 1
    while (index < commandLines.size) {
        when (commandLines[index]) {
            "-m" -> {
                if (options.message != "") {
                    throw BadRequest("커밋 메세지가 설정 요청이 중복되었습니다. : ${commandLines[index]} ${commandLines[index + 1]}")
                }

                options.message = commandLines[index + 1]
                index += 2
            }
            "-p" -> {
                if (options.parent != "") {
                    throw BadRequest("부모 커밋 설정 요청이 중복되었습니다. : ${commandLines[index]} ${commandLines[index + 1]}")
                }

                options.parent = commandLines[index + 1]
                index += 2
            }
            else -> {
                if (options.tree != "") {
                    throw BadRequest("'-m', '-p' 옵션 외의 지원하지 않는 옵션이거나 중복된 Tree 객체 SHA-1 값입니다. : ${commandLines[index]}")
                }

                options.tree = commandLines[index]
                index += 1
            }
        }
    }

    if (options.tree == "") {
        throw BadRequest("Tree 객체 SHA-1 값이 지정되지 않았습니다.")
    }

    if (options.message == "") {
        throw BadRequest("커밋 메시지가 지정되지 않았습니다.")
    }

    // TODO: 부모 커밋이 존재하지 않는 경우 예외 처리
//    if (options.parent != "" && !isCommitObject(options.parent)) {
//        throw BadRequest("부모 커밋이 존재하지 않습니다. : ${options.parent}")
//    }
    return options
}