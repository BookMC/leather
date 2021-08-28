package org.bookmc.tannery.tasks.export

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.workers.WorkerExecutor
import java.io.File
import javax.inject.Inject

abstract class MappingsExportTask : DefaultTask() {
    @InputDirectory
    var mappingsDirectory: File? = null

    @OutputDirectory
    var outputDirectory: File? = null

    @Inject
    abstract fun getWorkerExecutor(): WorkerExecutor

    @TaskAction
    fun run() {
        getWorkerExecutor().noIsolation().submit(MappingsExportAction::class.java) {
            it.mappingsFolder.set(mappingsDirectory)
            it.outputDirectory.set(outputDirectory)
        }
    }
}