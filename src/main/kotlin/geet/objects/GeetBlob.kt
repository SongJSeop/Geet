package geet.objects

import geet.utils.GeetObjectType
import geet.utils.GeetObjectType.*

class GeetBlob(
    path: String,
    content: String,
): GeetObject(type = BLOB, path = path, content = content) { }