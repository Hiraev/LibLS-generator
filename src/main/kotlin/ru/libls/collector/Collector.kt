package ru.libls.collector

import java.io.File
import kotlin.math.max

object Collector {

    fun collect(src: File, extension: String): List<File> {
        require(src.isDirectory)
        return recursivelyCollect(src, extension)
    }

    fun hello() = max(4, 3)

    private fun recursivelyCollect(src: File, extension: String): List<File> {
        val files = src.listFiles()
        return if (files.isNullOrEmpty()) {
            emptyList()
        } else {
            files.flatMap {
                if (it.isDirectory) recursivelyCollect(it, extension)
                else listOf(it)
            }.filter {
                it.extension == extension
            }
        }
    }

    object InternalCollector {

        fun internalHello() = hello()

    }

    class InternalCollectorClass() {

        fun internalCollectorClass() = InternalCollector.internalHello()

        val aaa = InternalCollector.internalHello()

    }

}
