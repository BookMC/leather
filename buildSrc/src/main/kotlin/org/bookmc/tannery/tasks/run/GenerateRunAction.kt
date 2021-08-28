package org.bookmc.tannery.tasks.run

import org.bookmc.tannery.utils.createRunConfig
import org.gradle.workers.WorkAction

abstract class GenerateRunAction : WorkAction<GenerateRunParameters> {
    override fun execute() {
        createRunConfig(
            parameters.projectDir.get(),
            parameters.mainClass.get(),
            parameters.libraryFolder.get(),
            parameters.gameVersion.get(),
            parameters.gameJar.get(),
            parameters.mappingsDirectory.get(),
            parameters.projectName.get(),
            parameters.sourceSetName.get()
        )
    }
}