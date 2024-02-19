package geet.utils.commandutil.porcelainutil

import geet.commands.porcelain.GeetSwitchOptions
import geet.exceptions.NotFound
import geet.utils.GEET_HEAD_FILE_PATH
import geet.utils.indexManager
import java.io.File

fun switch(geetSwitchOptions: GeetSwitchOptions) {
    if (geetSwitchOptions.create) {
        createBranch(geetSwitchOptions.branchName)
    }
    changeBranch(geetSwitchOptions.branchName)
}

fun changeBranch(branchName: String) {
    println("브랜치를 변경합니다. : $branchName")
}
