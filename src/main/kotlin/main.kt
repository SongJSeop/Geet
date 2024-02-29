import geet.processGeet

fun main(commandLines: Array<String>): Unit {
    try {
        processGeet(commandLines)
    } catch (exception: Exception) {
        println(exception.message)
        println("\n<<< Exception 스택 추적 >>>")
        exception.stackTrace.forEach { println("- ${it}") }
    }
}
