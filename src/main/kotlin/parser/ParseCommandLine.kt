package parser

fun parseCommandLine(args: Array<String>): Map<String, String> {
    if (args.isEmpty()) {
        return mapOf()
    }

    val parseData = mutableMapOf<String, String>()
    parseData["command"] = args[0]
    return parseData
}