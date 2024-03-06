package geet.command

data class StatusResult(
    val stagedNewFiles: MutableSet<String> = mutableSetOf(),
    val stagedModifiedFiles: MutableSet<String> = mutableSetOf(),
    val stagedDeletedFiles: MutableSet<String> = mutableSetOf(),
    val unstagedModifiedFiles: MutableSet<String> = mutableSetOf(),
    val unstagedDeletedFiles: MutableSet<String> = mutableSetOf(),
    val untrackedFiles: MutableSet<String> = mutableSetOf(),
)

fun geetStatus(commandLines: Array<String>): Unit {
    // stage - new
    // 최근 커밋에 없고, 스테이지엔 있음(NEW)

    // stage - modified
    // 최근 커밋에 있고, 스테이지에도 있음(삭제 제외) / 최근 커밋에 없고, 스테이지에도 있고, 스테이지와 작업 디렉토리 해시값 다름

    // stage - deleted
    // 최근 커밋에 있고, 스테이지에도 있음(삭제)

    // unstage - modified
    // 최근 커밋에 있고, 스테이지엔 없고, 최근 커밋과 작업 디렉토리 해시값 다름 / 최근 커밋 상관 없음, 스테이지에 있고, 스테이지와 작업 디렉토리 해시값 다름

    // unstage - deleted
    // 최근 커밋에 있고, 스테이지엔 없으며, 작업 디렉토리에서 파일이 삭제됨

    // untracked
    // 최근 커밋에 없고, 스테이지에도 없고, 작업 디렉토리엔 존재
}