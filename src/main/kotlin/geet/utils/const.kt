package geet.utils

import geet.managers.IndexFileManager
import java.security.MessageDigest

val messageDigest: MessageDigest = java.security.MessageDigest.getInstance("SHA-1")

val indexFileManager: IndexFileManager = IndexFileManager()