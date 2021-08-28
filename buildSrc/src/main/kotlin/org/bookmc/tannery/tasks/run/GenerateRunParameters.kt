package org.bookmc.tannery.tasks.run

import org.gradle.api.provider.Property
import org.gradle.workers.WorkParameters
import java.io.File

interface GenerateRunParameters : WorkParameters {
    val mainClass: Property<String>
    val libraryFolder: Property<File>
    val gameVersion: Property<String>
    val projectDir: Property<File>
    val gameJar: Property<File>
    val mappingsDirectory: Property<File>
    val projectName: Property<String>
    val sourceSetName: Property<String>
}