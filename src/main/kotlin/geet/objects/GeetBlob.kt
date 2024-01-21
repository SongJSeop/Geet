package geet.objects

class GeetBlob(
    type: String = "blob",
    content: String,
): GeetObject(type, content) { }