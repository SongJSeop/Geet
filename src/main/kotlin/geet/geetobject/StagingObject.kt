package geet.geetobject

import kotlinx.serialization.Serializable

@Serializable
data class StagingObject(
    val fileName: String,
    val filePath: String,
    val hash: String,
    val status: String,
    val slot: Int
)
