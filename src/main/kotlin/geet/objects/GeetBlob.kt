package geet.objects

class GeetBlob(
    path: String,
    content: String,
): GeetObject(type = "blob", path = path, content = content) { }