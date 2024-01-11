package geet.exception

import java.lang.Exception

class NotFoundException(override val message: String): Exception() {
    val statusCode: Int = 404
}