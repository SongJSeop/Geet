import parser.parseCommandLine

fun main(args: Array<String>) {
    val parseData = parseCommandLine(args)
    println(parseData)
}
