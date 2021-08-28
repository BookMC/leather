package org.bookmc.tannery.tasks.version

import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.bookmc.tannery.tasks.version.data.VersionManifest
import org.bookmc.tannery.utils.VERSION_MANIFEST
import org.bookmc.tannery.utils.http
import org.gradle.workers.WorkAction

abstract class RetrieveVersionManifestAction : WorkAction<RetrieveVersionManifestParameters> {
    override fun execute() {
        val gameVersion = parameters.gameVersion.get()
        if (gameVersion.isNullOrEmpty()) error("You must specify a version!")

        val versionJson = runBlocking {
            val url = http.get<VersionManifest>(VERSION_MANIFEST)
                .versions
                .find { it.id == gameVersion }
                ?.url
                ?: error("Failed to find $gameVersion")

            http.get<String>(url)
        }


        val output = parameters.outputFile.get()

        output.parentFile?.mkdirs()
        output.createNewFile()
        output.writeText(versionJson)
    }
}