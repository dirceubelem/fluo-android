package br.com.fluo.fluo.helper

import java.io.File

object IOHelper {

    fun createDir(dirName: String): File {
        val dir = File(dirName)

        if (!dir.exists()) {
            dir.mkdirs()
        }

        return dir
    }


}