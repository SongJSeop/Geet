package geet.exception

class NotFound(override val message: String) : Exception(message) {
    val code = 404
}