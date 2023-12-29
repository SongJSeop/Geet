import parser.CommandLineParser

val commandLineParser = CommandLineParser()

fun main(args: Array<String>) {
    val parseData = commandLineParser.parse(args)
    println(parseData)
}
