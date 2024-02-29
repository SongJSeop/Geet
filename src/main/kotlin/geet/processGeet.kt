package geet

import geet.command.geetHelp

fun processGeet(commandLines: Array<String>): Unit {
    if (commandLines.isEmpty() || commandLines[0] == "help") {
        geetHelp()
    }
}