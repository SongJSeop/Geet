import geet.processGeet

fun main(commandLines: Array<String>) {
    try {
        processGeet(commandLines)
    } catch (exception: Exception) {
        println(exception.message)
    }
}
