package geet.utils.commandutil.porcelainutil

import geet.commands.porcelain.GeetSwitchOptions
import geet.exceptions.NotFound
import geet.objects.GeetBlob
import geet.utils.GEET_HEAD_FILE_PATH
import geet.utils.getObjectsFromCommit
import geet.utils.indexManager
import java.io.File

fun switch(geetSwitchOptions: GeetSwitchOptions) {
    if (geetSwitchOptions.create) {
        createBranch(geetSwitchOptions.branchName)
    }
    changeBranch(geetSwitchOptions.branchName)
}

fun changeBranch(branchName: String) {
    val headFile = File(GEET_HEAD_FILE_PATH)
    if (!headFile.exists()) {
        throw NotFound("HEAD 파일을 찾을 수 없습니다.\ninit 명령어를 통해 저장소를 초기화하세요.")
    }

    val branchFile = File(".geet/refs/heads/$branchName")
    if (!branchFile.exists()) {
        throw NotFound("브랜치를 찾을 수 없습니다. : ${branchName}")
    }

    headFile.writeText("ref: refs/heads/$branchName")
    val branchCommitHash = getCurrentRefCommitHash()
    indexManager.getIndexFileData().lastCommitHash = branchCommitHash
    indexManager.writeIndexFile()

    val branchObjects = getObjectsFromCommit(branchCommitHash)
    clearWorkingDirectory()
    branchObjects.forEach { restoreObject(it as GeetBlob) }
}
