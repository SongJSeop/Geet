package geet.exception

class BadRequestException(override val message: String): Exception() {
    val statusCode: Int = 400
}