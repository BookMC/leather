package org.bookmc.leather.tasks

import cuchaz.enigma.command.MappingCommandsUtil
import cuchaz.enigma.translation.mapping.serde.MappingFileNameFormat
import cuchaz.enigma.translation.mapping.serde.MappingSaveParameters
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class ExportMappingsTask : DefaultTask() {
    @get:Input
    abstract val mappings: Property<String>

    @get:Input
    abstract val exportedFile: Property<String>

    @TaskAction
    fun run() {
        val mappingsFolder = File(project.projectDir, mappings.get()).toPath()

        val export = File(project.projectDir, exportedFile.get())
        export.delete()
        export.parentFile?.mkdirs()
        export.createNewFile()

        val params = MappingSaveParameters(MappingFileNameFormat.BY_DEOBF)
        val mappings = MappingCommandsUtil.read("enigma", mappingsFolder, params)
        MappingCommandsUtil.write(mappings, "tiny:from_column:to_column", export.toPath(), params)
    }
}