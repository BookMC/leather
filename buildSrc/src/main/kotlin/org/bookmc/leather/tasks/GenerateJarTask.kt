package org.bookmc.leather.tasks

import kotlinx.coroutines.runBlocking
import org.bookmc.leather.GameInstaller
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class GenerateJarTask : DefaultTask() {
    @get:Input
    abstract val version: Property<String>

    @get:Input
    abstract val dest: Property<String>

    @TaskAction
    fun run() {
        runBlocking {
            GameInstaller.download(version.get(), File(project.projectDir, dest.get()))
        }
    }
}