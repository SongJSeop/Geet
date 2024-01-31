package geet.utils

import geet.managers.IndexManager
import java.security.MessageDigest

val messageDigest: MessageDigest = java.security.MessageDigest.getInstance("SHA-1")

val indexManager: IndexManager = IndexManager()

const val GEET_DIR_PATH = "./.geet"

const val GEET_INDEX_FILE_PATH = "${GEET_DIR_PATH}/index"
const val GEET_HEAD_FILE_PATH = "${GEET_DIR_PATH}/HEAD"
const val GEET_CONFIG_FILE_PATH = "${GEET_DIR_PATH}/config"
const val GEET_DESCRIPTION_FILE_PATH = "${GEET_DIR_PATH}/description"
const val GEET_OBJECTS_DIR_PATH = "${GEET_DIR_PATH}/objects"
const val GEET_HOOKS_DIR_PATH = "${GEET_DIR_PATH}/hooks"
const val GEET_INFO_DIR_PATH = "${GEET_DIR_PATH}/info"
const val GEET_REFS_DIR_PATH = "${GEET_DIR_PATH}/refs"
const val GEET_REFS_HEADS_DIR_PATH = "${GEET_REFS_DIR_PATH}/heads"
const val GEET_REFS_TAGS_DIR_PATH = "${GEET_REFS_DIR_PATH}/tags"
const val GEET_OBJECTS_INFO_DIR_PATH = "${GEET_OBJECTS_DIR_PATH}/info"
const val GEET_OBJECTS_PACK_DIR_PATH = "${GEET_OBJECTS_DIR_PATH}/pack"

const val GEET_IGNORE_FILE_PATH = "./.gitignore"