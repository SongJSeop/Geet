package geet.exception

class BadRequest(override val message: String): Exception(message) {
    val code = 400
}