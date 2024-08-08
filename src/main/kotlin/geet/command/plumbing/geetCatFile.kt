package geet.command.plumbing

fun geetCatFile(commandLines: Array<String>): Unit {
    println("cat-file")
    commandLines.forEach { println(it) }
}