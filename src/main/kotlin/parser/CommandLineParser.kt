package parser

fun parseCommandLine(args: Array<String>): Map<String, String> {
    if (args.isEmpty()) {
        println("명령어와 함께 입력해주세요!")
        return mapOf()
    }

    val parseData = mutableMapOf<String, String>()
    parseData["command"] = args[0]
    return parseData
}