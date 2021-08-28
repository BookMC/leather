package org.bookmc.tannery.tasks.version.download

import org.bookmc.tannery.tasks.version.download.side.Side
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import org.gradle.workers.WorkerExecutor
import java.io.File
import javax.inject.Inject

@CacheableTask
abstract class DownloadLibrariesTask : DefaultTask() {
    @InputFile
    @PathSensitive(PathSensitivity.NONE)
    var versionJson: File? = null

    @InputDirectory
    @PathSensitive(PathSensitivity.NONE)
    var librariesFolder: File? = null

    @Input
    var gameVersion: String? = null

    @InputFile
    @PathSensitive(PathSensitivity.NONE)
    var gameJarDestination: File? = null

    @Input
    var side = Side.CLIENT

    @Inject
    abstract fun getWorkerExecutor(): WorkerExecutor

    @TaskAction
    fun run() {
        getWorkerExecutor().noIsolation().submit(DownloadLibrariesAction::class.java) {
            it.versionJsonFile.set(versionJson)
            it.destinationFolder.set(librariesFolder)
            it.gameVersion.set(gameVersion)
            it.gameJarDestination.set(gameJarDestination)
            it.side.set(side)
        }
    }
}