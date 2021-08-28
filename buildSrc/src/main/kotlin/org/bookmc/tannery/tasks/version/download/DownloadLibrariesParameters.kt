package org.bookmc.tannery.tasks.version.download

import org.bookmc.tannery.tasks.version.download.side.Side
import org.gradle.api.provider.Property
import org.gradle.workers.WorkParameters
import java.io.File

interface DownloadLibrariesParameters : WorkParameters {
    val versionJsonFile: Property<File>
    val destinationFolder: Property<File>
    val gameVersion: Property<String>
    val gameJarDestination: Property<File>
    val side: Property<Side>
}