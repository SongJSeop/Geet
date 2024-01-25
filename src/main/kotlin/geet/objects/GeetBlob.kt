package geet.objects

class GeetBlob(
    name: String,
    content: String,
): GeetObject(type = "blob", name, content) { }