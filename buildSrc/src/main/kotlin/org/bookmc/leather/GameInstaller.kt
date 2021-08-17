package org.bookmc.leather

import org.bookmc.leather.data.VersionJson
import org.bookmc.leather.data.VersionManifest
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL

object GameInstaller {
    private const val VERSION_MANIFEST = "https://launchermeta.mojang.com/mc/game/version_manifest_v2.json"

    private val http = HttpClient(Apache) {
        Json {
            serializer = GsonSerializer()
        }
    }

    suspend fun download(version: String, dest: File) {
        try {
            val download = withContext(Dispatchers.IO) {
                URL(
                    http.get<VersionJson>(
                        http.get<VersionManifest>(VERSION_MANIFEST)
                            .versions
                            .find { it.id == version }
                            ?.url
                            ?: error("Failed to find game version ($version)")
                    ).downloads.client.url
                )
            }

            dest.mkdirs()
            val jarDest = File(dest, "$version.jar")

            if (jarDest.exists()) {
                jarDest.delete()
            }

            withContext(Dispatchers.IO) {
                download.openStream().use {
                    jarDest.outputStream().use { os ->
                        it.copyTo(os)
                    }
                }
                println("Successfully downloaded $version")
            }
        } catch (t: Throwable) {
            println("Failed to download $version")
            t.printStackTrace()
        }
    }
}