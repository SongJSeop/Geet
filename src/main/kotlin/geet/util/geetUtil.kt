package geet.util

import java.io.File

fun isGeetDirectory(): Boolean {
    val geetDir = File(".geet")
    val geetObjectDir = File(geetDir, "objects")
    val geetRefsDir = File(geetDir, "refs")
    val geetHeadFile = File(geetDir, "HEAD")
    return geetDir.exists() && geetObjectDir.exists() && geetRefsDir.exists() && geetHeadFile.exists()
}