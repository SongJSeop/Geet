package geet.exception

class BadRequest(override val message: String): Exception() {
    val statusCode: Int = 400
}