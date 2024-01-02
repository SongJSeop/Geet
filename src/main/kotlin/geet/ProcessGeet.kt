package geet

fun processGeet(parseData: Map<String, String>): Unit {
    when (parseData["command"]) {
        "init" -> println("init")
        null -> println("geet 설명")
        else -> println("geet ${parseData["command"]} is not a valid command. Try init instead.")
    }
}