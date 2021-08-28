package org.bookmc.tannery.tasks.export

import cuchaz.enigma.command.MappingCommandsUtil.read
import cuchaz.enigma.command.MappingCommandsUtil.write
import cuchaz.enigma.translation.mapping.serde.MappingFileNameFormat
import cuchaz.enigma.translation.mapping.serde.MappingSaveParameters
import org.gradle.workers.WorkAction
import java.io.File

abstract class MappingsExportAction : WorkAction<MappingsExportParameters> {
    override fun execute() {
        val mappingsFolder = parameters.mappingsFolder.get().toPath()
        val outputDirectory = parameters.outputDirectory.get()

        val outputFile = File(outputDirectory, "mappings/mapping.tiny")
        outputFile.delete()
        outputFile.parentFile?.mkdirs()
        outputFile.createNewFile()

        val params = MappingSaveParameters(MappingFileNameFormat.BY_DEOBF)
        val mappings = read("enigma", mappingsFolder, params)
        write(mappings, "tiny:obfuscated:named", outputFile.toPath(), params)
    }
}