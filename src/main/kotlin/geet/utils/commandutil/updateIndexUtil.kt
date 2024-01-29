package geet.utils.commandutil

import geet.commands.plumbing.GeetUpdateIndexOptions
import geet.exceptions.NotFound
import geet.objects.GeetBlob
import geet.utils.indexManager
import java.io.File


fun updateIndex(updateIndexOptions: GeetUpdateIndexOptions) {
    val file = File(updateIndexOptions.path)
    if (!file.exists()) {
        throw NotFound("파일을 찾을 수 없습니다. : ${updateIndexOptions.path}")
    }

    if (file.isDirectory) {
        throw NotFound("update-index 명령어는 디렉토리를 지원하지 않습니다. : ${updateIndexOptions.path}")
    }
    val blobObject = GeetBlob(name = file.name, content = file.readText())

    when (updateIndexOptions.option) {
        "--add" -> {
            indexManager.addBlobInStagingArea(blobObject)
            indexManager.writeIndexFile()
            println("개체를 Staging Area에 추가했습니다.")
        }
        "--remove" -> {
            indexManager.removeObjectFromStagingArea(blobObject)
            indexManager.writeIndexFile()
            println("개체를 Staging Area에서 제거했습니다.")
        }
        "--refresh" -> refreshIndex()
    }
}

// TODO: refreshIndex 구현
fun refreshIndex() {
    println("개발 중인 기능입니다.")
}