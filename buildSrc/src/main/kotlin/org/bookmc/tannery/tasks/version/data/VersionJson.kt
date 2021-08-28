package org.bookmc.tannery.tasks.version.data

data class VersionJson(val id: String, val libraries: List<Library>, val downloads: Downloads) {
    data class Downloads(val client: Download, val server: Download) {
        data class Download(val url: String)
    }

    data class Library(val downloads: Downloads) {
        data class Downloads(val artifact: Artifact?) {
            data class Artifact(val path: String, val url: String)
        }
    }
}
