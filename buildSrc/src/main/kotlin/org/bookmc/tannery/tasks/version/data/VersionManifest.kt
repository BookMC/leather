package org.bookmc.tannery.tasks.version.data

data class VersionManifest(val versions: List<Version>) {
    data class Version(val id: String, val url: String)
}
