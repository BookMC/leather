package org.bookmc.tannery.tasks.version.download

import org.bookmc.tannery.tasks.version.data.VersionJson
import org.bookmc.tannery.tasks.version.download.side.Side
import org.bookmc.tannery.utils.fromJson
import org.gradle.workers.WorkAction
import java.io.File
import java.net.URL

abstract class DownloadLibrariesAction : WorkAction<DownloadLibrariesParameters> {
    override fun execute() {
        val dest = parameters.destinationFolder.get()
        dest.mkdirs()
        val versionJson = parameters.versionJsonFile
            .get()
            .readText()
            .fromJson<VersionJson>()


        val downloads = versionJson.downloads
        val url = when(parameters.side.get()) {
            Side.CLIENT -> downloads.client.url
            Side.SERVER -> downloads.server.url
            null -> error("Could not detect side!")
        }

        download(url, parameters.gameJarDestination.get())

        for (library in versionJson.libraries.mapNotNull { it.downloads.artifact }) {
            download(library.url, File(dest, library.path))
        }
    }

    private fun download(url: String, dest: File): File {
        if (dest.exists() && dest.length() > 0) return dest
        dest.parentFile?.mkdirs()
        dest.createNewFile()

        URL(url).let {
            it.openStream().use { `is` ->
                `is`.copyTo(dest.outputStream())
            }
        }
        return dest
    }
}