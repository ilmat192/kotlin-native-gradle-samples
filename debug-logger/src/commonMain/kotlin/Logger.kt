package org.sample.logger

internal expect val isDebug: Boolean

object Logger {
    fun info(msg: String) {
        if (isDebug) {
            println(msg)
        }
    }

    fun err(msg: String) {
        println(msg)
    }
}