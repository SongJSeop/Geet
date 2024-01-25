package geet.exception

import java.lang.Exception

class NotFound(override val message: String): Exception() {
    val statusCode: Int = 404
}