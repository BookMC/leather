package org.bookmc.tannery

import club.chachy.GitVersion
import org.bookmc.tannery.tasks.export.MappingsExportTask
import org.bookmc.tannery.tasks.run.GenerateRunTask
import org.bookmc.tannery.tasks.version.RetrieveVersionManifestTask
import org.bookmc.tannery.tasks.version.download.DownloadLibrariesTask
import org.bookmc.tannery.tasks.version.download.side.Side
import org.bookmc.tannery.utils.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import java.io.File

class TanneryPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val gitVersion = GitVersion(project.projectDir)

        val retrieveVersionManifestTask =
            project.tasks.register(DOWNLOAD_MANIFEST, RetrieveVersionManifestTask::class.java) {
                it.group = GROUP
                it.gameVersion = gitVersion.branch
                it.output = File(project.projectDir, "$DATA_FOLDER/${it.gameVersion}/${it.gameVersion}.json")
            }.get()

        val downloadLibraries = project.tasks.register(DOWNLOAD_LIBRARIES_TASK, DownloadLibrariesTask::class.java) {
            it.group = GROUP
            it.dependsOn(DOWNLOAD_MANIFEST)
            it.versionJson = retrieveVersionManifestTask.output
            it.gameVersion = retrieveVersionManifestTask.gameVersion
            it.librariesFolder = retrieveVersionManifestTask.output?.parentFile?.let { f -> File(f.parentFile, "${it.gameVersion}/libraries") }?.apply {
                mkdirs()
            } ?: error("Failed to find enigma-data folder")
            it.side = Side.of(project.property("side").toString())
            it.gameJarDestination = File(project.projectDir, "$DATA_FOLDER/${it.gameVersion}/${it.gameVersion}-${it.side}.jar").apply {
                parentFile?.mkdirs()
                createNewFile()
            }
        }

        val export = project.tasks.register(EXPORT_TASK, MappingsExportTask::class.java) {
            it.group = GROUP
            it.mappingsDirectory = File(project.projectDir, MAPPINGS_DIRECTORY)
            it.outputDirectory = File(project.buildDir, EXPORT_DIRECTORY)
        }.get()

        project.tasks.withType(Jar::class.java) {
            it.dependsOn(EXPORT_TASK)
            it.from(export.outputDirectory)
            it.archiveClassifier.set(retrieveVersionManifestTask.gameVersion)
        }

        project.tasks.register("$GENERATE_RUN_TASK${retrieveVersionManifestTask.gameVersion}", GenerateRunTask::class.java) {
            it.dependsOn(DOWNLOAD_MANIFEST, DOWNLOAD_LIBRARIES_TASK)
            it.group = GROUP
            it.mainClass = "org.bookmc.tannery.TanneryKt"
            it.gameVersion = retrieveVersionManifestTask.gameVersion
            it.projectDir = project.projectDir
            it.libraryFolder = downloadLibraries.get().librariesFolder
            it.gameJar = downloadLibraries.get().gameJarDestination
            it.mappingsDirectory = File(project.projectDir, "mappings")
            it.projectName = project.name
            it.sourceSetName = "main"
        }
    }
}