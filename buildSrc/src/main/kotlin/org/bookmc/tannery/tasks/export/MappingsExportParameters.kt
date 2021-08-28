package org.bookmc.tannery.tasks.export

import org.gradle.api.provider.Property
import org.gradle.workers.WorkParameters
import java.io.File

interface MappingsExportParameters : WorkParameters {
    val outputDirectory: Property<File>
    val mappingsFolder: Property<File>
}