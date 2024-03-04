package geet.manager

import geet.exception.BadRequest
import geet.exception.NotFound
import geet.geetobject.GeetBlob
import geet.util.toZlib
import java.io.File

class ObjectManager {
    
    val objectDir = File(".geet/objects")
    
    fun saveBlob(file: File): String {
        if (!file.exists()) {
            throw NotFound("파일이 존재하지 않습니다.")
        }

        if (file.isDirectory) {
            throw BadRequest("디렉토리를 Blob개체로 생성할 수 없습니다.")
        }

        val blob = GeetBlob(content = file.readText(), filePath = file.name)
        
        val blobDir = File(objectDir, blob.hashString.substring(0, 2))
        val blobFile = File(blobDir, blob.hashString.substring(2))

        if (!blobDir.exists()) {
            blobDir.mkdirs()
        }

        blobFile.writeText(blob.content.toZlib())
        return blob.hashString
    }
}