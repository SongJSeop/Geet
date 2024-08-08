package geet.command.plumbing

fun geetHashObject(commandLines: Array<String>): Unit {
    println("hash-object 명령어를 실행합니다.")
    commandLines.forEach { println(it) }
}