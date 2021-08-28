package org.bookmc.tannery.tasks.run

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import org.gradle.workers.WorkerExecutor
import java.io.File
import javax.inject.Inject

abstract class GenerateRunTask : DefaultTask() {
    @Input
    var mainClass: String? = null

    @Input
    var gameVersion: String? = null

    @InputDirectory
    @PathSensitive(PathSensitivity.NONE)
    var projectDir: File? = null

    @InputFile
    var gameJar: File? = null

    @InputDirectory
    @PathSensitive(PathSensitivity.NONE)
    var mappingsDirectory: File? = null

    @Input
    var sourceSetName: String? = null

    @Input
    var projectName: String? = null

    @OutputDirectory
    var libraryFolder: File? = null

    @Inject
    abstract fun getWorkerExecutor(): WorkerExecutor

    @TaskAction
    fun run() {
        getWorkerExecutor().noIsolation().submit(GenerateRunAction::class.java) {
            it.mainClass.set(mainClass)
            it.libraryFolder.set(libraryFolder)
            it.gameVersion.set(gameVersion)
            it.projectDir.set(projectDir)
            it.gameJar.set(gameJar)
            it.mappingsDirectory.set(mappingsDirectory)
            it.projectName.set(projectName)
            it.sourceSetName.set(sourceSetName)
        }
    }
}