package org.bookmc.leather.data

data class VersionManifest(val versions: List<Version>) {
    data class Version(val id: String, val url: String)
}