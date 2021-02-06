package com.thirdwavelist.coficiando.core.data

import java.io.InputStreamReader

internal class FileReaderUtil(filePath: String) {

    val content: String

    init {
        content = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(filePath), "UTF-8").use {
            return@use it.readText()
        }
    }
}