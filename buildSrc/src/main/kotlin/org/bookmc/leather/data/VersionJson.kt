package org.bookmc.leather.data

data class VersionJson(val downloads: Downloads) {
    data class Downloads(val client: Client) {
        data class Client(val url: String)
    }
}