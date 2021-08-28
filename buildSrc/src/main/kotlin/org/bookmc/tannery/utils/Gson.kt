package org.bookmc.tannery.utils

import com.google.gson.Gson

val gson = Gson()

inline fun <reified T> String.fromJson(): T = gson.fromJson(this, T::class.java)