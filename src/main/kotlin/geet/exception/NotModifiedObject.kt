package geet.exception

class NotModifiedObject(
    override val message: String
): Exception() {
    val statusCode: Int = 324
}