package geet.utils

import geet.managers.IndexManager
import java.security.MessageDigest

val messageDigest: MessageDigest = java.security.MessageDigest.getInstance("SHA-1")

val indexManager: IndexManager = IndexManager()