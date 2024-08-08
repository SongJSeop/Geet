import geet.processGeet
import geet.util.const.red
import geet.util.const.resetColor

fun main(commandLines: Array<String>): Unit {
    try {
        processGeet(commandLines)
    } catch (exception: Exception) {
        println("${red}[ERROR]${resetColor} ${exception.message}")
        println("\n<<< Exception 스택 추적 >>>")
        exception.stackTrace.forEach { println("- ${it}") }
    }
}
