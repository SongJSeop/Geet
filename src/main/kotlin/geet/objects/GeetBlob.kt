package geet.objects

class GeetBlob(
    type: String = "blob",
    name: String,
    content: String,
): GeetObject(type, name, content) { }