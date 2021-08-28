package org.bookmc.tannery.tasks.version

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import org.gradle.workers.WorkerExecutor
import java.io.File
import javax.inject.Inject

@CacheableTask
abstract class RetrieveVersionManifestTask : DefaultTask() {
    @Input
    var gameVersion: String? = null

    @OutputFile
    var output: File? = null

    @Inject
    abstract fun getWorkerExecutor(): WorkerExecutor

    @TaskAction
    fun run() {
        getWorkerExecutor().noIsolation().submit(RetrieveVersionManifestAction::class.java) {
            it.gameVersion.set(gameVersion)
            it.outputFile.set(output)
        }
    }
}