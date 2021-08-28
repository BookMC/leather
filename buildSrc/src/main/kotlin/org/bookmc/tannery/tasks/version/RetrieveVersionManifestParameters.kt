package org.bookmc.tannery.tasks.version

import org.gradle.api.provider.Property
import org.gradle.workers.WorkParameters
import java.io.File

interface RetrieveVersionManifestParameters : WorkParameters {
    val gameVersion: Property<String>
    val outputFile: Property<File>
}