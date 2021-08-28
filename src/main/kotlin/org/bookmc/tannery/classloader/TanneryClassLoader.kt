package org.bookmc.tannery.classloader

import java.net.URL
import java.net.URLClassLoader

class TanneryClassLoader(urls: Array<URL>) : URLClassLoader(urls, TanneryClassLoader::class.java.classLoader) {
    private val exclusions = listOf(
        "sun.", "java.", "com.sun.", "jdk.internal",
        "jdk.jfr.", "org.bookmc.tannery.", "javax.",
        "org.xml."
    )
    override fun loadClass(name: String?, resolve: Boolean): Class<*> {
        synchronized(getClassLoadingLock(name)) {
            val loadedClazz = findLoadedClass(name)
            if (loadedClazz != null) {
                return loadedClazz
            }

            val clazz = try {
                findClass(name)
            } catch (e: ClassNotFoundException) {
                super.loadClass(name, resolve)
            }

            if (resolve) {
                resolveClass(clazz)
            }

            return clazz
        }
    }

    override fun findClass(name: String?): Class<*> {
        if (name == null) throw ClassNotFoundException(name)

        if (exclusions.any { name.startsWith(it) }) {
            return parent.loadClass(name)
        }

        val bytes = getResourceAsStream(name.replace(".", "/") + ".class")
            .use { `is` -> `is`?.readBytes() }
            ?: throw ClassNotFoundException(name)

        return defineClass(name, bytes, 0, bytes.size)
    }

    override fun getResource(name: String?): URL? =
        super.getResource(name) ?: parent.getResource(name)
}