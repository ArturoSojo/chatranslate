package com.example.chatranslate

import android.os.Environment
import android.os.FileObserver
import android.util.Log
import java.io.File

private const val TAG = "MediaObserver"

class MediaObserver : FileObserver(getWatchedPath(), MOVED_TO or CREATE) {

    companion object {
        fun getWatchedPath(): String {
            return File(
                Environment.getExternalStorageDirectory(),
                "WhatsApp${File.separator}Media${File.separator}WhatsApp Images"
            ).absolutePath
        }

        private fun getDestinationDir(): File {
            return File(
                Environment.getExternalStorageDirectory(),
                "WhatsDeleted${File.separator}WhatsDeleted Images"
            )
        }
    }

    override fun onEvent(event: Int, path: String?) {
        if (event != MOVED_TO && event != CREATE) return
        if (path == null) return

        try {
            val sourceFile = File(getWatchedPath(), path)
            val destFile = File(getDestinationDir(), path)

            if (!sourceFile.exists()) {
                Log.w(TAG, "Archivo fuente no existe: ${sourceFile.absolutePath}")
                return
            }

            if (!getDestinationDir().exists()) {
                getDestinationDir().mkdirs()
            }

            if (!destFile.exists()) {
                sourceFile.copyTo(destFile, overwrite = false)
                Log.i(TAG, "Imagen copiada: ${sourceFile.name}")
            } else {
                Log.i(TAG, "Archivo ya existe: ${destFile.name}")
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error al copiar archivo: ${e.message}", e)
        }
    }
}
