package org.bookmc.tannery

import org.apache.commons.io.FileUtils
import org.bookmc.tannery.classloader.TanneryClassLoader
import java.io.File
import java.util.*

private var classLoader: TanneryClassLoader? = null

fun main(args: Array<String>) {
    val a = args.toMutableList()
    val libraryFolderIndex = a.indexOf("--library-folder")
    check(libraryFolderIndex != -1) { "Tannery was not launched correctly" }

    val libraries = FileUtils.listFiles(File(a[libraryFolderIndex + 1]), arrayOf("jar"), true)
        .map { it.toURI().toURL() }
        .toTypedArray()

    classLoader = TanneryClassLoader(libraries)
    Thread.currentThread().contextClassLoader = classLoader

    a.removeAt(libraryFolderIndex)
    a.removeAt(libraryFolderIndex)

    Tannery.launch(a.toTypedArray())
}

object Tannery {
    fun launch(args: Array<String>) {
        val clazz = Class.forName("cuchaz.enigma.gui.Main", false, classLoader)
        val method = clazz.getDeclaredMethod("main", Array<String>::class.java)
        method.invoke(null, args)
    }
}
