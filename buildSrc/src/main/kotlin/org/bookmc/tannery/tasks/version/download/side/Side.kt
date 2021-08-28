package org.bookmc.tannery.tasks.version.download.side

enum class Side {
    CLIENT,
    SERVER;

    override fun toString(): String {
        return name.toLowerCase()
    }

    companion object {
        fun of(value: String) = values().find { it.toString().equals(value, true) }
            ?: error("Unknown side specified... $value was given but the only options are ${values().joinToString()}")
    }
}